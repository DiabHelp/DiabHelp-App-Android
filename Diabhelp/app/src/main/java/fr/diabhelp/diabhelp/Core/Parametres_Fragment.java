package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */

import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperCallback;
import fr.diabhelp.diabhelp.R;

public class Parametres_Fragment extends Fragment {
    private RecyclerView                recyclerView;
    private ParametresRecyclerAdapter recAdapter;
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
        View v = inflater.inflate(R.layout.modulemanager_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        recAdapter = new ParametresRecyclerAdapter(getModulesList());
        recyclerView.setAdapter(recAdapter);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(recAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        return v;
    }

    /* GROS GROS DEBUG
    */
    private int count_dApp(List<PackageInfo> packs) {
        int ctr = 0;
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if (p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp"))
                ctr++;
        }
        return ctr;
    }

    private ArrayList<ParametresModule> getModulesList() {
        ArrayList modulesList = new ArrayList<>();
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        int nbDApp = count_dApp(packs);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ((p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp"))) {
                String appname = p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString();
                String pname = p.packageName;
                String versionName = p.versionName;
                int versionCode = p.versionCode;
                long size = new File(p.applicationInfo.publicSourceDir).length();
                String sizeStr = String.format("%.2f", (size / 1000000.0));
                modulesList.add(new ParametresModule("", p.applicationInfo.loadIcon(getActivity().getPackageManager()), appname, pname, sizeStr + " Mo" , "Version " + versionName));
                Log.d("ModuleManager", "Found app : " + appname + ", " + pname + ", " + versionName + ", " + versionCode);
            }
        }
        //*/
        return modulesList;
    }
}
