package fr.diabhelp.diabhelp.Menu;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.diabhelp.diabhelp.API.Asynctasks.ProfilGetAPICallTask;
import fr.diabhelp.diabhelp.API.Asynctasks.ProfilPostAPICallTask;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilGet;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilSet;
import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.R.drawable;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;
import fr.diabhelp.diabhelp.R.string;
import fr.diabhelp.diabhelp.Utils.DateUtils;
import fr.diabhelp.diabhelp.Utils.MyToast;
import fr.diabhelp.diabhelp.Utils.StringUtils;

/**
 * Created by naqued on 21/04/16.
 */

public class ProfileActivity extends AppCompatActivity implements IApiCallTask
{

    ProgressDialog _progress;
    public static SharedPreferences _settings;

    private EditText nomView, prenomView, emailView, dateNaissanceView, telephoneView, organismeView, pwdView, pwdCView;
    private String mail, prenom, nom, organisme, tel, dateNaissance, pwd, pwdC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_profile);
        this.nomView = (EditText) this.findViewById(id.edit_last_name);
        this.prenomView = (EditText) this.findViewById(id.edit_first_name);
        this.emailView = (EditText) this.findViewById(id.editEmail);
        this.dateNaissanceView = (EditText) this.findViewById(id.editBirthDate);
        this.telephoneView = (EditText) this.findViewById(id.editPhone);
        this.organismeView = (EditText) this.findViewById(id.editOrganism);
        this.pwdView = (EditText) this.findViewById(id.editPassword);
        this.pwdCView = (EditText) this.findViewById(id.editPassword2);

        this.dateNaissanceView.setOnClickListener(new OnClickListener() {
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
                        SimpleDateFormat sf = new SimpleDateFormat(DateUtils.DATE_PICKER_PATERN);
                        String dateStr = sf.format(d);
                        ProfileActivity.this.dateNaissanceView.setText(dateStr);
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



    @Override
    public void onBackPressed()
    {
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
            if (error != Error.NONE)
                this.manageError(error);
            else
            {
                this.setFields(response);
                this._progress.dismiss();
            }
        }
        if (action.equals("setInfo"))
        {
            ResponseProfilSet response = (ResponseProfilSet) bodyResponse;
            Error error = response.getError();
            if (error != Error.NONE)
                this.manageError(error);
            else {
                this._progress.dismiss();
                MyToast.getInstance().displayWarningMessage(this.getString(string.profile_save_success), Toast.LENGTH_SHORT, this);
            }
        }
    }

    private void setFields(ResponseProfilGet response) {
        this.nomView.setText(response.getLastname());
        this.prenomView.setText(response.getFirstname());
        this.emailView.setText(response.getEmail());
        String birthDate = response.getBirthdate();
        if (birthDate != null && !birthDate.isEmpty() && !birthDate.equals("null"))
            this.dateNaissanceView.setText(response.getBirthdate());
        String phone = response.getMobile();
        if (phone != null && !phone.isEmpty() && !phone.equals("null"))
            this.telephoneView.setText(response.getMobile());
        String organism = response.getOrganism();
        if (organism != null && !organism.isEmpty() && !organism.equals("null"))
            this.organismeView.setText(response.getOrganism());
    }

    private void manageError(Error error) {
        this._progress.dismiss();
        switch (error)
        {
            case SERVER_ERROR:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_server), Toast.LENGTH_LONG, this);
                Log.e("ProfileActivity", "Problème survenu lors de la connexion au serveur");
                break;
            case PRENOM_TO_SHORT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_firstname_too_short), Toast.LENGTH_LONG, this);
                prenomView.requestFocus();
                break;
            case NOM_TO_SHORT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_lastname_too_short), Toast.LENGTH_LONG, this);
                nomView.requestFocus();
                break;
            case DATE_WRONG_FORMAT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_datenaissance_wrong_format), Toast.LENGTH_LONG, this);
                break;
            case MAIL_EMPTY:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_mail_empty), Toast.LENGTH_LONG, this);
                break;
            case MAIL_WRONG_FORMAT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_mail_wrong_format), Toast.LENGTH_LONG, this);
                break;
            case TEL_WRONG_FORMAT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_tel_wrong_format), Toast.LENGTH_LONG, this);
                break;
            case PASSWORD_TOO_SHORT:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_password_incorrect), Toast.LENGTH_LONG, this);
                break;
            case PASSWORDS_DIFFER:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_passwords_differ), Toast.LENGTH_LONG, this);
                break;
        }
    }

    public void saveProfil(View view) {
        this.mail = this.emailView.getText().toString();
        this.prenom = this.prenomView.getText().toString();
        this.nom = this.nomView.getText().toString();
        this.organisme = this.organismeView.getText().toString();
        this.tel = this.telephoneView.getText().toString();
        this.dateNaissance = this.dateNaissanceView.getText().toString();
        this.pwd = this.pwdView.getText().toString();
        this.pwdC = this.pwdCView.getText().toString();
        ProfileActivity.Error error = this.checkFields();
        if (error != ProfileActivity.Error.NONE)
            this.manageError(error);
        else
        {
            SimpleDateFormat sf = new SimpleDateFormat(DateUtils.DATE_PICKER_PATERN);
            try {
                Date date = sf.parse(this.dateNaissance);
                this.dateNaissance = String.valueOf(date.getTime() / 1000);
                System.out.println("date naissance to send = " + this.dateNaissance );
                new ProfilPostAPICallTask(this, this).execute(ProfileActivity._settings.getString(ConnexionActivity.ID_USER, ""), this.mail, this.nom, this.prenom, this.tel, this.dateNaissance, this.organisme, this.pwd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public ProfileActivity.Error checkFields()
    {
        ProfileActivity.Error error = ProfileActivity.Error.NONE;
        if (this.mail == null || this.mail.isEmpty())
            error = ProfileActivity.Error.MAIL_EMPTY;
        else if (!StringUtils.isEmailValid(this.mail))
            error = ProfileActivity.Error.MAIL_WRONG_FORMAT;
        else if (!StringUtils.hasFieldGoodFormat(this.prenom, StringUtils.MIN_LENGTH, 3))
            error = ProfileActivity.Error.PRENOM_TO_SHORT;
        else if (!StringUtils.hasFieldGoodFormat(this.nom, StringUtils.MIN_LENGTH, 3))
            error = ProfileActivity.Error.NOM_TO_SHORT;
        else if (!StringUtils.hasFieldGoodFormat(this.dateNaissance, StringUtils.MATCH_LENGTH, 10))
            error = ProfileActivity.Error.DATE_WRONG_FORMAT;
        else if (this.tel != null && !this.tel.isEmpty() &&
                (!StringUtils.hasFieldGoodFormat(this.tel, StringUtils.MATCH_LENGTH, 10) || !(this.tel.length() > 0 && this.tel.charAt(0) == '0')))
                error = ProfileActivity.Error.TEL_WRONG_FORMAT;
        else if (pwd != null && !pwd.isEmpty())
        {
            if (!StringUtils.hasFieldGoodFormat(this.pwd, StringUtils.MIN_LENGTH, 6)) {
                error = ProfileActivity.Error.PASSWORD_TOO_SHORT;
            }
            else if (!this.pwd.equals(this.pwdC))
                error = ProfileActivity.Error.PASSWORDS_DIFFER;
        }
        return error;
    }

    public enum  Error
    {
        NONE,
        SERVER_ERROR,
        MAIL_EMPTY,
        MAIL_WRONG_FORMAT,
        PRENOM_TO_SHORT,
        NOM_TO_SHORT,
        TEL_WRONG_FORMAT,
        DATE_WRONG_FORMAT,
        PASSWORD_TOO_SHORT,
        PASSWORDS_DIFFER

    }
}