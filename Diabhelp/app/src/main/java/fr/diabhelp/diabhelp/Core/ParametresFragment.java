package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */

import android.app.ProgressDialog;
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

import fr.diabhelp.diabhelp.API.ApiCallTask;
import fr.diabhelp.diabhelp.Utils.NetworkUtils;
import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperCallback;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.R;

public class ParametresFragment extends Fragment implements IApiCallTask {
    private RecyclerView                recyclerView;
    private ParametresRecyclerAdapter recAdapter;
    private RecyclerView.LayoutManager  recLayoutManager;
    private static Integer launch = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser == true && launch == 0)
        {
            if (NetworkUtils.getConnectivityStatus(getActivity())) {
                new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "ParametresModuleList").execute("0", "modules");
            }
            launch = 1;
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
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
        Log.d("ModuleManager", "onCreateView");
        View v = inflater.inflate(R.layout.parametres_fragment, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(recLayoutManager);
        recAdapter = new ParametresRecyclerAdapter(getModulesList((CoreActivity)getActivity()), getActivity());
        recyclerView.setAdapter(recAdapter);
        registerForContextMenu(recyclerView);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(recAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);
        return v;
    }

    private ArrayList<ParametresModule> getModulesList(CoreActivity activity) {
        ArrayList<CoreActivity.PInfo> pInfoList= activity.getAppList();
        Log.d("ModuleManager", "pInfoList CoreActivity size = " + pInfoList.size());
        ArrayList modulesList = new ArrayList<>();
        if (pInfoList != null)
        {
            int moduleListSize = pInfoList.size(); //Offset parce que l'array actuel contient l'aide + la redirection vers le site
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

    @Override
    public void onBackgroundTaskCompleted(Object bodyResponse, String action, ProgressDialog progress) {

    }
}

