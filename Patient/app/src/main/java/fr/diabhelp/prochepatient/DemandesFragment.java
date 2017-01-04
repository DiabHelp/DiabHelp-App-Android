package fr.diabhelp.prochepatient;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import fr.diabhelp.prochepatient.ApiLinker.ApiErrors;
import fr.diabhelp.prochepatient.ApiLinker.ApiService;
import fr.diabhelp.prochepatient.ApiLinker.ResponseRequest;
import fr.diabhelp.prochepatient.ApiLinker.RetrofitHelper;
import fr.diabhelp.prochepatient.Listeners.DemandeRecyclerListener;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import fr.diabhelp.prochepatient.Utils.ManageListStatus;
import fr.diabhelp.prochepatient.Utils.MyToast;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by 4kito on 02/08/2016.
 */
public class DemandesFragment extends Fragment implements DemandeRecyclerListener {
    private final int REQUEST = 0;
    private final int ACCEPTED = 1;
    private final int REFUSED = 2;
    private final int WAITING = 3;
    private ArrayList<PatientRequest> requestsList = new ArrayList<>();
    private RecyclerView requestRecyclerView;
    private PatientRequestRecyclerAdapter requestRecyclerAdapter;
    private LinearLayoutManager requestRecLayoutManager;
    private LinearLayout errorLayout;

    String APIToken;
    String idUser;
    User.Role role;

    Suividesproches parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (Suividesproches) getActivity();
        idUser = parent.getIdUser();
        role = parent.getRole();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_demandes, container, false);

        // Création de la liste des requêtes
        requestRecyclerView = (RecyclerView) v.findViewById(R.id.patient_request_recycler_view);
        requestRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        requestRecyclerView.setLayoutManager(requestRecLayoutManager);
        errorLayout = (LinearLayout) v.findViewById(R.id.error_layout);
        requestRecyclerAdapter = new PatientRequestRecyclerAdapter(APIToken, this, new ArrayList<PatientRequest>());
        requestRecyclerView.setAdapter(requestRecyclerAdapter);
        try {
            getRequestsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    public void getRequestsList() throws Exception {

        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = null;
        if (role.equals(User.Role.PATIENT) == true)
            call = service.getProchePendingRequests(idUser);
        else if (role.equals(User.Role.PROCHE) == true)
            call = service.getPatientPendingRequests(idUser);
        if (call != null)
        {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            ResponseRequest rep = new ResponseRequest(JsonUtils.getObj(body));
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
        }
    }

    private void displayEntries(ResponseRequest rep) {
        if (requestRecyclerAdapter == null)
            requestRecyclerAdapter = new PatientRequestRecyclerAdapter(null, this, rep.getRequests());
        else
            requestRecyclerAdapter.setRequestsList(rep.getRequests());
    }

    //TODO Handle le choix d'acceptation d'un contact

    @Override
    public void onClickAcceptPatient(final PatientRequest request, final int position, final PatientRequestRecyclerAdapter.PatientRequestHolder holder) {
        System.out.println("on handle l'évènement d'acceptation dans le fragment");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = null;
        if (role.equals(User.Role.PATIENT) == true)
            call = service.acceptProcheRequest(request.getId(), idUser,  ManageListStatus.ACCEPTED.ordinal());
        else if (role.equals(User.Role.PROCHE) == true)
            call = service.acceptPatientRequest(request.getId(), idUser,  ManageListStatus.ACCEPTED.ordinal());
        if (call != null)
        {
            System.out.println("call != null");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            System.out.println("it's successfull");
                            String body = response.body().string();
                            System.out.println("body = " + body);
                            JSONObject obj = JsonUtils.getObj(body);
                            if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true) {
                                holder.changeRequestState(PatientRequest.State.ACCEPTED);
                                requestRecyclerAdapter.notifyItemChanged(position);

                                // Ce block de code permet d'attendre un petit moment avant de supprimer la demande du patient
                                // Ca permet d'avoir le temps d'afficher l'animation "Refusé"
                                // Il faut absolument pas que 2 requetes puissent passer en meme temps, sinon la premiere risque de passer ici et la seconde dans manageApiError
                                // En gros si y'a du caca c'est surement d'ici que ca vient
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestRecyclerAdapter.getRequestList().remove(request);
                                        requestRecyclerAdapter.notifyItemRemoved(position);
                                        requestRecyclerAdapter.notifyItemRangeChanged(position, requestRecyclerAdapter.getItemCount());
                                    }
                                }, 2000);
                            }
                            else {
                                ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                                holder.request.setState(PatientRequest.State.WAITING);
                                requestRecyclerAdapter.notifyItemChanged(position);
                                manageError(apiError);
                            }
                        } else {
                            System.out.println("it's error");
                            String error = response.errorBody().string();
                            System.out.println("error = " + error);
                            System.out.println("erreur message = " + error);
                            ApiErrors apiError = ApiErrors.getFromMessage(error);
                            holder.request.setState(PatientRequest.State.WAITING);
                            requestRecyclerAdapter.notifyItemChanged(position);
                            manageError(apiError);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        ApiErrors api = ApiErrors.NETWORK_ERROR;
                        holder.request.setState(PatientRequest.State.WAITING);
                        requestRecyclerAdapter.notifyItemChanged(position);
                        manageError(api);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    System.out.println("it's fail");
                    ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                    holder.request.setState(PatientRequest.State.WAITING);
                    requestRecyclerAdapter.notifyItemChanged(position);
                    manageError(apiError);
                }
            });
        }
        else
            System.out.println("call == null !!!!!!!!");
    }

    @Override
    public void onClickDenyPatient(final PatientRequest request, final int position, final PatientRequestRecyclerAdapter.PatientRequestHolder holder) {
        System.out.println("on handle l'évènement de refus dans le fragment");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = null;
        if (role.equals(User.Role.PATIENT) == true)
            call = service.denyProcheRequest(request.getId(), idUser, ManageListStatus.REJECTED.ordinal());
        if (role.equals(User.Role.PROCHE) == true)
            call = service.denyPatientRequest(request.getId(), idUser, ManageListStatus.REJECTED.ordinal());
        if (call != null)
        {
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        System.out.println("on response");
                        if (response.isSuccessful()) {
                            String body = response.body().string();
                            System.out.println("it's successful");
                            JSONObject obj = JsonUtils.getObj(body);
                            if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true) {
                                holder.changeRequestState(PatientRequest.State.REFUSED);
                                requestRecyclerAdapter.notifyItemChanged(position);

                                // Ce block de code permet d'attendre un petit moment avant de supprimer la demande du patient
                                // Ca permet d'avoir le temps d'afficher l'animation "Refusé"
                                // Il faut absolument pas que 2 requetes puissent passer en meme temps, sinon la premiere risque de passer ici et la seconde dans manageApiError
                                // En gros si y'a du caca c'est surement d'ici que ca vient
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        requestRecyclerAdapter.getRequestList().remove(request);
                                        requestRecyclerAdapter.notifyItemRemoved(position);
                                        requestRecyclerAdapter.notifyItemRangeChanged(position, requestRecyclerAdapter.getItemCount());
                                    }
                                }, 2000);

                            }
                            else {
                                System.out.println("error");
                                ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                                holder.request.setState(PatientRequest.State.WAITING);
                                requestRecyclerAdapter.notifyItemChanged(position);
                                manageError(apiError);
                            }
                        } else {
                            String error = response.errorBody().string();
                            System.out.println("erreur message = " + error);
                            ApiErrors apiError = ApiErrors.getFromMessage(error);
                            holder.request.setState(PatientRequest.State.WAITING);
                            requestRecyclerAdapter.notifyItemChanged(position);
                            manageError(apiError);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        ApiErrors api = ApiErrors.NETWORK_ERROR;
                        holder.request.setState(PatientRequest.State.WAITING);
                        requestRecyclerAdapter.notifyItemChanged(position);
                        manageError(api);
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                    holder.request.setState(PatientRequest.State.WAITING);
                    requestRecyclerAdapter.notifyItemChanged(position);
                    manageError(apiError);
                }
            });
        }
    }

    protected void manageError(ApiErrors error) {
        //progressLayout.setVisibility(View.GONE);
        switch (error) {
//            case NO_USERS_FOUND: {
//                errorLayout.setVisibility(View.VISIBLE);
//                break;
//            }
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
}
