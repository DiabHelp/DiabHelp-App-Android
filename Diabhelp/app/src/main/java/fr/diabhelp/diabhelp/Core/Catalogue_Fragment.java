package fr.diabhelp.diabhelp.Core;

/**
 * Created by naqued on 28/09/15.
 */
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import fr.diabhelp.diabhelp.ApiCallTask;
import fr.diabhelp.diabhelp.ConnexionState;
import fr.diabhelp.diabhelp.IApiCallTask;
import fr.diabhelp.diabhelp.JsonUtils;
import fr.diabhelp.diabhelp.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Catalogue_Fragment extends Fragment implements IApiCallTask {
    private RecyclerView                _recyclerView;
    private RecyclerView.Adapter        _recAdapter;
    private RecyclerView.LayoutManager  _recLayoutManager;
    private ArrayList<CatalogModule>    _modulesList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ConnexionState co = new ConnexionState(getActivity());
        if (co.getStatus()){
            new ApiCallTask(this, ApiCallTask.POST, ApiCallTask.OBJECT, "getModulesList").execute("0", "modules");
        }
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
        View v = inflater.inflate(R.layout.catalogue_fragment, container, false);
        _recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        _recyclerView.setHasFixedSize(true);
        _recLayoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        _recyclerView.setLayoutManager(_recLayoutManager);
        _recAdapter = new CatalogRecyclerAdapter(_modulesList);
        _recyclerView.setAdapter(_recAdapter);
        return v;
    }

    private void checkAvailability() {
        List<PackageInfo> packs = getActivity().getPackageManager().getInstalledPackages(0);
        for(int i = 0 ; i < packs.size() ; i++) {
            PackageInfo p = packs.get(i);
            if ((p.packageName.contains("diabhelp") && !p.packageName.contains("diabhelp.diabhelp"))) {
                for (int j = 0 ; j < _modulesList.size() ; j++) {
                    if (_modulesList.get(j).getName().equals(p.applicationInfo.loadLabel(getActivity().getPackageManager()).toString()));
                        //_modulesList.remove(j);
                }
            }
        }
    }

    private void getModulesList(String data) {
        System.out.println("renvoi de la chaine json = " + data);
        if (data.equals("io exception")){
            return;
        }
        JSONArray array = JsonUtils.get_array(data);
        for (int i = 0 ; i < array.length() ; i++) {
            JSONObject obj;
            CatalogModule module = new CatalogModule();
            if ((obj = JsonUtils.getObjfromArray(array, i)) != null)
            {
                String name;
                if ((name = JsonUtils.getStringfromKey(obj, "name")) != null)
                    module.setName(name);
                String desc;
                if ((desc= JsonUtils.getStringfromKey(obj, "describ")) != null)
                    module.setDesc(desc);
                String rating;
                if ((rating = JsonUtils.getStringfromKey(obj, "note")) != null)
                    module.setRating(rating);
                String url;
                if ((url = JsonUtils.getStringfromKey(obj, "url")) != null)
                    module.setURLStore(url);
                String version;
                if ((version = JsonUtils.getStringfromKey(obj, "version")) != null) {
                    if (version.equals("null"))
                        module.setVersion("v0.1");
                    else
                        module.setVersion(version);
                }
                _modulesList.add(module);
            }
        }
        checkAvailability();
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (action.compareTo("getModulesList") == 0) {
            getModulesList(s);
        }
        _recAdapter.notifyDataSetChanged();
    }
}
