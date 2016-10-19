package fr.diabhelp.medecin_patient;

/**
 * Created by sundava on 09/03/16.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import fr.diabhelp.medecin_patient.API.ApiErrors;
import fr.diabhelp.medecin_patient.API.ApiService;
import fr.diabhelp.medecin_patient.API.ResponseChatMessage;
import fr.diabhelp.medecin_patient.API.ResponseMedecinList;
import fr.diabhelp.medecin_patient.API.ResponseRequest;
import fr.diabhelp.medecin_patient.API.RetrofitHelper;
import fr.diabhelp.medecin_patient.Listeners.MedecinListListener;
import fr.diabhelp.medecin_patient.Listeners.MedecinRequestListener;
import fr.diabhelp.medecin_patient.Utils.JsonUtils;
import fr.diabhelp.medecin_patient.Utils.MyToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class MedecinsFragment extends Fragment implements MedecinRequestListener, MedecinListListener {

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private RecyclerView requestRecyclerView;
    private MedecinRequestRecyclerAdapter requestRecyclerAdapter;
    private LinearLayoutManager requestRecLayoutManager;
    private RecyclerView listRecyclerView;
    private MedecinListRecyclerAdapter listRecyclerAdapter;
    private LinearLayoutManager  listRecLayoutManager;
    String APIToken;
    String idUser;
    MainActivity parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (MainActivity) getActivity();
        idUser = parent.getIdUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medecins, container, false);

        // Création de la liste des requêtes
        requestRecyclerView = (RecyclerView) v.findViewById(R.id.medecins_requetes_recycler_view);
        requestRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        requestRecyclerView.setLayoutManager(requestRecLayoutManager);
        requestRecyclerAdapter = new MedecinRequestRecyclerAdapter(APIToken, requestRecyclerView, this, new ArrayList<MedecinRequest>());
        requestRecyclerView.setAdapter(requestRecyclerAdapter);
        setRecyclerViewHeight(requestRecyclerView);

        //Création de la liste des médecins
        listRecyclerView = (RecyclerView) v.findViewById(R.id.medecins_list_recycler_view);
        listRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        MedecinListItemAnimator listItemAnimator = new MedecinListItemAnimator();
        listRecyclerView.setLayoutManager(listRecLayoutManager);
        listRecyclerView.setItemAnimator(listItemAnimator);
        listRecyclerAdapter = new MedecinListRecyclerAdapter(APIToken, listRecyclerView, this, new ArrayList<MedecinInfo>());
        listRecyclerView.setAdapter(listRecyclerAdapter);
        setRecyclerViewHeight(listRecyclerView);

        try {
            getRequestsList();
            getMedecinList();
        } catch (Exception e) {
            e.printStackTrace();
        }        return v;
    }

    private void getRequestsList() {
        Log.d("MedecinFragment", "Begin getRequestsList");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.getPendingRequests(idUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseRequest rep = new ResponseRequest(JsonUtils.getObj(body));
                        displayMedecinRequests(rep);
                    } else {
                        String error = response.errorBody().string();
                        System.err.println("erreur message = " + error);
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

    private void displayMedecinRequests(ResponseRequest rep) {
        if (requestRecyclerAdapter == null)
            requestRecyclerAdapter = new MedecinRequestRecyclerAdapter(null, requestRecyclerView, this, rep.getmedecinRequests());
        else
            requestRecyclerAdapter.setMedecinRequests(rep.getmedecinRequests());
        setRecyclerViewHeight(requestRecyclerView);
    }

    private void getMedecinList() {
        Log.d("MedecinFragment", "Begin getMedecinList");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.getMedecins(idUser);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseMedecinList rep = new ResponseMedecinList(JsonUtils.getObj(body));
                        displayMedecinList(rep);
                    } else {
                        String error = response.errorBody().string();
                        System.err.println("erreur message = " + error);
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

    private void displayMedecinList(ResponseMedecinList rep) {
        if (listRecyclerAdapter == null)
            listRecyclerAdapter = new MedecinListRecyclerAdapter(null, listRecyclerView, this, rep.getMedecinInfos());
        else
            listRecyclerAdapter.setMedecinList(rep.getMedecinInfos());
        setRecyclerViewHeight(listRecyclerView);
    }

    public void setRecyclerViewHeight(RecyclerView view)
    {
        int itemCount = view.getAdapter().getItemCount();
        int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70 * itemCount, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = viewHeight;
        view.setLayoutParams(params);
        Log.d("MedecinView", "viewHeight = " + viewHeight);

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
                Log.e("MedecinFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR: {
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, getActivity());
                Log.e("MedecinFragment", "Erreur lors du traitement de la demande");
                break;
            }

        }
    }

    @Override
    public void onClickAcceptRequest(MedecinRequest request, final int position, final MedecinRequestRecyclerAdapter.MedecinRequestHolder holder) {
        Log.e("MedecinFragment", "onClickAcceptRequest");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.acceptRequest(idUser, request.getId(), MedecinRequest.State.ACCEPTED.ordinal());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONObject obj = JsonUtils.getObj(body);
                        if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true) {
                            holder.changeRequestState(MedecinRequest.State.ACCEPTED, position);

                            // Ce block de code permet d'attendre un petit moment avant de supprimer la demande du patient
                            // Ca permet d'avoir le temps d'afficher l'animation "Refusé"
                            // Il faut absolument pas que 2 requetes puissent passer en meme temps, sinon la premiere risque de passer ici et la seconde dans manageApiError
                            // En gros si y'a du caca c'est surement d'ici que ca vient
                            holder.removeItem(position);
                        }
                        else {
                            ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                            holder.request.setState(MedecinRequest.State.WAITING);
                            requestRecyclerAdapter.notifyItemChanged(position);
                            manageError(apiError);
                        }
                    } else {
                        String error = response.errorBody().string();
                        System.out.println("erreur message = " + error);
                        ApiErrors apiError = ApiErrors.getFromMessage(error);
                        holder.request.setState(MedecinRequest.State.WAITING);
                        requestRecyclerAdapter.notifyItemChanged(position);
                        manageError(apiError);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ApiErrors api = ApiErrors.NETWORK_ERROR;
                    holder.request.setState(MedecinRequest.State.WAITING);
                    requestRecyclerAdapter.notifyItemChanged(position);
                    manageError(api);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                holder.request.setState(MedecinRequest.State.WAITING);
                requestRecyclerAdapter.notifyItemChanged(position);
                manageError(apiError);
            }
        });
    }

    @Override
    public void onClickDenyRequest(MedecinRequest request, final int position, final MedecinRequestRecyclerAdapter.MedecinRequestHolder holder) {
        Log.e("MedecinFragment", "onClickDenyRequest");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.acceptRequest(idUser, request.getId(), MedecinRequest.State.REFUSED.ordinal());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONObject obj = JsonUtils.getObj(body);
                        if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true) {
                            holder.changeRequestState(MedecinRequest.State.REFUSED, position);

                            holder.removeItem(position);
                        }
                        else {
                            ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                            holder.request.setState(MedecinRequest.State.WAITING);
                            requestRecyclerAdapter.notifyItemChanged(position);
                            manageError(apiError);
                        }
                    } else {
                        String error = response.errorBody().string();
                        System.out.println("erreur message = " + error);
                        ApiErrors apiError = ApiErrors.getFromMessage(error);
                        holder.request.setState(MedecinRequest.State.WAITING);
                        requestRecyclerAdapter.notifyItemChanged(position);
                        manageError(apiError);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ApiErrors api = ApiErrors.NETWORK_ERROR;
                    holder.request.setState(MedecinRequest.State.WAITING);
                    requestRecyclerAdapter.notifyItemChanged(position);
                    manageError(api);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                holder.request.setState(MedecinRequest.State.WAITING);
                requestRecyclerAdapter.notifyItemChanged(position);
                manageError(apiError);
            }
        });
    }

    @Override
    public void onDeleteMedecin(MedecinInfo medecin, final int position, final MedecinListRecyclerAdapter.MedecinInfoHolder holder) {
        Log.e("MedecinFragment", "onDeleteMedecin");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.deleteMedecin(idUser, medecin.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONObject obj = JsonUtils.getObj(body);
                        if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true) {
                            holder.changeRequestState(MedecinInfo.State.REMOVED, position);

                            holder.removeItem(position);
                        }
                        else {
                            ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                            holder.info.setState(MedecinInfo.State.PRESENT);
                            listRecyclerAdapter.notifyItemChanged(position);
                            manageError(apiError);
                        }
                    } else {
                        String error = response.errorBody().string();
                        System.out.println("erreur message = " + error);
                        ApiErrors apiError = ApiErrors.getFromMessage(error);
                        holder.info.setState(MedecinInfo.State.PRESENT);
                        listRecyclerAdapter.notifyItemChanged(position);
                        manageError(apiError);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    ApiErrors api = ApiErrors.NETWORK_ERROR;
                    holder.info.setState(MedecinInfo.State.PRESENT);
                    listRecyclerAdapter.notifyItemChanged(position);
                    manageError(api);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ApiErrors apiError = ApiErrors.NETWORK_ERROR;
                holder.info.setState(MedecinInfo.State.PRESENT);
                listRecyclerAdapter.notifyItemChanged(position);
                manageError(apiError);
            }
        });
    }
}
