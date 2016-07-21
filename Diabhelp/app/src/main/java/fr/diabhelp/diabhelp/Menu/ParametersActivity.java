package fr.diabhelp.diabhelp.Menu;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;

import fr.diabhelp.diabhelp.API.Asynctasks.ParametresAPICallTask;
import fr.diabhelp.diabhelp.API.IApiCallTask;
import fr.diabhelp.diabhelp.API.ResponseModels.ResponseCatalogue;
import fr.diabhelp.diabhelp.Core.CatalogueFragment;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperCallback;
import fr.diabhelp.diabhelp.Core.ParametresRecyclerAdapter;
import fr.diabhelp.diabhelp.Core.ParametresRecyclerAdapter.ParametresModuleHolder;
import fr.diabhelp.diabhelp.Models.ModuleList;
import fr.diabhelp.diabhelp.R;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;
import fr.diabhelp.diabhelp.Utils.MyToast;

public class ParametersActivity extends AppCompatActivity implements IApiCallTask<ResponseCatalogue> {

    private RecyclerView recyclerView;
    private ParametresRecyclerAdapter recAdapter;
    private LayoutManager  recLayoutManager;
    private ModuleList moduleList;
    private BroadcastReceiver broad;
    private ProgressDialog _progress;
    private static final Integer launch = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(layout.activity_parameters);
        this.moduleList = new ModuleList(this);
        Toolbar toolbar = (Toolbar) this.findViewById(id.toolbar);
        this.setSupportActionBar(toolbar);
        ActionBar act = this.getSupportActionBar();
        act.setDisplayHomeAsUpEnabled(true);
        this.recyclerView = (RecyclerView) this.findViewById(id.recycler_view);
        this.recyclerView.setHasFixedSize(true);
        this.recLayoutManager = new LinearLayoutManager(this);
        this.recyclerView.setLayoutManager(this.recLayoutManager);
        this.recAdapter = new ParametresRecyclerAdapter(this.moduleList, this, (CoreActivity) this.getParent());
        this.recyclerView.setAdapter(this.recAdapter);
        this.registerForContextMenu(this.recyclerView);
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(this.recAdapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(this.recyclerView);
        new ParametresAPICallTask(this, this).execute();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        ParametresModuleHolder holder;
        try {
            holder = ((ParametresRecyclerAdapter) this.recyclerView.getAdapter()).getContextMenuHolder();
        } catch (Exception e) {
            Log.d("ModuleManager", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case id.action_uninstall:
                Log.d("ModuleManager", "Context Menu : Uninstall app : " + holder.pname);
                holder.uninstallApp();
                break;
            case id.action_store:
                Log.d("ModuleManager", "Context Menu : Store : " + holder.pname);
                holder.openStore();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
    }

    @Override
    public void onBackgroundTaskCompleted(ResponseCatalogue response, String action, ProgressDialog progress) {
        CatalogueFragment.Error error = response.getError();
        Integer errorCode = error.getErrorCode();
        if (errorCode != null && errorCode != 0) {
            this.manageError(response.getError());
        }
        else if (action.equals("getModules")) {
            this.recAdapter.setLatestVersion(response);
        }
    }

    private void manageError(CatalogueFragment.Error error) {
        switch (error) {
            case SERVER_ERROR:
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_server), Toast.LENGTH_LONG, this);
                Log.e(getClass().getSimpleName(), "Problème survenu lors de la connexion au serveur");
                break;
        }
    }

    public void setBroadCast(BroadcastReceiver broad){this.broad = broad;}


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (this.broad != null)
            try {
                this.unregisterReceiver(this.broad);
            } catch(IllegalArgumentException e) {}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.broad != null)
            try {
                this.unregisterReceiver(this.broad);
            } catch(IllegalArgumentException e) {}
    }
}
