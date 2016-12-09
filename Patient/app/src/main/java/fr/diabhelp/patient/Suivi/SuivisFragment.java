package fr.diabhelp.patient.Suivi;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import fr.diabhelp.patient.ApiLinker.ApiErrors;
import fr.diabhelp.patient.ApiLinker.ApiService;
import fr.diabhelp.patient.ApiLinker.ResponseSuivi;
import fr.diabhelp.patient.ApiLinker.RetrofitHelper;
import fr.diabhelp.patient.Patient;
import fr.diabhelp.patient.PatientActivity;
import fr.diabhelp.patient.R;
import fr.diabhelp.patient.Utils.JsonUtils;
import fr.diabhelp.patient.Utils.MyToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 4kito on 02/08/2016.
 */
public class SuivisFragment extends Fragment {
    PatientActivity                                 _parent;
    String                                          _idUser;
    private RecyclerView                            _recyclerView;
    private RecyclerView.LayoutManager              _recLayoutManager;
    private ArrayList<ExpandableListAdapter.Item>   _patientsItems = new ArrayList<>();
    private ExpandableListAdapter                   _recAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _parent = (PatientActivity) getActivity();
        _idUser = _parent.getIdUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_suivis, container, false);
        _recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    _patientsItems = getPatientsItems();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                swipeRefreshLayout.setRefreshing(false);
                _recyclerView.setAdapter(new ExpandableListAdapter(_patientsItems));
            }
        });
        try {
            _patientsItems = getPatientsItems();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _recyclerView.setAdapter(new ExpandableListAdapter(_patientsItems));
        return v;
    }

    public ArrayList<ExpandableListAdapter.Item> getPatientsItems() throws Exception {
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.getPendingRequests(_idUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseSuivi rep = new ResponseSuivi(JsonUtils.getObj(body));
                        displayEntries(rep);
                    } else {
                        String error = response.errorBody().string();
                        System.out.println("erreur message = " + error);
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
        return _patientsItems;
    }

    private void displayEntries(ResponseSuivi rep) {
        if (_recAdapter == null) {
            _recAdapter = new ExpandableListAdapter(rep.patientsList);
        } else {
            _recAdapter._data = rep.patientsList;

        }
        _recyclerView.setAdapter(_recAdapter);
    }

    protected void manageError(ApiErrors error) {
        switch (error) {
            case NETWORK_ERROR: {
                MyToast.getInstance().displayWarningMessage("Impossible de se connecter au serveur, veuillez vérifier votre connexion ou réessayer plus tard", Toast.LENGTH_LONG, _parent);
                Log.e("RechercheFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR: {
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, _parent);
                Log.e("RechercheFragment", "Erreur lors du traitement de la demande");
                break;
            }
        }
    }
}

