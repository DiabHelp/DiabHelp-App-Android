package fr.diabhelp.proche;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import fr.diabhelp.proche.ApiLinker.ApiErrors;
import fr.diabhelp.proche.ApiLinker.ApiService;
import fr.diabhelp.proche.ApiLinker.ResponseSearch;
import fr.diabhelp.proche.ApiLinker.RetrofitHelper;
import fr.diabhelp.proche.Utils.MyToast;
import fr.diabhelp.proche.Utils.SharedContext;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Proche extends AppCompatActivity implements SearchRecyclerListerner {
    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String ID_USER = "id_user";
    private ViewPager viewPager;
    private SharedPreferences settings;
    private String idUser;
    fr.diabhelp.proche.PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proche);
        try {
            SharedContext.setContext(getApplicationContext().createPackageContext(
                    "fr.diabhelp.diabhelp",
                    Context.MODE_PRIVATE));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        settings = SharedContext.getSharedContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        idUser = settings.getString(ID_USER, "");
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
        pagerAdapter = new fr.diabhelp.proche.PagerAdapter(getSupportFragmentManager(), fragments);
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

    public void sendDemande(String idPatient)
    {
        System.out.println("je vais send une demande");
        RetrofitHelper retrofitH = new RetrofitHelper(this);
        ApiService service = retrofitH.createService(RetrofitHelper.Build.PROD);
        Call<ResponseBody> call = service.sendDemande(idUser, idPatient, Status.EN_COURS.ordinal());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        displaySuccessSendDemande();
                    } else {
                        String error = response.errorBody().string();
                        System.out.println("erreur message = " + error);
                        ApiErrors apiError = ApiErrors.getFromMessage(error);
                        manageError(apiError);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                    ApiErrors api = ApiErrors.NETWORK_ERROR;
                    manageError(api);
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

    private void displaySuccessSendDemande()
    {
        Snackbar snack = Snackbar.make(findViewById(R.id.search_root), "Demande envoyée !",Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackView =  (Snackbar.SnackbarLayout) snack.getView();
        snackView.setBackgroundColor(Color.parseColor("#b410c83e"));
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
        RechercheFragment frag = (RechercheFragment)pagerAdapter.getItem(2);
        RecyclerView patientsList = frag.getPatientsList();
        SearchRecyclerAdapter searchAdapter = (SearchRecyclerAdapter) patientsList.getAdapter();
        searchAdapter.clearData();
    }

    private void manageError(ApiErrors error) {
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
    public void onClickAddPatient(int position) {
        pagerAdapter = (fr.diabhelp.proche.PagerAdapter)viewPager.getAdapter();
        RechercheFragment frag = (RechercheFragment)pagerAdapter.getItem(2);
        RecyclerView patientsList = frag.getPatientsList();
        SearchRecyclerAdapter searchAdapter = (SearchRecyclerAdapter) patientsList.getAdapter();
        Patient patient = searchAdapter.getPatientsList().get(position);
        sendDemande(patient.getId());

    }

    public enum Status {
        EN_COURS,
        ACCEPTED,
        REJECTED,
        DELETED
    }
}
