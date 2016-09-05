package fr.diabhelp.proche;

import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
public class SuivisFragment extends Fragment {
    private RecyclerView                    _recyclerView;
    private RecyclerView.Adapter            _recAdapter;
    private RecyclerView.LayoutManager      _recLayoutManager;
    private ArrayList<Patient>              _patientsList = new ArrayList<>();
    final   OkHttpClient                    _client = new OkHttpClient();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_suivis, container, false);
        _recyclerView = (RecyclerView) v.findViewById(R.id.patient_suivi_recycler_view);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        try {
            getPatientsList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        _recAdapter = new PatientRecyclerAdapter(getContext(), this, _patientsList);
        _recyclerView.setAdapter(_recAdapter);
        return v;
    }

    public void getPatientsList() throws Exception {
        Request request = new Request.Builder()
                .url("http://www.dev.diabhelp.org/api/patient/getAllByUserId/25")
                .build();

        Response response = _client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        /*
        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            Log.d("OKHTTP ", responseHeaders.name(i) + ": " + responseHeaders.value(i));
        }
        */

        JSONObject  json = new JSONObject(response.body().string());
        JSONArray   array = json.getJSONArray("users");
        for (int i = 0; i < array.length(); i++) {
            JSONObject item;
            if ((item = array.getJSONObject(i).getJSONObject("patient")) != null) {
                _patientsList.add(new Patient(item.getString("firstname"), item.getString("lastname"), new Location(""), Patient.State.OK));
                Log.d("JSON", item.toString());
            }
            //Log.d("JSON", array.getJSONObject(i).toString());
        }
        //Log.d("OKHTTP ", response.body().string());

    }

}

