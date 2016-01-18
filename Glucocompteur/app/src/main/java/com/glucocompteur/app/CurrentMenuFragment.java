package com.glucocompteur.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;
import android.widget.AdapterView;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Simon on 25-Nov-15.
 */
public class CurrentMenuFragment extends Fragment {
    private RecyclerView                _recyclerView;
    private RecyclerView.Adapter        _recAdapter;
    private RecyclerView.LayoutManager  _recLayoutManager;
    private ArrayList<Aliment>          _alimentsList = new ArrayList<>();
    private DBSearchBox searchBox;
    private DBHelper dbHelper;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);

        //File file = new File(getActivity().getApplicationInfo().dataDir + "/databases/aliments.db");
        //boolean deleted = file.delete();
        //Log.d("DELETING DB FILE", "" + file + " == " + deleted);

        dbHelper = new DBHelper(getActivity());
        if (dbHelper.openDataBase() == false)
            Log.d("DBAliment", "Could not open DB");
        searchBox = (DBSearchBox) v.findViewById(R.id.search_input);
        searchBox.initDBHooks(dbHelper);
        searchBox.setAlimentList(_alimentsList);
        _recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        //_recyclerView.setHasFixedSize(true);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        _recAdapter = new AlimentRecyclerAdapter(_alimentsList);
        _recyclerView.setAdapter(_recAdapter);
        searchBox.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyObject selected = searchBox.products.get(0);
                // Aliment (String name, Integer weight, Integer glucids, Integer totalGlucids)
                Log.d("GlucocompteurAdd", "Adding aliment : " + selected.getObjectName() + "glucides = " + selected.getObjectGlucides());
                String glucidesStr = selected.getObjectGlucides().replace(",", ".");
                Float glucides = 0f;
                try {
                    glucides = Float.parseFloat(glucidesStr);

                } catch (NumberFormatException e) {
                    Log.d("DBField", "Incorrect field : " + selected.getObjectName() + " : " + selected.getObjectGlucides());
                }

                Aliment toAdd = new Aliment(selected.getObjectName(), 0f, glucides, 0f);
                _alimentsList.clear(); // La recyclerview bug avec plus d'un aliment pour le moment
                _alimentsList.add(toAdd);
                _recAdapter.notifyDataSetChanged();
                searchBox.setText("");
            }
        });
        return v;
    }

}
