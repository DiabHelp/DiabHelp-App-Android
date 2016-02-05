package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.API.ApiCallTask;
import fr.diabhelp.diabhelp.API.ConnexionAPICallTask;
import fr.diabhelp.diabhelp.API.ResponseConnexion;
import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.BDD.User;
import fr.diabhelp.diabhelp.ConnexionState;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.JsonUtils;
import fr.diabhelp.diabhelp.MyToast;
import fr.diabhelp.diabhelp.R;

public class ConnexionActivity extends Activity implements IApiCallTask<ResponseConnexion> {


    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String USERNAME = "fr.diabhelp.diabhelp.username";
    public static final String IS_NETWORK = "fr.diabhelp.diabhelp.isNetwork";
    public static final String SESSION = "fr.diabhelp.diabhelp.session";
    public static final String AUTO_CONNEXION_PREFERENCE = "auto_connexion";

    private String _login_input = null;
    private String _pwd_input = null;
    private String _session = null;

    public ProgressDialog _progress = null;

    public static SharedPreferences _settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
//        inflateLoadingBar();
//        //recupere les préférences dans le fichier donné en param
        _settings = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            Log.i("ConnexionActivity", "connexion automatique");
            connectAutomaticaly();
        }
}

    //on ajoute le loading panel à la vue en cours et on le cache
    public void inflateLoadingBar()
    {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup parent = (ViewGroup) findViewById(R.id.root_co);
        inflater.inflate(R.layout.loading_connexion, parent);
        //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    //on desactive la connexion automatique pour permettre à l'utilisateur de se connecter manuellement
    private void disableAutomaticConnexion() {
        SharedPreferences.Editor edit = _settings.edit();
        edit.putBoolean(AUTO_CONNEXION_PREFERENCE, false);
        edit.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_connexion, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    boolean isCorrectFields(ArrayList<Integer> fieldsNames)
    {
        for (int i = 0; i < fieldsNames.size();i++)
        {
            if ((((EditText)findViewById(fieldsNames.get(i))).getText().toString().isEmpty())) {
                return false;
            }
        }
        return (true);
    }

    public void connectByInput(View view) {
        ConnexionState connexion = new ConnexionState(getApplicationContext());

        _login_input = ((EditText) findViewById(R.id.login_input)).getText().toString();
        _pwd_input = ((EditText) findViewById(R.id.pwd_input)).getText().toString();

        ArrayList<Integer> fieldNames = new ArrayList<>();
        fieldNames.add(R.id.login_input);
        fieldNames.add(R.id.pwd_input);
        Boolean correctFields = isCorrectFields(fieldNames);

        if (!correctFields) {
            MyToast.getInstance().displayWarningMessage("Veuillez remplir tous les champs", Toast.LENGTH_LONG, this);
        } else {
            if (connexion.getStatus()) {tryConnectWithNetwork();}
            else {tryConnectWithoutNetwork();}
        }
    }

    public void connectAutomaticaly()
    {
        ConnexionState connexion = new ConnexionState(getApplicationContext());
        //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        if (connexion.getStatus()) {
            System.out.println("connexion en ligne");
            tryConnectWithNetwork();
        }
        else {
            System.out.println("connexion hors ligne");
            tryConnectWithoutNetwork();
        }
    }

    private void tryConnectWithoutNetwork() {
        DAO bdd  = new DAO(this);
        bdd.open();
        User user = null;
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            if ((user = bdd.selectUser()) == null){
                MyToast.getInstance().displayWarningMessage("Une erreur est survenue, veuillez vous connectez manuellement", Toast.LENGTH_LONG, this);
                //on desactive la connexion automatique pour lui permettre de se connecter manuellement
                disableAutomaticConnexion();
                bdd.close();
                //on essaye de se connecter avec les Ids stockés dans la base sqlLITE
            }
            else {
                bdd.close();
                launchCoreWithoutConnexion(user);
            }
        }
        else {
            if ((user = bdd.selectUser()) == null){
                MyToast.getInstance().displayWarningMessage("La connexion hors ligne necessite une première connexion en ligne pour pouvoir être active", Toast.LENGTH_LONG, this);
                bdd.close();
            }
            else{
                if (!_login_input.equals(user.getUser()) || !_pwd_input.equals(user.getPwd())) {
                    MyToast.getInstance().displayWarningMessage("Mauvais nom de compte/mot de passe", Toast.LENGTH_LONG, this);
                    bdd.close();
                }
                else {
                    bdd.close();
                    launchCoreWithoutConnexion(user);
                }
            }
        }
    }

    public void launchCoreWithoutConnexion(User user)
    {
        Intent coreInt = new Intent(ConnexionActivity.this, CoreActivity.class);
        coreInt.putExtra(USERNAME, user.getId());
        coreInt.putExtra(IS_NETWORK, false);
        startActivity(coreInt);
        finish();
    }

    private void tryConnectWithNetwork() {
        DAO bdd  = new DAO(this);
        bdd.open();
        //si connexion automatique
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            User user = null;
            if ((user = bdd.selectUser()) == null){
                MyToast.getInstance().displayWarningMessage("Une erreur est survenue, veuillez vous connectez manuellement", Toast.LENGTH_LONG, this);
                //on desactive la connexion automatique pour lui permettre de se connecter manuellement
                disableAutomaticConnexion();
                bdd.close();
            }
            //on essaye de se connecter avec les Ids stockés dans la base sqlLITE
            else {
                System.out.println("Ids de l'user = " + user.getUser() + " " + user.getPwd());
                bdd.close();
//                new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "getBasicAuthSession").execute("2", "connect", "login", user.getUser(), "password", user.getPwd());
                new ConnexionAPICallTask(this).execute(user.getUser(), user.getPwd());
            }
        }
        else {
            bdd.close();
            System.out.println("je vais tenter la connexion");
            //new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "getBasicAuthSession").execute("2", "login_check", "username", _login_input, "password", _pwd_input);
            new ConnexionAPICallTask(this).execute(_login_input, _pwd_input);
        }
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
//        System.out.println("retour requete  = " + s);
//        TODO ENLEVER
//        return;
//        if (action.equals("getBasicAuthSession")){
//            getBasicAuthSession(s, type);
//        }
    }

    @Override
    public void onBackgroundTaskCompleted(ResponseConnexion response, String action, ProgressDialog progress){
        System.out.println("passé par la méthode implémentée");
        _progress = progress;
        ConnexionActivity.Error error = response.getError();
        Integer errorCode = error.getErrorCode();
        if (errorCode != 0) {
            System.out.println("a trouvé un erreur");
            manageError(response.getError());
        }
        else if (action.equals("initSession")) {
            System.out.println("va init session");
            _session = response.getCookie();
            initSession();
        }
        System.out.println("merde");
    }

    /**
     * Gere les erreurs survenues pendant la récoltes des données sur l'API dans {@link ConnexionAPICallTask}
     * @param error correspond au type d'erreur rencontré dans {@link ConnexionAPICallTask#doInBackground(String...)}
     */
    private void manageError(Error error) {
        _progress.dismiss();
        switch (error)
        {
            case BAD_CREDENTIALS:{
                boolean coAuto = _settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false);
                if (coAuto == true) {
                    disableAutomaticConnexion();
                    MyToast.getInstance().displayWarningMessage("Il semblerait que vous ayez mis à jour vos identifiants, veuillez vous reconnecter", Toast.LENGTH_LONG, this);
                    Log.e("ConnexionActivity", "Les identifiants locaux sont differents des identifiants serveur");
                    DAO bdd = new DAO(this);
                    bdd.open();
                    bdd.deleteUser(0);
                }
                else if (coAuto == false) {
                    MyToast.getInstance().displayWarningMessage("Mauvais identifiants", Toast.LENGTH_LONG, this);
                    Log.i("ConnexionActivity", "Les indenfiants sont invalides");
                }
                break;
            }
            case SERVER_ERROR:{
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, this);
                Log.e("ConnexionApiCallTask", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case DB_ERROR:{
                Log.e("ConnexionApiCallTask", "Une erreur est survenue lors de la recherche de données sur la db du serveur");
            }
        }
    }

    /**
     * Effectue les actions de mise à jour de la base utilisateur et d'activation de la connexion automatique quand cela est necessaire puis
     * lance l'initialisation du {@link CoreActivity}
     * @param token correspond au token de session renvoyé par {@link ConnexionAPICallTask#onPostExecute(ResponseConnexion)}} pour effectuer des actions en ligne.
     */
    private void initSession()
    { System.out.println("init");

        User user = null;
        DAO bdd = new DAO(this);
        bdd.open();
        if (!_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)){
            user = new User(0L, _login_input, _pwd_input);
            if (((CheckBox) findViewById(R.id.checkbox_connexion_auto)).isChecked()){
                SharedPreferences.Editor edit = _settings.edit();
                edit.putBoolean(AUTO_CONNEXION_PREFERENCE, true);
                edit.commit();
                if (!bdd.isUserAlreadyFilled("0")) {
                    bdd.AddUser(user);
                }
            }
        }
        else {
            user = bdd.selectUser();
            if (user == null){
                _progress.dismiss();
                Log.e("ConnexionActivity", "Erreur lors de la récupération de l'utilisateur en bdd locale");
                return;
            }
        }
        System.out.println("fin");
        bdd.close();
        launchCoreWithConnexion(user);
    }

    private void getSession(String datas, int type) {
        //si il y a des erreurs
        if (datas != null) {
            if (datas.equals("io exception") || datas.equals("DB_ERROR")) {
                //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez verifier sur le site l'état du serveur", Toast.LENGTH_LONG, this);
            } else if ("WRONG_IDS".equals(datas)) {
                System.out.println("WRONGS IDS connexion");
                //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getInstance().displayWarningMessage("Mauvais login/mot de passe", Toast.LENGTH_LONG, this);
            }
            //si il n'y a pas d'erreurs
            else {
                DAO bdd = new DAO(this);
                bdd.open();
                //si la chaine est valide et que la co auto est inactive
                if ((_session = JsonUtils.getStringfromKey(JsonUtils.get_obj(datas), "token")) != null && !_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
                    User user = new User(0L, _login_input, _pwd_input);
                    if (!bdd.isUserAlreadyFilled("0")) {
                        bdd.AddUser(user);
                        bdd.close();
                    }
                    if (((CheckBox) findViewById(R.id.checkbox_connexion_auto)).isChecked()) {
                        SharedPreferences.Editor edit = _settings.edit();
                        edit.putBoolean(AUTO_CONNEXION_PREFERENCE, true);
                        edit.commit();
                    }
                    //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    launchCoreWithConnexion(user);
                }
                //si la chaine est valide et que la co auto est active
                else if ((_session = JsonUtils.getStringfromKey(JsonUtils.get_obj(datas), "token")) != null && _settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
                    User user = bdd.selectUser();
                    bdd.close();
                    //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                    launchCoreWithConnexion(user);
                }
                bdd.close();
            }
        }
        //une erreur inatendue c'est produite
        else {
            disableAutomaticConnexion();
            MyToast.getInstance().displayWarningMessage("Une erreur innatendue s'est produite", Toast.LENGTH_LONG, this);
        }
    }

    /**
     * Lance la {@link CoreActivity} lorsque l'utilisateur est connecté à internet
     * @param user correspond à la classe contenant les informations de l'utilisateur
     */
    public void launchCoreWithConnexion(User user)
    {
        Intent coreInt = new Intent(this, CoreActivity.class);
        coreInt.putExtra(USERNAME, user.getUser());
        coreInt.putExtra(IS_NETWORK, false);
        System.out.println("session = " + _session);
        coreInt.putExtra(SESSION, _session);
        _progress.dismiss();
        startActivity(coreInt);
        finish();
    }

    /**
     * Lance la {@link RegisterActivity}
     * @param view correspond au bouton cliqué qui a appelé cette fonction
     */
    public void signUp(View view)
    {
        Intent signUpInt = new Intent(ConnexionActivity.this, RegisterActivity.class);
        startActivity(signUpInt);
    }

    public enum  Error
    {
        NONE(0),
        BAD_CREDENTIALS(1),
        DB_ERROR(2),
        SERVER_ERROR(3);

        private Integer errorCode;

        Error(Integer i) {
            this.errorCode = i;
        }

        public Integer getErrorCode() {
            return this.errorCode;
        }
    }
}
