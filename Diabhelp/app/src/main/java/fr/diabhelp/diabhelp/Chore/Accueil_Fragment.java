package fr.diabhelp.diabhelp.Chore;

/**
 * Created by naqued on 28/09/15.
 */

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.BDD.DAO;
import fr.diabhelp.diabhelp.R;

public class Accueil_Fragment extends Fragment {

    private ArrayList<PInfo> app;
    private ArrayList<String> listApp;
    private GridView grid;
    private String[] web;
    private Drawable[] img;
    private DAO bdd;

    class PInfo {
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode = 0;
        private Drawable icon;

        public int describeContents() {
            return (0);
        }

        private void DebugPrint() {
            Log.v(appname, appname + "\t" + pname + "\t" + versionName + "\t" + versionCode);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.accueil_fragment, container, false);

        bdd = new DAO(getActivity().getBaseContext());

        listApp = new ArrayList<String>();
        app = getPackages();

        CustomGrid adapter = new CustomGrid(getActivity(), web, img);
        grid=(GridView)inflate.findViewById(R.id.grid);
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

    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false); /* false = no system packages */
        final int max = apps.size();
        for (int i=0; i<max; i++) {
            apps.get(i).DebugPrint();

        }
        return apps;
    }

    private int count_dApp(List<PackageInfo> packs) {
        int ctr = 0;
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp"))
                ctr++;
        }
        return ctr;
    }

    private PInfo nav_manageur(Drawable t)
    {
        PInfo inf = new PInfo();
        inf.appname = "Site Diabhelp";
        inf.icon = t;
        inf.versionCode = 0;
        inf.pname = "diab_website";
        return inf;
    }
    private PInfo help_manageur(Drawable t)
    {
        PInfo inf = new PInfo();
        inf.appname = "Aide";
        inf.icon = t;
        inf.versionCode = 0;
        inf.pname = "help_me";
        return inf;
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        int ctr = 0;

        PInfo newInfo;

        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        int nbDApp = count_dApp(packs);
        img = new Drawable[nbDApp + 4];
        web = new String[nbDApp + 4];
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp")) {

                if ((!getSysPackages) && (p.versionName == null)) {
                    continue;
                }
                newInfo = new PInfo();
                newInfo.appname = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                newInfo.pname = p.packageName;
                listApp.add(p.packageName);
                newInfo.versionName = p.versionName;
                newInfo.versionCode = p.versionCode;
                newInfo.icon = p.applicationInfo.loadIcon(getActivity().getPackageManager());
                if (newInfo.icon != null && newInfo.appname != null) {
                    img[ctr] = newInfo.icon;
                    web[ctr++] = newInfo.appname;
                }
                res.add(newInfo);
            }
        }
        //set du nav
        newInfo = nav_manageur(getResources().getDrawable(R.drawable.web)); // icone par défaut a changer !!
        img[ctr] = newInfo.icon;
        web[ctr++] = newInfo.appname;
        res.add(newInfo);
        // set de l'help
        newInfo = help_manageur(getResources().getDrawable(R.drawable.help)); // icone par défaut a changer !!
        img[ctr] = newInfo.icon;
        web[ctr] = newInfo.appname;
        res.add(newInfo);
        return res;
    }
}