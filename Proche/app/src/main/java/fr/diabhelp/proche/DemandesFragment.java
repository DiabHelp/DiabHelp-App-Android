package fr.diabhelp.proche;

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

/**
 * Created by 4kito on 02/08/2016.
 */
public class DemandesFragment extends Fragment {
    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    private RecyclerView requestRecyclerView;
    private PatientRequestRecyclerAdapter requestRecyclerAdapter;
    private LinearLayoutManager requestRecLayoutManager;
    private RecyclerView listRecyclerView;
    private PatientListRecyclerAdapter listRecyclerAdapter;
    private LinearLayoutManager  listRecLayoutManager;
    String APIToken;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_demandes, container, false);

        // Création de la liste des requêtes
        requestRecyclerView = (RecyclerView) v.findViewById(R.id.patient_request_recycler_view);
        requestRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        requestRecyclerView.setLayoutManager(requestRecLayoutManager);
        requestRecyclerAdapter = new PatientRequestRecyclerAdapter(APIToken, this, requestRecyclerView);
        requestRecyclerView.setAdapter(requestRecyclerAdapter);
        setRecyclerViewHeight(requestRecyclerView);
        return v;
    }

    public void setRecyclerViewHeight(RecyclerView view)
    {
        int itemCount = view.getAdapter().getItemCount();
        int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70 * itemCount, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = viewHeight;
        view.setLayoutParams(params);
        Log.d("PatientView", "viewHeight = " + viewHeight);
    }

    public boolean sendRequestResponse(String response, int id)
    {
        Log.d("PatientRequest API", "Sending response : " + response + " for request id : " + id);
        return true;
    }

}
