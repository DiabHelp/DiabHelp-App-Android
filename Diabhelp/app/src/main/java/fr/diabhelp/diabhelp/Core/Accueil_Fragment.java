package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.R;

public class Accueil_Fragment extends Fragment {


    public GridView grid;
    private String[] web;
    private Drawable[] img;
    private DAO bdd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.accueil_fragment, container, false);
        //        bdd = new DAO(getActivity().getBaseContext());


        CoreActivity activity = (CoreActivity) getActivity();
        final ArrayList<CoreActivity.PInfo> app = activity.getAppList();
        web = activity.getAppStringList();
        img = activity.getAppDrawableList();

        CustomGrid adapter = new CustomGrid(getActivity(), web, img);
        grid=(GridView) inflate.findViewById(R.id.grid);

        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (app.get(position).pname.contains("diab_website")) {
//                    if (_session != null) {
                        String url = "http://www.diabhelp.fr";
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                        startActivity(intent);
//                    }
                    /*else {
                        MyToast toast = MyToast.getInstance();
                        toast.displayWarningMessage("Vous ne pouvez pas accéder à cette fonctionnalité, car vous n'ètes pas connécté à Internet.", 10, Core.this);
                    }*/
                }
                else if (app.get(position).pname.contains("help_me")) {
//                    Intent Intmod_manager = new Intent(Core.this, HelpActivity.class);
//                    startActivity(Intmod_manager);
                }
                else {
                    Intent intent = new Intent(app.get(position).pname);
                    String unflat = app.get(position).pname;
                    String appnameclean;
                    appnameclean = app.get(position).appname;
                    appnameclean = appnameclean.replaceAll("\\s", "");

                    unflat = unflat.concat("/").concat(app.get(position).pname).concat(".").concat(appnameclean);

                    intent.setComponent(ComponentName
                            .unflattenFromString(unflat));
                    intent.addCategory("android.intent.category.LAUNCHER");
                    //intent.putExtra("session", _session);

                    startActivity(intent);
                }
            }
        });
        return inflate;
    }

}