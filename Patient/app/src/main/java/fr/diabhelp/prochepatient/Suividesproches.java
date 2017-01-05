package fr.diabhelp.prochepatient;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import fr.diabhelp.prochepatient.ApiLinker.ApiErrors;
import fr.diabhelp.prochepatient.ApiLinker.ApiService;
import fr.diabhelp.prochepatient.ApiLinker.ResponseSearch;
import fr.diabhelp.prochepatient.ApiLinker.ResponseUser;
import fr.diabhelp.prochepatient.ApiLinker.RetrofitHelper;
import fr.diabhelp.prochepatient.Services.PositionIntentService;
import fr.diabhelp.prochepatient.Suivi.SuivisFragment;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import fr.diabhelp.prochepatient.Utils.MyToast;
import fr.diabhelp.prochepatient.Utils.SharedContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.ArrayList;

public class Suividesproches extends AppCompatActivity{
    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String ID_USER = "id_user";
    private ViewPager viewPager;
    private SharedPreferences settings;
    private String idUser;
    private User.Role role;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proche);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


//        try {
//            SharedContext.setContext(getApplicationContext().createPackageContext(
//                    "fr.diabhelp.diabhelp",
//                    Context.CONTEXT_IGNORE_SECURITY));
//
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        settings = SharedContext.getSharedContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);
//        idUser = settings.getString(ID_USER, "");

//        if (idUser.equals(""))
            idUser = "26";

        getUserInfos(idUser);
        if (getIntent().getExtras() != null)
        {
            System.out.println("Suividesproches getExtra != null");
            handleBundle(getIntent().getExtras());
        }
    }

    private void handleBundle(Bundle bundle) {
        if (bundle.containsKey("carnet"))
        {
            System.out.println("Suividesproches handle alert");
            handleGetAlert(bundle);
        }
    }

    private void handleGetAlert(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
// Add the buttons
        LayoutInflater inflater = getLayoutInflater();
        View v = inflater.inflate(R.layout.reception_alert_dialog, null);
        builder.setView(v);
        builder.setTitle("Alerte d'un proche");
        builder.setMessage(bundle.get("nom") + " a besoin d'assistance.\n  S'en occuper ?");
        ((TextView) v.findViewById(R.id.alert_message)).setText("\"" + bundle.get("message") + "\"");
        builder.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });
        builder.setNegativeButton("NON", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
// Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getUserInfos(final String idUser) {
        RetrofitHelper retrofitH = new RetrofitHelper(getApplicationContext());
        ApiService serv = retrofitH.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = serv.getUserInfo(idUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try
                {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseUser rep = new ResponseUser(JsonUtils.getObj(body));
                        String error = rep.getError();
                        if (error == null || error.isEmpty()) {
                           // role = User.Role.PATIENT;
                            role = rep.getRole();
                            initView();
                            if (role.equals(User.Role.PATIENT))
                            {
                                Intent intent = new Intent(getParent(), PositionIntentService.class);
                                intent.putExtra(PositionIntentService.ID_USER, idUser);
                                startService(intent);
                            }
                        }
                        else
                        {
                            ApiErrors apiError = ApiErrors.getFromMessage(error);
                            manageError(apiError);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ResponseSearch response = new ResponseSearch(ApiErrors.NETWORK_ERROR.getServerMessage());
                ApiErrors apiError = ApiErrors.getFromMessage(response.getError());
                manageError(apiError);
            }
        });
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitle("Suivi des proches");
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Suivis"));
        tabLayout.addTab(tabLayout.newTab().setText("Demandes"));
        tabLayout.addTab(tabLayout.newTab().setText("Recherches"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (ViewPager) findViewById(R.id.pager);
        ArrayList<Fragment> fragments = new ArrayList<>(3);
        fragments.add(new SuivisFragment());
        fragments.add(new DemandesFragment());
        fragments.add(new RechercheFragment());
        pagerAdapter = new PagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition());}

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    protected void manageError(ApiErrors error) {
        switch (error)
        {
            case NETWORK_ERROR:{
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, this);
                Log.e("RechercheFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR:{
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, this);
                Log.e("RechercheFragment", "Erreur lors du traitement de la demande");
                break;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            startActivity(new Intent(this, ParametresActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String getIdUser() {
        return this.idUser;
    }

    public User.Role getRole() { return this.role; }
}
