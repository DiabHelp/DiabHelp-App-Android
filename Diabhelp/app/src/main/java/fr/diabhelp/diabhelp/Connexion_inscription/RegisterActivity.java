package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.ApiCallTask;
import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.BDD.User;
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
/*        if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    
    boolean is_correctFields(ArrayList<Integer> fieldsNames)
    {
        for (int i = 0; i < fieldsNames.size();i++)
        {
            if (((EditText)findViewById(fieldsNames.get(i))).getText().toString().isEmpty())
            {
                return false;
            }
        }
        return (true);
    }

    public void onRegisterClick(View v)
    {
        ConnexionState connect = new ConnexionState(this);
        String login = ((EditText)findViewById(R.id.login_input)).getText().toString();
        String pwd = ((EditText)findViewById(R.id.pwd_input)).getText().toString();
        bdd = new DAO(this);
        ArrayList<Integer> fieldNames = new ArrayList<>();

        fieldNames.add(R.id.email_input);
        fieldNames.add(R.id.login_input);
        fieldNames.add(R.id.pwd_input);
        if (fieldNames.size() > 0 && is_correctFields(fieldNames))
        {
            User User = new User(0, login, pwd);
            bdd.open();
            if (bdd.isUserAlreadyFilled("0") == false)
                bdd.AddUser(User);
            else
                bdd.UpdateUser(User);
            bdd.close();
            if (connect.get_status())
            {
                new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "sendID").execute("3", "register", "email", "login", login, "password", pwd);
            }
            else {
                //MyToast.getinstance().displayCustomWarningMessage("Vous n'etes pas conn�ct� � internet, R�essayez plus tard.", R.layout.toast_layout, R.id.toast_layout_root, Toast.LENGTH_LONG, this);
                Toast.makeText(getApplicationContext(), "Vous n'etes pas connecte a internet, Reessayez plus tard.", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            //MyToast.getinstance().displayCustomWarningMessage("", R.layout.toast_layout, R.id.toast_layout_root, Toast.LENGTH_LONG, this);
        }
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (action.compareTo("sendID") == 0)
        {
            manage_getToken(s, type, action);
        }
    }

    private void manage_getToken(String s, int type, String action) {
        if (action.compareTo("LOGIN_EXIST") == 0) {
            //MyToast.getinstance().displayCustomWarningMessage("le login/mail n'est pas disponible", R.layout.toast_layout, R.id.toast_layout_root, Toast.LENGTH_LONG, this);
            Toast.makeText(getApplicationContext(), "le login/mail n'est pas disponible", Toast.LENGTH_SHORT).show();
           }
        else {
            new AlertDialog.Builder(this)
                    .setTitle("Enregistrement")
                    .setMessage("Vous avez bien ete enregistre ".concat("\n" + "Vous pouvez des maintenant vous connecter sur votre application ou sur notre site internet www.diabhelp.fr"))
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            RegisterActivity.this.finish();
                        }
                    })
                    .show();

        }
    }
}
