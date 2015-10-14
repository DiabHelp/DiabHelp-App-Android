package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import fr.diabhelp.diabhelp.ApiCallTask;
import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.BDD.User;
import fr.diabhelp.diabhelp.Chore.ChoreActivity;
import fr.diabhelp.diabhelp.ConnexionState;
import fr.diabhelp.diabhelp.IApiCallTask;
import fr.diabhelp.diabhelp.JsonUtils;
import fr.diabhelp.diabhelp.MyToast;
import fr.diabhelp.diabhelp.R;

public class ConnexionActivity extends Activity implements IApiCallTask {


    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String USERNAME = "fr.diabhelp.diabhelp.username";
    public static final String IS_NETWORK = "fr.diabhelp.diabhelp.isNetwork";
    public static final String SESSION = "fr.diabhelp.diabhelp.session";
    public static final String AUTO_CONNEXION_PREFERENCE = "auto_connexion";

    private String _login_input = null;
    private String _pwd_input = null;

    private String _session = null;

    public static SharedPreferences _settings = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        inflateLoadingBar();
        //recupere les préférences dans le fichier donné en param
        _settings = getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            connectAutomaticaly();
        }
}

    //on ajoute le loading panel à la vue en cours et on le cache
    public void inflateLoadingBar()
    {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup parent = (ViewGroup) findViewById(R.id.root_co);
        inflater.inflate(R.layout.loading_connexion, parent);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
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
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void connectByInput(View view)
    {
        Boolean connected_to_network = false;
        ConnexionState connexion = new ConnexionState(getApplicationContext());

        _login_input = ((EditText) findViewById(R.id.login_input)).getText().toString();
        _pwd_input = ((EditText) findViewById(R.id.pwd_input)).getText().toString();

        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        if (connected_to_network = connexion.get_status()) {
            System.out.println("test avec connexion");

            tryConnectWithNetwork();
        }
        else {
            System.out.println("test sans connexion");
            tryConnectWithoutNetwork();
        }
    }
    public void connectAutomaticaly()
    {
        Boolean connected_to_network = false;
        ConnexionState connexion = new ConnexionState(getApplicationContext());
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        if (connected_to_network = connexion.get_status()) {
            tryConnectWithNetwork();
        }
        else {
            tryConnectWithoutNetwork();
        }

    }

    private void tryConnectWithoutNetwork() {
        DAO bdd  = new DAO(this);
        bdd.open();
        User user = null;
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            if ((user = bdd.SelectUser()) == null){
                MyToast.getinstance().displayWarningMessage("Une erreur est survenue, veuillez vous connectez manuellement", Toast.LENGTH_LONG, this);
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
            if ((user = bdd.SelectUser()) == null){
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getinstance().displayWarningMessage("La connexion hors ligne necessite une première connexion en ligne pour pouvoir être active", Toast.LENGTH_LONG, this);
                bdd.close();
            }
            else{
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                if (!_login_input.equals(user.getUser()) || !_pwd_input.equals(user.getPwd())) {
                    MyToast.getinstance().displayWarningMessage("Mauvais nom de compte/mot de passe", Toast.LENGTH_LONG, this);
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
        Intent coreInt = new Intent(ConnexionActivity.this, ChoreActivity.class);
        // coreiInt.setFlags(R.anim.abc_popup_enter);
        coreInt.putExtra(USERNAME, user.getId());
        coreInt.putExtra(IS_NETWORK, false);
        startActivity(coreInt);
        finish();
    }

    private void tryConnectWithNetwork() {
        DAO bdd  = new DAO(this);
        bdd.open();
        User user = null;
        //si connexion automatique
        if (_settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            if ((user = bdd.SelectUser()) == null){
                MyToast.getinstance().displayWarningMessage("Une erreur est survenue, veuillez vous connectez manuellement", Toast.LENGTH_LONG, this);
                //on desactive la connexion automatique pour lui permettre de se connecter manuellement
                disableAutomaticConnexion();
                bdd.close();
            }
            //on essaye de se connecter avec les Ids stockés dans la base sqlLITE
            else {
                bdd.close();
                new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "getSession").execute("2", "connect", "login", user.getUser(), "password", user.getPwd());
            }
        }
        else {
            bdd.close();
            new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "getSession").execute("2", "connect", "login", _login_input, "password", _pwd_input);
        }
    }
    
    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (action.equals("getSession")){
            getSession(s, type);
        }
        
    }

    private void getSession(String datas, int type) {
        //si il y a des erreurs
        if (datas.equals("io exception") || datas.equals("DB_ERROR"))
        {
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            MyToast.getinstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez verifier sur le site l'état du serveur", Toast.LENGTH_LONG, this);
            finish();
        }
        else if (datas.equals("WRONG_IDS")){
            System.out.println("WRONGS IDS connexion");
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getinstance().displayWarningMessage("Mauvais login/mot de passe", Toast.LENGTH_LONG, this);
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
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                launchCoreWithConnexion(user);
            }
            //si la chaine est valide et que la co auto est active
            else if ((_session = JsonUtils.getStringfromKey(JsonUtils.get_obj(datas), "token")) != null && _settings.getBoolean(AUTO_CONNEXION_PREFERENCE, false)) {
                User user = bdd.SelectUser();
                bdd.close();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                launchCoreWithConnexion(user);
            }
        bdd.close();
        }
        //une erreur inatendue c'est produite
    }

    public void launchCoreWithConnexion(User user)
    {
        Intent coreInt = new Intent(ConnexionActivity.this, ChoreActivity.class);
        // coreiInt.setFlags(R.anim.abc_popup_enter);
        coreInt.putExtra(USERNAME, user.getId());
        coreInt.putExtra(IS_NETWORK, false);
        System.out.println("session = " + _session);
        coreInt.putExtra(SESSION, _session);
        startActivity(coreInt);
        finish();
    }
}
