package fr.diabhelp.medecin_patient;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import fr.diabhelp.medecin_patient.API.ApiErrors;
import fr.diabhelp.medecin_patient.API.ApiService;
import fr.diabhelp.medecin_patient.API.ResponseChatMessage;
import fr.diabhelp.medecin_patient.API.RetrofitHelper;
import fr.diabhelp.medecin_patient.Listeners.ChatRecyclerListener;
import fr.diabhelp.medecin_patient.Utils.JsonUtils;
import fr.diabhelp.medecin_patient.Utils.MyToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements ChatRecyclerListener {

    public MainActivityFragment() {
    }
    private RecyclerView recyclerView;
    private ChatRecyclerAdapter recyclerAdapter;
    private RecyclerView.LayoutManager  recLayoutManager;

    String APIToken;
    String idUser;
    Medecin_Patient parent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        parent = (Medecin_Patient) getActivity();
        idUser = parent.getIdUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("Medecin Patient", "onCreateView");
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.chat_recycler_view);
        recyclerView.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        recyclerAdapter = new ChatRecyclerAdapter(APIToken, this, new ArrayList<ChatMessage>());
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1); // Scroll to bottom of messages
        final TextView textInput = (TextView)v.findViewById(R.id.msg_input);
        // C'est le listener, faut le caller dans une methode et call ca depuis recyclerAdapter
        //Ptet pas finalement ca semble OK comme ca
        v.findViewById(R.id.button_send_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = textInput.getText().toString();
                textInput.setText("");
                sendChatMessage(msg);
                recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1);
            }
        });
        try {
            getMessageHistory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return v;
    }

    private void getMessageHistory()
    {
        Log.d("ChatFragment", "Begin getMessageHistory");
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.getChatMessages(idUser, 5, Calendar.getInstance().getTimeInMillis());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        ResponseChatMessage rep = new ResponseChatMessage(JsonUtils.getObj(body));
                        displayChatMessages(rep);
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

    private void displayChatMessages(ResponseChatMessage rep) {
        if (recyclerAdapter == null)
            recyclerAdapter = new ChatRecyclerAdapter(null, this, rep.getChatMessages());
        else
            recyclerAdapter.setChatMessages(rep.getChatMessages());
        recyclerView.scrollToPosition(recyclerAdapter.getItemCount() - 1); // Scroll to bottom of messages
    }

    public  void sendChatMessage(String msg) {
        Log.d("ChatFragment", "Begin sendChatMessage : " + msg);
        RetrofitHelper retrofitHelper = new RetrofitHelper(getActivity());
        ApiService service = retrofitHelper.createService(RetrofitHelper.Build.DEV);
        Call<ResponseBody> call = service.postChatMessage(idUser, msg, Calendar.getInstance().getTimeInMillis());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String body = response.body().string();
                        JSONObject obj = JsonUtils.getObj(body);
                        if (obj != null && JsonUtils.getBoolFromKey(obj, "success") == true) {
                            Log.d("ChatFragment", "sendChatMessage : OK");
                        }
                        else {
                            ApiErrors apiError = ApiErrors.getFromMessage(response.errorBody().string());
                            manageError(apiError);
                        }
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
        recyclerAdapter.addMessage(msg);

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
                Log.e("MainFragment", "Problème survenu lors de la connexion au serveur");
                break;
            }
            case SERVER_ERROR: {
                MyToast.getInstance().displayWarningMessage("Erreur lors du traitement de la demande", Toast.LENGTH_LONG, getActivity());
                Log.e("MainFragment", "Erreur lors du traitement de la demande");
                break;
            }

        }
    }
}
