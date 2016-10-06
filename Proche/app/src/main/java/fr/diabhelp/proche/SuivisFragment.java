package fr.diabhelp.proche;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import fr.diabhelp.proche.ApiLinker.ApiErrors;
import fr.diabhelp.proche.ApiLinker.ApiService;
import fr.diabhelp.proche.ApiLinker.ResponseList;
import fr.diabhelp.proche.ApiLinker.ResponseSearch;
import fr.diabhelp.proche.ApiLinker.RetrofitHelper;
import fr.diabhelp.proche.Listeners.SearchRecyclerListener;
import fr.diabhelp.proche.Utils.JsonUtils;
import fr.diabhelp.proche.Utils.MyToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 4kito on 02/08/2016.
 */
public class SuivisFragment extends Fragment {
    private RecyclerView recyclerView;
    private PatientRecyclerAdapter recAdapter;
    private RecyclerView.LayoutManager recLayoutManager;
    private ArrayList<Patient> listPatients = new ArrayList<>();
    private LinearLayout progressLayout;
    private String idUser;
    private Proche parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (Proche) getActivity();
        idUser = parent.getIdUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_suivis, container, false);
        progressLayout = (LinearLayout) v.findViewById(R.id.progress_layout);
        progressLayout.setVisibility(View.VISIBLE);
        recyclerView = (RecyclerView) v.findViewById(R.id.patient_suivi_recycler_view);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        try {
            getPatientsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        recAdapter = new PatientRecyclerAdapter(listPatients, getContext());
        recyclerView.setAdapter(recAdapter);
        return v;
    }


    public void getPatientsList() throws Exception {

        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.getAllPatients(idUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseList rep = new ResponseList(JsonUtils.getObj(body));
                        String error = rep.getError();
                        if (error == null || error.isEmpty()){
                            listPatients = rep.getPatientsList();
                            displayEntries();
                        }
                        else
                        {
                            ApiErrors apiError = ApiErrors.getFromMessage(error);
                            manageError(apiError);
                        }
                    } else {
                        String error = response.errorBody().string();
                        ApiErrors apiError = ApiErrors.getFromMessage(error);
                        manageError(apiError);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ApiErrors api = ApiErrors.NETWORK_ERROR;
                    manageError(api);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                manageError(apiError);
            }
        });
    }

        private void displayEntries() {
            progressLayout.setVisibility(View.GONE);
            if (recAdapter == null)
                recAdapter = new PatientRecyclerAdapter(listPatients, getActivity());
            else
                recAdapter.setPatientsList(listPatients);
        }

    protected void manageError(ApiErrors error) {
        progressLayout.setVisibility(View.GONE);
        switch (error) {
            case NETWORK_ERROR: {
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, getActivity());
                Log.e("RechercheFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR: {
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, getActivity());
                Log.e("RechercheFragment", "Erreur lors du traitement de la demande");
                break;
            }

        }
    }

    public void update()
    {
        try {
            getPatientsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

//    public void getPatientsList() throws Exception {
//        Request request = new Request.Builder()
//                .url("http://www.dev.diabhelp.org/api/patient/getAllByUserId/25")
//                .build();
//
//        Response response = _client.newCall(request).execute();
//        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
//        /*
//        Headers responseHeaders = response.headers();
//        for (int i = 0; i < responseHeaders.size(); i++) {
//            Log.d("OKHTTP ", responseHeaders.name(i) + ": " + responseHeaders.value(i));
//        }
//        */
//
//        JSONObject  json = new JSONObject(response.body().string());
//        JSONArray   array = json.getJSONArray("users");
//        for (int i = 0; i < array.length(); i++) {
//            JSONObject item;
//            if ((item = array.getJSONObject(i).getJSONObject("patient")) != null) {
//                listPatients.add(new Patient(item.getString("firstname"), item.getString("lastname"), new Location(""), Patient.State.OK));
//                Log.d("JSON", item.toString());
//            }
//            //Log.d("JSON", array.getJSONObject(i).toString());
//        }
//        //Log.d("OKHTTP ", response.body().string());
//
//    }


