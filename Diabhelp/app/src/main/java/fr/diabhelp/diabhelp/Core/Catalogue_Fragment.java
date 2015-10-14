package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fr.diabhelp.diabhelp.R;

import java.util.ArrayList;

public class Catalogue_Fragment extends Fragment {
    private RecyclerView                recyclerView;
    private RecyclerView.Adapter        recAdapter;
    private RecyclerView.LayoutManager  recLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_user) {
            return (false);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.action_user);
        item.setVisible(false);
        item.setEnabled(false);
        return ;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.catalogue_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        recAdapter = new CatalogRecyclerAdapter(getModulesList());
        recyclerView.setAdapter(recAdapter);
        return v;
    }

    private ArrayList<CatalogModule> getModulesList() {
        ArrayList modulesList = new ArrayList<>();
        modulesList.add(new CatalogModule("Le glucocompteur est un module qui permet de connaitre facilement les apports glucidiques d'un repas", getActivity().getResources().getDrawable(R.drawable.diab_logo), "Glucocompteur", "3", "2mb", "v1.0"));
        modulesList.add(new CatalogModule("Le glucocompteur est un module qui permet de connaitre facilement les apports glucidiques d'un repas", getActivity().getResources().getDrawable(R.drawable.diab_logo), "Glucocompteur", "4", "2mb", "v1.0"));
        modulesList.add(new CatalogModule("Le glucocompteur est un module qui permet de connaitre facilement les apports glucidiques d'un repas", getActivity().getResources().getDrawable(R.drawable.diab_logo), "Glucocompteur", "5", "2mb", "v1.0"));
        return modulesList;
    }
}

