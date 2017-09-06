package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnetdesuivi;
import fr.diabhelp.diabhelp.Models.ModuleList;
import fr.diabhelp.diabhelp.Models.ModuleList.PInfo;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;

public class AccueilFragment extends Fragment {

    public GridView grid;
    private String[] web;
    private Drawable[] img;
    private DAO dao;
    private SQLiteDatabase db;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.dao = DAO.getInstance(this.getActivity().getApplicationContext());
        this.db = this.dao.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(layout.accueil_fragment, container, false);
        //        bdd = new DAO(getActivity().getBaseContext());

        ModuleList moduleList = new ModuleList(this.getActivity());
        final ArrayList<PInfo> app = moduleList.getAppList();
        this.web = moduleList.getAppStringList();
        this.img = moduleList.getAppDrawableList();

        CustomGrid adapter = new CustomGrid(this.getActivity(), this.web, this.img);
        this.grid =(GridView) inflate.findViewById(id.grid);

        this.grid.setAdapter(adapter);
        this.grid.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (app.get(position).pname.contains("")) {
//                    if (_session != null) {
                        Intent intent = new Intent(getContext(), Carnetdesuivi.class);
                    AccueilFragment.this.startActivity(intent);
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
                    AccueilFragment.this.startActivity(intent);
                }
            }
        });
        return inflate;
    }

}