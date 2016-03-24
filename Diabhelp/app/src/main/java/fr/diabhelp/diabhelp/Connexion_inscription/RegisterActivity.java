package fr.diabhelp.diabhelp.Connexion_inscription;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import fr.diabhelp.diabhelp.API.Asynctasks.RegisterAPICallTask;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseObjects.ResponseRegister;
import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.MyToast;
import fr.diabhelp.diabhelp.R;

public class RegisterActivity extends FragmentActivity implements MainRegisterFragment.MainFragmentListener, RegisterConnexionInfosFragment.FragmentFirstStepListener, RegisterPersonalInfosFragment.FragmentSecondStepListener, IApiCallTask<ResponseRegister> {

    ProgressDialog _progress;

    DAO bdd;
    private String _login,_mail, _pwd, _firstName, _lastName, _role;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment_container, new MainRegisterFragment());
        ft.commit();

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

    @Override
    public void onRegisteNatifClick() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new RegisterPageViewerFragment());
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void saveDatasFirstStep(String mail, String login, String pwd) {
        _mail = mail;
        _login = login;
        _pwd = pwd;
        launchSecondStep();
    }

    private void launchSecondStep() {
        NonSwipeableViewPager mViewPager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        mViewPager.setCurrentItem(1, true);
    }

    @Override
    public void saveDatasSecondStep(String firstname, String lastName, String role) {
        _firstName = firstname;
        _lastName = lastName;
        _role = role;
        executeBackgroundRegisterRequest();
    }

    private void executeBackgroundRegisterRequest() {
        new RegisterAPICallTask(this).execute(_login, _mail, _pwd, _role, _firstName, _lastName);
    }



    @Override
    public void onBackPressed() {
        NonSwipeableViewPager mViewPager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        if (mViewPager != null) {
            if (mViewPager.getCurrentItem() == 0) {
                super.onBackPressed();
            } else {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {

    }

    @Override
    public void onBackgroundTaskCompleted(ResponseRegister response, String action, ProgressDialog progress) {
        _progress = progress;
        RegisterActivity.Error error = response.getError();
        Integer errorCode = error.getErrorCode();
        if (errorCode != null && errorCode != 0) {
            manageError(response.getError());
        }
        else if (action.equals("createAccount")) {
            informSuccess();
        }
    }

    private void informSuccess() {
        MyToast.getInstance().displayWarningMessage(getString(R.string.register_success), Toast.LENGTH_LONG, this);
        Log.i(getLocalClassName(), "le compte a été créé");
        returnConnexionActivity();
    }

    private void returnConnexionActivity() {
        _progress.dismiss();
        finish();
    }

    private void manageError(Error error) {
        _progress.dismiss();
        switch (error)
        {
            case LOGIN_ALREADY_USED:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_login_already_exist), Toast.LENGTH_LONG, this);
                Log.e(getLocalClassName(), "le login est déja utilisé");
                break;
            }
            case EMAIL_ALREADY_USED:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_email_already_exist), Toast.LENGTH_LONG, this);
                Log.e(getLocalClassName(), "l'email est déja utilisé");
                break;
            }
            case SERVER_ERROR:{
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, this);
                Log.e(getLocalClassName(), "Problème survenu lors de la connexion au serveur");
                break;
            }
            case DB_ERROR:{
                Log.e(getLocalClassName(), "Une erreur est survenue lors de l'ajout de données sur la db du serveur");
            }
        }
        getSupportFragmentManager().popBackStackImmediate();
        FragmentTransaction ft =getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, new MainRegisterFragment());
        ft.commit();

    }

    public enum  Error
    {
        NONE(0),
        LOGIN_ALREADY_USED(1),
        EMAIL_ALREADY_USED(2),
        DB_ERROR(3),
        SERVER_ERROR(4);

        private Integer errorCode;

        Error(Integer i) {
            this.errorCode = i;
        }

        public Integer getErrorCode() {
                return this.errorCode;
        }
    }
}
