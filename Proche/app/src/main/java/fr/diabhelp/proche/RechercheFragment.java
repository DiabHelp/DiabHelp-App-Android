package fr.diabhelp.proche;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import fr.diabhelp.proche.ApiLinker.ApiErrors;
import fr.diabhelp.proche.ApiLinker.ApiService;
import fr.diabhelp.proche.ApiLinker.ResponseSearch;
import fr.diabhelp.proche.ApiLinker.RetrofitHelper;
import fr.diabhelp.proche.Utils.JsonUtils;
import fr.diabhelp.proche.Utils.MyToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 4kito on 02/08/2016.
 */
public class RechercheFragment extends Fragment {

    private SearchView searchView;
    private LinearLayout progressLayout;
    private LinearLayout errorLayout;
    private RecyclerView patientsList;
    private SearchRecyclerAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        adapter = new SearchRecyclerAdapter(new ArrayList<Patient>(), (SearchRecyclerListerner) getActivity());
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
        });
    }

    private void displayEntries(ResponseSearch rep) {
        if (adapter == null)
            adapter = new SearchRecyclerAdapter(rep.getPatients(), (SearchRecyclerListerner) getActivity());
        else
            adapter.setPatientsList(rep.getPatients());
    }

    private void manageError(ApiErrors error) {
        progressLayout.setVisibility(View.GONE);
        switch (error)
        {
            case NO_USERS_FOUND:{
                errorLayout.setVisibility(View.VISIBLE);
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



    public RecyclerView getPatientsList() {
        System.out.println("patientslist = " + patientsList);
        return (patientsList);
    }
}
