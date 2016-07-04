package fr.diabhelp.glucocompteur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by Simon on 25-Nov-15.
 */
public class FavoritesMenusFragment extends Fragment {
    private RecyclerView                _recyclerView;
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

        /*
        Delete fichier menu

        File file = new File(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
        boolean deleted = file.delete();

        Log.d("DELETING JSON FILE", "" + file + " == " + deleted);
        try {
            boolean created = file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */

        final SwipeRefreshLayout swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swiperefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                final MenuManager manager = new MenuManager(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
                _menuList = manager.getSavedMenu();
                ArrayList<ExpandableListAdapter.Item> data = new ArrayList<>();
                for (fr.diabhelp.glucocompteur.Menu menu : _menuList) {
                    ExpandableListAdapter.Item item = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, menu.getMenuName(), 0., menu.getMenuGlucids());
                    ArrayList<Aliment> aliments = menu.alimentsList;
                    for (Aliment aliment : aliments)
                        item.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, aliment.getName(), aliment.getWeight(), aliment.getTotalGlucids()));
                    data.add(item);
                }
                swipeRefresh.setRefreshing(false);
                _recyclerView.setAdapter(new ExpandableListAdapter(data));
            }
        });
        final MenuManager manager = new MenuManager(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
        _menuList = manager.getSavedMenu();
        ArrayList<ExpandableListAdapter.Item> data = new ArrayList<>();
        for (fr.diabhelp.glucocompteur.Menu menu : _menuList) {
            ExpandableListAdapter.Item item = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, menu.getMenuName(), 0., menu.getMenuGlucids());
            ArrayList<Aliment> aliments = menu.alimentsList;
            for (Aliment aliment : aliments) {
                item.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, aliment.getName(), aliment.getWeight(), aliment.getTotalGlucids()));
            }
            data.add(item);
        }
        _recyclerView.setAdapter(new ExpandableListAdapter(data));
        return v;
    }
}
