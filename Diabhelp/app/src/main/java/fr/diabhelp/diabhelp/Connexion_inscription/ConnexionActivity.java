package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONException;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.API.Asynctasks.ConnexionAPICallTask;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseConnexion;
import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.BDD.UserDAO;
import fr.diabhelp.diabhelp.BDD.Ressource.User;
import fr.diabhelp.diabhelp.BDD.User;
import fr.diabhelp.diabhelp.Services.MyInstanceIDListenerService;
import fr.diabhelp.diabhelp.Services.RegistrationIntentService;
import fr.diabhelp.diabhelp.Utils.NetworkUtils;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.MyToast;

public class ConnexionActivity extends Activity implements IApiCallTask<ResponseConnexion> {


    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String USERNAME = "fr.diabhelp.diabhelp.username";
    public static final String IS_NETWORK = "fr.diabhelp.diabhelp.isNetwork";
    public static final String SESSION = "fr.diabhelp.diabhelp.session";
    public static final String AUTO_CONNEXION_PREFERENCE = "auto_connexion";
    public static final String TOKEN = "token";
    public static final String TYPE_USER = "type_user";
    public static final String ID_USER = "id";

    public static final String SENT_TOKEN_TO_SERVER = "token_is_sent_to_server";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private DAO dao = null;
    private SQLiteDatabase db = null;

    private String _login_input = null;
    private String _pwd_input = null;
    private String _session = null;

    public ProgressDialog _progress = null;

    public static SharedPreferences _settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        dao = DAO.getInstance(this);
        db = dao.open();
//        inflateLoadingBar();
//        //recupere les préférences dans le fichier donné en param
        _settings = getSharedPreferences(PREF_FILE, Context.MODE_WORLD_READABLE);
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
        UserDAO.deleteAllUsers(db);
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
        _login_input = ((EditText) findViewById(R.id.login_input)).getText().toString();
        _pwd_input = ((EditText) findViewById(R.id.pwd_input)).getText().toString();

        ArrayList<Integer> fieldNames = new ArrayList<>();
        fieldNames.add(R.id.login_input);
        fieldNames.add(R.id.pwd_input);
        Boolean correctFields = isCorrectFields(fieldNames);

        if (!correctFields) {
            MyToast.getInstance().displayWarningMessage("Veuillez remplir tous les champs", Toast.LENGTH_LONG, this);
        } else {
            if (NetworkUtils.getConnectivityStatus(getApplicationContext())) {tryConnectWithNetwork();}
            else {tryConnectWithoutNetwork();}
        }
    }

    public void connectAutomaticaly()
    {
        //findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        if (NetworkUtils.getConnectivityStatus(getApplicationContext())) {
            System.out.println("connexion en ligne");
            tryConnectWithNetwork();
        }
        else {
            System.out.println("connexion hors ligne");
            tryConnectWithoutNetwork();
        }
    }

    private void tryConnectWithoutNetwork() {
        User user = null;
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            if ((user = UserDAO.selectUser(db)) == null){
                MyToast.getInstance().displayWarningMessage("Une erreur est survenue, veuillez vous connectez manuellement", Toast.LENGTH_LONG, this);
                //on desactive la connexion automatique pour lui permettre de se connecter manuellement
                disableAutomaticConnexion();
                //on essaye de se connecter avec les Ids stockés dans la base sqlLITE
            }
            else {
                launchCoreWithoutConnexion(user);
            }
        }
        else {
            if ((user = UserDAO.selectUser(db)) == null){
                MyToast.getInstance().displayWarningMessage("La connexion hors ligne necessite une première connexion en ligne pour pouvoir être active", Toast.LENGTH_LONG, this);
            }
            else{
                if (!_login_input.equals(user.getUser()) || !_pwd_input.equals(user.getPwd())) {
                    MyToast.getInstance().displayWarningMessage("Mauvais nom de compte/mot de passe", Toast.LENGTH_LONG, this);
                }
                else {
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
        //si connexion automatique
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            User user = null;
            if ((user = UserDAO.selectUser(db)) == null){
                MyToast.getInstance().displayWarningMessage("Une erreur est survenue, veuillez vous connectez manuellement", Toast.LENGTH_LONG, this);
                //on desactive la connexion automatique pour lui permettre de se connecter manuellement
                disableAutomaticConnexion();
            }
            //on essaye de se connecter avec les Ids stockés dans la base sqlLITE
            else {
                Log.i(getLocalClassName(),"Ids de l'user = " + user.getUser() + " " + user.getPwd());
                System.out.println("Ids de l'user = " + user.getUser() + " " + user.getPwd());
                bdd.close();
                new ConnexionAPICallTask(this).execute(user.getUser(), user.getPwd());
            }
        }
        else {
            bdd.close();
            System.out.println("je vais tenter la connexion non automatique");
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
        _progress = progress;
        ConnexionActivity.Error error = response.getError();
        Integer errorCode = error.getErrorCode();
        if (errorCode != 0) {
            manageError(response.getError());
        }
        else if (action.equals("initSession")) {
            initSession(response.getCookie(), response.getTypeUser(), response.getIdUser());
        }
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
                    UserDAO.deleteUser(0, db);
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
     */
    private void initSession(String token, String typeUser, String idUser)
    {
        SharedPreferences.Editor edit = _settings.edit();
        edit.putString(TOKEN,token);
        edit.putString(TYPE_USER, typeUser);
        edit.putString(ID_USER, idUser);
        edit.apply();
        User user = null;
        if (!_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)){
            //TODO recevoir l'id de l'user
            if (idUser != null)
                user = new User(Long.valueOf(idUser), _login_input, _pwd_input);
            else
                user = new User(0L, _login_input, _pwd_input);
            if (((CheckBox) findViewById(R.id.checkbox_connexion_auto)).isChecked()){
                SharedPreferences.Editor edit = _settings.edit();
                edit.putBoolean(AUTO_CONNEXION_PREFERENCE, true);
                edit.putString(TYPE_USER, typeUser);
                edit.apply();
                if (!UserDAO.isUserAlreadyFilled(idUser, db)) {
                    UserDAO.addUser(user, db);
                }
            }
        }
        else {
            user = UserDAO.selectUser(db);
            if (user == null){
                _progress.dismiss();
                disableAutomaticConnexion();
                Log.e("ConnexionActivity", "Erreur lors de la récupération de l'utilisateur en db locale");
                return;
            }
        }
        launchCoreWithConnexion(user);
    }

    /**
     * Lance la {@link CoreActivity} lorsque l'utilisateur est connecté à internet
     * verifie aussi si le token representant de manière unique l'application, à été envoyé au serveur
     * en vue de recevoir des alertes pushs
     * @param user correspond à la classe contenant les informations de l'utilisateur
     */
    public void launchCoreWithConnexion(User user)
    {
        Intent coreInt = new Intent(this, CoreActivity.class);
        coreInt.putExtra(USERNAME, user.getUser());
        coreInt.putExtra(IS_NETWORK, false);
        System.out.println("session = " + _session);
        coreInt.putExtra(SESSION, _session);
        Boolean tokenGcmSentToServer = _settings.getBoolean(SENT_TOKEN_TO_SERVER, false);
        if (!tokenGcmSentToServer) {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
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

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(getLocalClassName(), "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor edit = _settings.edit();
        edit.putString(TOKEN, "");
        edit.putString(TYPE_USER, "");
        edit.putString(ID_USER, "");
        edit.commit();
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
