package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.ApiCallTask;
import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.ConnexionState;
import fr.diabhelp.diabhelp.IApiCallTask;
import fr.diabhelp.diabhelp.MyToast;
import fr.diabhelp.diabhelp.R;

public class RegisterActivity extends Activity implements IApiCallTask {

    DAO bdd;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        inflateLoadingBar();
    }

    public void inflateLoadingBar()
    {
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup parent = (ViewGroup) findViewById(R.id.root_register);
        inflater.inflate(R.layout.loading_connexion, parent);
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_core, menu);
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

    Boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void onRegisterClick(View view) {
        findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        ConnexionState connect = new ConnexionState(this);
        String email = ((EditText)findViewById(R.id.email_input)).getText().toString();
        String login = ((EditText)findViewById(R.id.login_input)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.pwd_input)).getText().toString();
        bdd = new DAO(this);

        ArrayList<Integer> fieldNames = new ArrayList<>();
        fieldNames.add(R.id.email_input);
        fieldNames.add(R.id.login_input);
        fieldNames.add(R.id.pwd_input);

        Boolean correctFields = isCorrectFields(fieldNames);
        Boolean goodEmailFormat = isEmailValid(email);

        if (correctFields && goodEmailFormat)
        {
            if (connect.getStatus()) {
                new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "sendID").execute("3", "register", "email", email, "login", login, "password", pwd);
            }
            else {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getInstance().displayWarningMessage("Vous n'êtes pas connecté à internet, veuillez réessayer plus tard", Toast.LENGTH_LONG, this);
            }
        }
        else {
            if (!correctFields) {
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getInstance().displayWarningMessage("Veuillez remplir tous les champs", Toast.LENGTH_LONG, this);
            }
            else if (!goodEmailFormat){
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                MyToast.getInstance().displayWarningMessage("format de l'adresse email incorrecte", Toast.LENGTH_LONG, this);
            }
        }
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (action.compareTo("sendID") == 0)
        {
            sendId(s, type, action);
        }
    }

    private void sendId(String s, int type, String action) {
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        if ("ALREADY_USE".equals(s)) {
            MyToast.getInstance().displayWarningMessage("email/login déja utilisé", Toast.LENGTH_LONG, this);
        }
        else if ("TRUE".equals(s)){
            MyToast.getInstance().displayWarningMessage("Le compte a été créé.\n Vous pouvez dés maintenant vous connecter sur votre application ou sur notre site internet www.diabhelp.fr", Toast.LENGTH_LONG, this);
            finish();
        }
    }
}
