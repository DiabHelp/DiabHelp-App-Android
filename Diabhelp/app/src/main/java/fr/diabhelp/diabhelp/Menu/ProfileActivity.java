package fr.diabhelp.diabhelp.Menu;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;

import fr.diabhelp.diabhelp.API.Asynctasks.ProfilAPICallTask;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseCatalogue;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseProfilGet;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.Utils.MyToast;

/**
 * Created by naqued on 21/04/16.
 */

public class ProfileActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, IApiCallTask<ResponseProfilGet>
{
    private ProgressDialog              _progress;
    private EditText                    _name;
    private EditText                    _lastname;
    private EditText                    _email;
    private EditText                    _mobile;
    private EditText                    _birthdate;
    private EditText                    _organism;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
        new ProfilAPICallTask(ProfileActivity.this, this).execute();
/*        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/
    }

    public void updateProfil()
    {
/*        new ProfilAPICallTask(ProfileActivity.this, );*/
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_website) {
            String url = "http://www.diabhelp.fr";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        } else if (id == R.id.nav_help) {

        } else if (id == R.id.nav_faq) {

        } else if (id == R.id.nav_logout) {

        } else if (id == R.id.nav_facebook) {

        }
/*        else if (id == R.id.nav_twitter) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        finish();

        super.onBackPressed();
    }

    public enum  Error
    {
        NONE(0),
        SERVER_ERROR(1);

        private Integer errorCode;

        Error(Integer i) {
            this.errorCode = i;
        }

        public Integer getErrorCode() {
            return this.errorCode;
        }
    }

    public void displayInfos(ResponseProfilGet res)
    {
        _name = (EditText) findViewById(R.id.editName);
        _lastname = (EditText) findViewById(R.id.editName);
        _mobile = (EditText) findViewById(R.id.editPhone);
        _birthdate = (EditText) findViewById(R.id.editBirthDate);
        _organism = (EditText) findViewById(R.id.editOrganism);
        _email = (EditText) findViewById(R.id.editEmail);

        _name.setHint(res.getFirstname());
        _lastname.setHint(res.getLastname());
        _mobile.setHint(res.getMobile());
        _birthdate.setHint(res.getOrganism());
        _organism.setHint(res.getOrganism());
        _email.setHint(res.getEmail());

    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {}

    @Override
    public void onBackgroundTaskCompleted(ResponseProfilGet response, String action, ProgressDialog progress) {
        _progress = progress;
        ProfileActivity.Error error = response.getError();
        Integer errorCode = error.getErrorCode();
        if (errorCode != null && errorCode != 0) {
            manageError(response.getError());
        }
        else if (action.equals("getInfo")) {
            displayInfos(response);
        }
    }


    private void manageError(ProfileActivity.Error error) {
        _progress.dismiss();
        switch (error) {
            case SERVER_ERROR:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_server), Toast.LENGTH_LONG, this);
                Log.e(getClass().getSimpleName(), "Problème survenu lors de la connexion au serveur");
                break;
            }
        }
    }
}