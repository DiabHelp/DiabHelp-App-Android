package fr.diabhelp.glucocompteur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Simon on 25-Nov-15.
 */
public class FavoritesMenusFragment extends Fragment {
    private RecyclerView _recyclerView;
    private RecyclerView.Adapter        _recAdapter;
    private RecyclerView.LayoutManager  _recLayoutManager;
    private ArrayList<Menu>             _menuList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        _recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        final MenuManager manager = new MenuManager(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
        final SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                System.out.println(manager.getSavedMenu());
                swipeRefresh.setRefreshing(false);
            }
        });


        ArrayList<ExpandableListAdapter.Item> data = new ArrayList<>();
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Matin"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Lait"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Cereales"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Pomme"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Midi"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Pates"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Poulet"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Fromage"));
        data.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Yaourt"));

        ExpandableListAdapter.Item places = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, "Places");
       places.invisibleChildren = new ArrayList<>();
       places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Kerala"));
       places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Tamil Nadu"));
       places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Karnataka"));
       places.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, "Maharashtra"));

       data.add(places);


        _recyclerView.setAdapter(new ExpandableListAdapter(data));
        return v;
    }
}
