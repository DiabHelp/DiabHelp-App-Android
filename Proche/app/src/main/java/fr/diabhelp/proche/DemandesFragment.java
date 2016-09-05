package fr.diabhelp.proche;

import android.location.Location;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by 4kito on 02/08/2016.
 */
public class DemandesFragment extends Fragment {
    private final int                       REQUEST = 0;
    private final int                       ACCEPTED = 1;
    private final int                       REFUSED = 2;
    private final int                       WAITING = 3;
    private ArrayList<PatientRequest>       requestsList = new ArrayList<>();
    private RecyclerView                    requestRecyclerView;
    private PatientRequestRecyclerAdapter   requestRecyclerAdapter;
    private LinearLayoutManager             requestRecLayoutManager;
    final OkHttpClient                      client = new OkHttpClient();
    String                                  APIToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_demandes, container, false);

        // Création de la liste des requêtes
        requestRecyclerView = (RecyclerView) v.findViewById(R.id.patient_request_recycler_view);
        requestRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        requestRecyclerView.setLayoutManager(requestRecLayoutManager);
        try {
            getRequestsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        requestRecyclerAdapter = new PatientRequestRecyclerAdapter(APIToken, this, requestRecyclerView, requestsList);
        requestRecyclerView.setAdapter(requestRecyclerAdapter);
        return v;
    }

    public void getRequestsList() throws Exception {
        Request request = new Request.Builder()
                .url("http://www.dev.diabhelp.org/api/proche/getAllByUserId/25")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        JSONObject json = new JSONObject(response.body().string());
        JSONArray array = json.getJSONArray("users");
        for (int i = 0; i < array.length(); i++) {
            JSONObject item;
            if ((item = array.getJSONObject(i).getJSONObject("proche")) != null) {
                requestsList.add(new PatientRequest(item.getString("firstname") + " " + item.getString("lastname"), i, REQUEST));
            }
            //Log.d("JSON", array.getJSONObject(i).toString());
        }
        //Log.d("OKHTTP ", response.body().string());

    }

    public boolean sendRequestResponse(String response, int id)
    {
        Log.d("PatientRequest API", "Sending response : " + response + " for request id : " + id);
        return true;
    }

}
