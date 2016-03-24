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



public class MedecinsFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_medecins, container, false);

        // Création de la liste des requêtes
        requestRecyclerView = (RecyclerView) v.findViewById(R.id.medecins_requetes_recycler_view);
        requestRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        //requestRecLayoutManager = new LinearLayoutManager(requestRecyclerView, LinearLayoutManager.VERTICAL, false); //test custom LinearLayoutManager
        //requestRecLayoutManager.setOverScrollMode(ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS);
        requestRecyclerView.setLayoutManager(requestRecLayoutManager);
        //requestRecyclerView.setHasFixedSize(true);
        requestRecyclerAdapter = new MedecinRequestRecyclerAdapter(APIToken);
        requestRecyclerView.setAdapter(requestRecyclerAdapter);
        setRecyclerViewHeight(requestRecyclerView);
        //requestRecyclerView.setLayoutParams(params);
        //Création de la liste des médecins
        listRecyclerView = (RecyclerView) v.findViewById(R.id.medecins_list_recycler_view);
        listRecLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        //listRecLayoutManager = new LinearLayoutManager(listRecyclerView, LinearLayoutManager.VERTICAL, false); //test custom LinearLayoutManager
        //listRecLayoutManager.setOverScrollMode(ViewCompat.OVER_SCROLL_IF_CONTENT_SCROLLS);
        listRecyclerView.setLayoutManager(listRecLayoutManager);
        //listRecyclerView.setHasFixedSize(true);
        listRecyclerAdapter = new MedecinListRecyclerAdapter(APIToken);
        listRecyclerView.setAdapter(listRecyclerAdapter);
        setRecyclerViewHeight(listRecyclerView);

        return v;
    }

    private void setRecyclerViewHeight(RecyclerView view)
    {
        int itemCount = view.getAdapter().getItemCount();
        int viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70 * itemCount, getResources().getDisplayMetrics());
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = viewHeight;
        view.setLayoutParams(params);
        Log.d("MedecinView", "viewHeight = " + viewHeight);

    }
}
