package fr.diabhelp.diabhelp.Menu;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.diabhelp.diabhelp.API.Asynctasks.ProfilGetAPICallTask;
import fr.diabhelp.diabhelp.API.Asynctasks.ProfilPosrtAPICallTask;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilGet;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilSet;
import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.R.drawable;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;
import fr.diabhelp.diabhelp.R.string;
import fr.diabhelp.diabhelp.Utils.DateUtils;
import fr.diabhelp.diabhelp.Utils.MyToast;

/**
 * Created by naqued on 21/04/16.
 */

public class ProfileActivity extends AppCompatActivity
        implements OnNavigationItemSelectedListener, IApiCallTask
{

    ProgressDialog _progress;
    public static SharedPreferences _settings;

    private EditText nom, prenom, email, dateNaissance, telephone, organisme, pwd, cpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_profile);
        this.nom = (EditText) this.findViewById(id.editName);
        this.prenom = (EditText) this.findViewById(id.editLastName);
        this.email = (EditText) this.findViewById(id.editEmail);
        this.dateNaissance = (EditText) this.findViewById(id.editBirthDate);
        this.telephone = (EditText) this.findViewById(id.editPhone);
        this.organisme = (EditText) this.findViewById(id.editOrganism);
        this.pwd = (EditText) this.findViewById(id.editPassword);
        this.cpwd = (EditText) this.findViewById(id.editPassword2);

        this.dateNaissance.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker;

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                mDatePicker = new DatePickerDialog(ProfileActivity.this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        c.set(year, month, day);
                        Date d = c.getTime();
                        SimpleDateFormat sf = new SimpleDateFormat(DateUtils.DATE_PATERN);
                        String dateStr = sf.format(d);
                        ProfileActivity.this.dateNaissance.setText(dateStr);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        ProfileActivity._settings = this.getSharedPreferences(ConnexionActivity.PREF_FILE, Context.MODE_WORLD_READABLE);

        Toolbar toolbar = (Toolbar) this.findViewById(id.toolbar);
        toolbar.setTitle("Profil");
        this.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Carnetintent = new Intent(ProfileActivity.this, CoreActivity.class);
                ProfileActivity.this.startActivity(Carnetintent);
                ProfileActivity.this.finish();
            }
        });
        new ProfilGetAPICallTask(this, this).execute(ProfileActivity._settings.getString(ConnexionActivity.ID_USER, ""));
/*        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == id.nav_website) {
            String url = "http://www.diabhelp.fr";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            this.startActivity(intent);
        } else if (id == id.nav_help) {

        } else if (id == id.nav_faq) {

        } else if (id == id.nav_logout) {

        } else if (id == id.nav_facebook) {

        }
/*        else if (id == R.id.nav_twitter) {

        }*/

        DrawerLayout drawer = (DrawerLayout) this.findViewById(id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed()
    {
        this.finish();

        super.onBackPressed();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {

    }

    @Override
    public void onBackgroundTaskCompleted(Object bodyResponse, String action, ProgressDialog progress) {
        this._progress = progress;
        if (action.equals("getInfo"))
        {
            ResponseProfilGet response = (ResponseProfilGet) bodyResponse;
            Error error = response.getError();
            if (error != ProfileActivity.Error.NONE)
                this.manageError(error);
            else
            {
                System.out.println("PAS D ERREUR LORS DU CHARGEMENT DU PROFIL");
                this.setFields(response);
                this._progress.dismiss();
            }
        }
        if (action.equals("setInfo"))
        {
            ResponseProfilSet response = (ResponseProfilSet) bodyResponse;
            ProfileActivity.Error error = response.getError();
            if (error != ProfileActivity.Error.NONE)
                this.manageError(error);
            else {
                System.out.println("PAS D ERREUR LORS DE L ENREGISTREMENT DU PROFIL");
                this._progress.dismiss();
                MyToast.getInstance().displayWarningMessage(this.getString(string.profile_save_success), Toast.LENGTH_SHORT, this);
            }
        }
    }

    private void setFields(ResponseProfilGet response) {
        this.nom.setText(response.getLastname());
        this.prenom.setText(response.getFirstname());
        this.email.setText(response.getEmail());
        String birthDate = response.getBirthdate();
        if (birthDate != null && !birthDate.isEmpty() && !birthDate.equals("null"))
            this.dateNaissance.setText(response.getBirthdate());
        String phone = response.getMobile();
        if (phone != null && !phone.isEmpty() && !phone.equals("null"))
            this.telephone.setText(response.getMobile());
        String organism = response.getOrganism();
        if (organism != null && !organism.isEmpty() && !organism.equals("null"))
            this.organisme.setText(response.getOrganism());
    }

    private void manageError(ProfileActivity.Error error) {
        this._progress.dismiss();
        switch (error)
        {
            case SERVER_ERROR:
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, this);
                Log.e("ProfileActivity", "Problème survenu lors de la connexion au serveur");
                break;
        }
    }

    public void saveProfil(View view) {
        String password = this.pwd.getText().toString();
        String cPassword = this.pwd.getText().toString();
        if (password != null && cPassword != null && !password.isEmpty() && !cPassword.isEmpty()) {
            if (password.length() < 6)
                MyToast.getInstance().displayWarningMessage(this.getString(string.profile_password_incorrect), Toast.LENGTH_LONG, this);
            else if (!password.equals(cPassword))
                MyToast.getInstance().displayWarningMessage(this.getString(string.error_passwords_differ), Toast.LENGTH_LONG, this);
            else
                new ProfilPosrtAPICallTask(this, this).execute(ProfileActivity._settings.getString(ConnexionActivity.ID_USER, ""), this.email.getText().toString(), this.nom.getText().toString(), this.prenom.getText().toString(), this.telephone.getText().toString(), this.dateNaissance.getText().toString(), this.organisme.getText().toString(), this.pwd.getText().toString());
        }
        else
            new ProfilPosrtAPICallTask(this, this).execute(ProfileActivity._settings.getString(ConnexionActivity.ID_USER, ""), this.email.getText().toString(), this.nom.getText().toString() , this.prenom.getText().toString(), this.telephone.getText().toString(), this.dateNaissance.getText().toString(), this.organisme.getText().toString(), this.pwd.getText().toString());

    }

    public enum  Error
    {
        NONE(0),
        SERVER_ERROR(1);

        private final Integer errorCode;

        Error(Integer i) {
            errorCode = i;
        }

        public Integer getErrorCode() {
            return errorCode;
        }
    }
}