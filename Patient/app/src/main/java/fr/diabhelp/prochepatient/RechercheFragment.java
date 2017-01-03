package fr.diabhelp.prochepatient;

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

import fr.diabhelp.prochepatient.ApiLinker.ApiErrors;
import fr.diabhelp.prochepatient.ApiLinker.ApiService;
import fr.diabhelp.prochepatient.ApiLinker.ResponseSearch;
import fr.diabhelp.prochepatient.ApiLinker.RetrofitHelper;
import fr.diabhelp.prochepatient.Listeners.SearchRecyclerListener;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import fr.diabhelp.prochepatient.Utils.ManageListStatus;
import fr.diabhelp.prochepatient.Utils.MyToast;
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
    private ProchePatientActivity parent;
    private String idUser;
    private User.Role role;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (ProchePatientActivity) getActivity();
        idUser = parent.getIdUser();
        role = parent.getRole();
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
        adapter = new SearchRecyclerAdapter(new ArrayList<User>(), role, this);
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
                    searchUsers(query);
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

    private void searchUsers(String query) {
        RetrofitHelper retrofitH = new RetrofitHelper(getActivity());
        ApiService service = retrofitH.createService(RetrofitHelper.Build.PROD);
        Call<ResponseBody> call = null;
        System.out.println("idUserBeforeRecherche = " + this.idUser);
        System.out.println("roleBeforeRecherche = " + this.role);
        if (role.equals(User.Role.PATIENT) == true)
            call = service.searchProche(this.idUser, query);
        else if (role.equals(User.Role.PROCHE) == true)
            call = service.searchPatient(this.idUser, query);
        if (call != null)
        {
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
                        adapter = new SearchRecyclerAdapter(rep.getUsers(), role, (SearchRecyclerListener) getActivity());
                    else
                        adapter.setPatientsList(rep.getUsers());
                }
            });
        }
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

    public void sendDemande(String idUser)
    {
        System.out.println("je vais send une demande");
        RetrofitHelper retrofitH = new RetrofitHelper(parent);
        ApiService service = retrofitH.createService(RetrofitHelper.Build.PROD);
        Call<ResponseBody> call = null;
        if (role.equals(User.Role.PATIENT) == true)
            call = service.sendDemandeToProche(this.idUser, idUser, ManageListStatus.WAITING.ordinal());
        else if (role.equals(User.Role.PROCHE) == true)
            call = service.sendDemandeToPatient(this.idUser, idUser, ManageListStatus.WAITING.ordinal());
        if (call != null)
        {
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
        User user = adapter.getPatientsList().get(position);
        sendDemande(user.getId());

    }
}
