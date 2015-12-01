package com.glucocompteur.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;

import java.util.ArrayList;

/**
 * Created by Simon on 25-Nov-15.
 */
public class CurrentMenuFragment extends Fragment {
    private RecyclerView                _recyclerView;
    private RecyclerView.Adapter        _recAdapter;
    private RecyclerView.LayoutManager  _recLayoutManager;
    private ArrayList<Aliment>          _alimentsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        _alimentsList.add(new Aliment("Sucre", 1, 2, 3));
        _alimentsList.add(new Aliment("Sel", 4, 5, 6));
        _alimentsList.add(new Aliment("Pain", 7, 8, 9));
        _alimentsList.add(new Aliment("Eau", 10, 11, 12));
        _recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        _recyclerView.setHasFixedSize(true);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        _recAdapter = new AlimentRecyclerAdapter(_alimentsList);
        _recyclerView.setAdapter(_recAdapter);
        return v;
    }

}
