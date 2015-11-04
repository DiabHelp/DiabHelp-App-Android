package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */

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

import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

import fr.diabhelp.diabhelp.ApiCallTask;
import fr.diabhelp.diabhelp.ConnexionState;
import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperCallback;
import fr.diabhelp.diabhelp.IApiCallTask;
import fr.diabhelp.diabhelp.R;

public class Parametres_Fragment extends Fragment implements IApiCallTask {
    private RecyclerView                recyclerView;
    private ParametresRecyclerAdapter recAdapter;
    private RecyclerView.LayoutManager  recLayoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ConnexionState co = new ConnexionState(getActivity());
        if (co.getStatus()) {
            new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "ParametresModuleList").execute("0", "modules");
        }
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
    public boolean onContextItemSelected(MenuItem item) {
        ParametresRecyclerAdapter.ParametresModuleHolder holder;
        try {
            holder = ((ParametresRecyclerAdapter) recyclerView.getAdapter()).getContextMenuHolder();
        } catch (Exception e) {
            Log.d("ModuleManager", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case R.id.action_uninstall:
                Log.d("ModuleManager", "Context Menu : Uninstall app : " + holder.pname);
                holder.uninstallApp();
                break;
            case R.id.action_store:
                Log.d("ModuleManager", "Context Menu : Store : " + holder.pname);
                holder.openStore();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.parametres_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        recAdapter = new ParametresRecyclerAdapter(getModulesList(), getActivity());
        recyclerView.setAdapter(recAdapter);
        registerForContextMenu(recyclerView);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(recAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        return v;
    }

    private ArrayList<ParametresModule> getModulesList() {
        ArrayList<CoreActivity.PInfo> pInfoList= ((CoreActivity)getActivity()).getAppList();
        ArrayList modulesList = new ArrayList<>();
        if (pInfoList != null)
        {
            int moduleListSize = pInfoList.size() - 2; //Offset parce que l'array actuel contient l'aide + la redirection vers le site
            for (int i = 0; i < moduleListSize; i++)
            {
                CoreActivity.PInfo module = pInfoList.get(i);
                long size = new File(module.publicSourceDir).length();
                String sizeStr = String.format("%.2f", (size / 1000000.0)) + " Mo";
                modulesList.add(new ParametresModule(module.icon, module.appname, module.pname, sizeStr, "Version " + module.versionName));
            }
        }
        return modulesList;
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (action.equals("ParametresModuleList"))
        {
            recAdapter.setLatestVersion(s);
        }
    }



}

