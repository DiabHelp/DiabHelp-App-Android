package fr.diabhelp.patient;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import fr.diabhelp.patient.ApiLinker.ApiErrors;
import fr.diabhelp.patient.ApiLinker.ApiService;
import fr.diabhelp.patient.ApiLinker.ResponseSearch;
import fr.diabhelp.patient.ApiLinker.RetrofitHelper;
import fr.diabhelp.patient.Listeners.SearchRecyclerListener;
import fr.diabhelp.patient.Utils.JsonUtils;
import fr.diabhelp.patient.Utils.ManageListStatus;
import fr.diabhelp.patient.Utils.MyToast;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 4kito on 02/08/2016.
 */
public class RechercheFragment extends Fragment implements SearchRecyclerListener {

    private SearchView searchView;
    private LinearLayout progressLayout;
    private LinearLayout errorLayout;
    private RecyclerView patientsList;
    private SearchRecyclerAdapter adapter;
    private PatientActivity parent;
    private String idUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (PatientActivity) getActivity();
        idUser = parent.getIdUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recherche, container, false);
        searchView = (SearchView) v.findViewById(R.id.search_view);
        progressLayout = (LinearLayout) v.findViewById(R.id.progress_layout);
        errorLayout = (LinearLayout) v.findViewById(R.id.error_layout);
        patientsList = (RecyclerView) v.findViewById(R.id.list_contacts_found_view);
        patientsList.setHasFixedSize(false);
        patientsList.setLayoutManager(new LinearLayoutManager(getActivity().getBaseContext()));
        adapter = new SearchRecyclerAdapter(new ArrayList<Patient>(), this);
        patientsList.setAdapter(adapter);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                errorLayout.setVisibility(View.GONE);
                adapter = (SearchRecyclerAdapter) patientsList.getAdapter();
                if (adapter != null)
                    adapter.clearData();
                System.out.println("test submited = " + query);
                if (!query.trim().isEmpty()) {
                    progressLayout.setVisibility(View.VISIBLE);
                    searchProches(query);
                }
                return (true);
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return (true);
            }
        });
        return v;
    }

    private void searchProches(String query) {
        RetrofitHelper retrofitH = new RetrofitHelper(getActivity());
        ApiService service = retrofitH.createService(RetrofitHelper.Build.PROD);
        Call<ResponseBody> call = service.searchPatient(query);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseSearch rep = new ResponseSearch(JsonUtils.getObj(body));
                        String error = rep.getError();
                        if (error == null || error.isEmpty()) {
                            progressLayout.setVisibility(View.GONE);
                            displayEntries(rep);
                        }
                        else
                        {
                            ApiErrors apiError = ApiErrors.getFromMessage(error);
                            manageError(apiError);
                        }
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


            private void displayEntries(ResponseSearch rep) {
                if (adapter == null)
                    adapter = new SearchRecyclerAdapter(rep.getPatients(), (SearchRecyclerListener) getActivity());
                else
                    adapter.setPatientsList(rep.getPatients());
            }
        });
    }


    protected void manageError(ApiErrors error) {
        progressLayout.setVisibility(View.GONE);
        switch (error)
        {
            case NO_USERS_FOUND:{
                errorLayout.setVisibility(View.VISIBLE);
                break;
            }
            case NO_PATIENT_FOUND:{
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, getActivity());
                Log.e("RechercheFragment", "Erreur lors du traitement de la demande");
                break;
            }
            case NETWORK_ERROR:{
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, getActivity());
                Log.e("RechercheFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR:{
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, getActivity());
                Log.e("RechercheFragment", "Erreur lors du traitement de la demande");
                break;
            }

        }
    }

    public void sendDemande(String idPatient)
    {
        System.out.println("je vais send une demande");
        RetrofitHelper retrofitH = new RetrofitHelper(parent);
        ApiService service = retrofitH.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.sendDemande(idUser, idPatient, ManageListStatus.WAITING.ordinal());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONObject obj = JsonUtils.getObj(body);
                        if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true)
                            displaySuccessSendDemande();
                        else {
                                ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                                manageError(apiError);
                            }
                    } else {
                        String error = response.errorBody().string();
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
        Snackbar snack = Snackbar.make(parent.findViewById(R.id.search_root), "Demande envoyée !",Snackbar.LENGTH_SHORT);
        Snackbar.SnackbarLayout snackView =  (Snackbar.SnackbarLayout) snack.getView();
        snackView.setBackgroundColor(Color.parseColor("#b410c83e"));
        TextView tv = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        snack.show();
        adapter.clearData();
    }

    @Override
    public void onClickAddPatient(int position) {
        Patient patient = adapter.getPatientsList().get(position);
        sendDemande(patient.getId());

    }
}
