package fr.diabhelp.glucocompteur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

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

    /*
    MenuManager menuManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.menu_main, container, false);
        menuManager = new MenuManager(getActivity().getApplicationInfo().dataDir + "/menus_favoris.json");
        v.findViewById(R.id.load_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    menuManager.loadMenu();
                } catch (IOException e) {
                    Log.d("MenuFavori", "No JSON file found");
                    e.printStackTrace();
                }
            }
        });
        v.findViewById(R.id.reset_button).setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            menuManager.clearMenu();
        }
        });
        v.findViewById(R.id.debug_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuManager.debug();
            }
        });
        return v;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);

        _recyclerView = (RecyclerView) v.findViewById(R.id.recyclerview);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        List<ExpandableListAdapter.Item> data = new ArrayList<>();

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
