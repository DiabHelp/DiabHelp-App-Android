package fr.diabhelp.glucocompteur;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by Simon on 25-Nov-15.
 */
public class FavoritesMenusFragment extends Fragment {
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
}
