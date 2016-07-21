package fr.diabhelp.diabhelp.Menu;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
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
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.DateUtils;
import fr.diabhelp.diabhelp.Utils.MyToast;

/**
 * Created by naqued on 21/04/16.
 */

public class ProfileActivity extends AppCompatActivity implements IApiCallTask
{

    ProgressDialog _progress;
    public static SharedPreferences _settings;

    private EditText nom, prenom, email, dateNaissance, telephone, organisme, pwd, cpwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        nom = (EditText) findViewById(R.id.editName);
        prenom = (EditText) findViewById(R.id.editLastName);
        email = (EditText) findViewById(R.id.editEmail);
        dateNaissance = (EditText) findViewById(R.id.editBirthDate);
        telephone = (EditText) findViewById(R.id.editPhone);
        organisme = (EditText) findViewById(R.id.editOrganism);
        pwd = (EditText) findViewById(R.id.editPassword);
        cpwd = (EditText) findViewById(R.id.editPassword2);

        dateNaissance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog mDatePicker;

                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                mDatePicker = new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        c.set(year, month, day);
                        Date d = c.getTime();
                        SimpleDateFormat sf = new SimpleDateFormat(DateUtils.DATE_PATERN);
                        String dateStr = sf.format(d);
                        dateNaissance.setText(dateStr);
                    }
                },mYear, mMonth, mDay);
                mDatePicker.show();
            }
        });

        _settings = getSharedPreferences(ConnexionActivity.PREF_FILE, Context.MODE_WORLD_READABLE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profil");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Carnetintent = new Intent(ProfileActivity.this, CoreActivity.class);
                startActivity(Carnetintent);
                finish();
            }
        });
        new ProfilGetAPICallTask(this, this).execute(_settings.getString(ConnexionActivity.ID_USER, ""));
/*        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }



    @Override
    public void onBackPressed()
    {
        finish();

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
        _progress = progress;
        if (action.equals("getInfo"))
        {
            ResponseProfilGet response = (ResponseProfilGet) bodyResponse;
            ProfileActivity.Error error = response.getError();
            if (error != ProfileActivity.Error.NONE)
                manageError(error);
            else
            {
                System.out.println("PAS D ERREUR LORS DU CHARGEMENT DU PROFIL");
                setFields(response);
                _progress.dismiss();
            }
        }
        if (action.equals("setInfo"))
        {
            ResponseProfilSet response = (ResponseProfilSet) bodyResponse;
            ProfileActivity.Error error = response.getError();
            if (error != ProfileActivity.Error.NONE)
                manageError(error);
            else {
                System.out.println("PAS D ERREUR LORS DE L ENREGISTREMENT DU PROFIL");
                _progress.dismiss();
                MyToast.getInstance().displayWarningMessage(getString(R.string.profile_save_success), Toast.LENGTH_SHORT, this);
            }
        }
    }

    private void setFields(ResponseProfilGet response) {
        nom.setText(response.getLastname());
        prenom.setText(response.getFirstname());
        email.setText(response.getEmail());
        String birthDate = response.getBirthdate();
        if (birthDate != null && !birthDate.isEmpty() && !birthDate.equals("null"))
            dateNaissance.setText(response.getBirthdate());
        String phone = response.getMobile();
        if (phone != null && !phone.isEmpty() && !phone.equals("null"))
            telephone.setText(response.getMobile());
        String organism = response.getOrganism();
        if (organism != null && !organism.isEmpty() && !organism.equals("null"))
            organisme.setText(response.getOrganism());
    }

    private void manageError(ProfileActivity.Error error) {
        _progress.dismiss();
        switch (error)
        {
            case SERVER_ERROR:
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, this);
                Log.e("ProfileActivity", "Problème survenu lors de la connexion au serveur");
                break;
        }
    }

    public void saveProfil(View view) {
        String password = pwd.getText().toString();
        String cPassword = pwd.getText().toString();
        if (password != null && cPassword != null && !password.isEmpty() && !cPassword.isEmpty()) {
            if (password.length() < 6)
                MyToast.getInstance().displayWarningMessage(getString(R.string.profile_password_incorrect), Toast.LENGTH_LONG, this);
            else if (!password.equals(cPassword))
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_passwords_differ), Toast.LENGTH_LONG, this);
            else
                new ProfilPosrtAPICallTask(this, this).execute(_settings.getString(ConnexionActivity.ID_USER, ""), email.getText().toString(), nom.getText().toString(), prenom.getText().toString(), telephone.getText().toString(), dateNaissance.getText().toString(), organisme.getText().toString(), pwd.getText().toString());
        }
        else
            new ProfilPosrtAPICallTask(this, this).execute(_settings.getString(ConnexionActivity.ID_USER, ""), email.getText().toString(), nom.getText().toString() , prenom.getText().toString(), telephone.getText().toString(), dateNaissance.getText().toString(), organisme.getText().toString(), pwd.getText().toString());

    }

    public enum  Error
    {
        NONE(0),
        SERVER_ERROR(1);

        private final Integer errorCode;

        Error(Integer i) {
            this.errorCode = i;
        }

        public Integer getErrorCode() {
            return this.errorCode;
        }
    }
}