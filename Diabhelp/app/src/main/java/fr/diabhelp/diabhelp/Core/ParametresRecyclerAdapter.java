package fr.diabhelp.diabhelp.Core;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperAdapter;
import fr.diabhelp.diabhelp.Core.ParametresRecyclerAdapter.ParametresModuleHolder;
import fr.diabhelp.diabhelp.R.id;
import fr.diabhelp.diabhelp.R.layout;
import fr.diabhelp.diabhelp.Utils.JsonUtils;

/**
 * Created by Simon for Diabhelp
 * Started on 14 Oct 2015 at 15:27
 */
public class ParametresRecyclerAdapter extends Adapter<ParametresModuleHolder> implements ItemTouchHelperAdapter{
    private final CoreActivity _activity;
    private final ArrayList<ParametresModule> _modulesList;
    private ParametresRecyclerAdapter.ParametresModuleHolder contextMenuHolder;

    private int getModulePosition(String appname) {
        Log.d("ModuleManager", "getModulePosition: Looking for app : " + appname);
        int listsize = this._modulesList.size();
        for (int i = 0; i < listsize; i++) {
            Log.d("ModuleManager", "_modulesList.get(i) : " + this._modulesList.get(i).getAppName());
            if (this._modulesList.get(i).getAppName().equals(appname))
                return i;
        }
        return -1;
    }

    @Override
    public void onItemDismiss(ViewHolder viewHolder) {
        this.notifyItemChanged(viewHolder.getAdapterPosition());
        ((ParametresRecyclerAdapter.ParametresModuleHolder) viewHolder).uninstallApp();
    }

    public void setContextMenuHolder(ParametresRecyclerAdapter.ParametresModuleHolder contextMenuHolder) {
        this.contextMenuHolder = contextMenuHolder;
    }

    public ParametresRecyclerAdapter.ParametresModuleHolder getContextMenuHolder() {
        return this.contextMenuHolder;
    }

    public void setLatestVersion(String s) {
        JSONArray array = null;
        try {
            array = JsonUtils.getArray(s);
        Log.d("ModuleManager", "SetLatestVersion : ModuleList size = " + this._modulesList.size());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj;
                if ((obj = JsonUtils.getObjFromArray(array, i)) != null) {
                    String name;
                    if ((name = JsonUtils.getStringFromKey(obj, "name")) != null) {
                        for (int j = 0; j < this._modulesList.size(); j++) {
                            if (this._modulesList.get(i).getName().equals(name) || j == 0) {
                                String version;
                                if ((version = JsonUtils.getStringFromKey(obj, "version")) != null) {
                                    this._modulesList.get(i).setLatestVersion(version);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    public void onModuleListUpdated(String appname) {

        Log.d("ModuleManager", "Package removed : " + appname);
        int position = this.getModulePosition(appname);
        Log.d("ModuleManager", "removing holder at position : " + position);
        if (position >= 0) {
            this.notifyItemRemoved(position);
            this._modulesList.remove(position);
        }
        // Update app list in core
        this._activity.updateModuleList();
        Log.d("ModuleManager", "Uninstall BR : ModuleList size = " + this._modulesList.size());
        this.notifyDataSetChanged();
    }


    public static class ParametresModuleHolder extends ViewHolder implements OnCreateContextMenuListener {
        TextView name;
        TextView size;
        TextView version;
        TextView updateNotif;
        String pname;
        ImageView logo;

        public ParametresModuleHolder(View _itemView) {
            super(_itemView);
            this.name = (TextView) this.itemView.findViewById(id.name);
            this.size = (TextView) this.itemView.findViewById(id.size);
            this.version = (TextView) this.itemView.findViewById(id.version);
            this.logo = (ImageView) this.itemView.findViewById(id.logo);
            this.updateNotif = (TextView) this.itemView.findViewById(id.updateNotif);
            this.itemView.setOnCreateContextMenuListener(this);
            this.itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ModuleManager", "Updating package : " + ParametresRecyclerAdapter.ParametresModuleHolder.this.pname);
                    ParametresRecyclerAdapter.ParametresModuleHolder.this.openStore();
                }
            });
        }

        public void openStore() {
            try {
                this.itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + this.pname)));
            }
            catch (Exception e) {
                Log.e("OpenStore", "Can't start Activity market");
            }
        }

        public void uninstallApp() {
            Uri packageUri = Uri.parse("package:" + this.pname);
            Log.d("ModuleManager", "Uninstalling package : " + this.pname + ", holder position : " + this.getAdapterPosition());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
            this.itemView.getContext().startActivity(uninstallIntent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, id.action_store, Menu.NONE, "Visiter la page");
            menu.add(Menu.NONE, id.action_uninstall, Menu.NONE, "Désinstaller");
        }
    }

    public ParametresRecyclerAdapter(ArrayList<ParametresModule> modulesList, FragmentActivity activity) {
        this._activity = (CoreActivity) activity;
        this._modulesList = modulesList;
          /* On vérifie si le package a bien été désinstallé sans reparser la liste des modules */
        UninstallBroadcastReceiver broadcastReceiver = new UninstallBroadcastReceiver(new Handler(), this);
        /*
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String appname = intent.getDataString();
                appname = appname.substring(appname.indexOf(':') + 1);
                if (appname.contains("diabhelp")) //TODO : Change for diabhelp
                {
                    Log.d("ModuleManager", "Package removed : " + appname);
                    int position = getModulePosition(appname);
                    Log.d("ModuleManager", "removing holder at position : " + position);
                    if (position >= 0) {
                        _modulesList.remove(position);
                        notifyItemRemoved(position);
                    }
                }

            }

        };
        */
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        activity.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public ParametresRecyclerAdapter.ParametresModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layout.parametres_cardview_row, parent, false);
        ParametresRecyclerAdapter.ParametresModuleHolder moduleManagerModuleHolder = new ParametresRecyclerAdapter.ParametresModuleHolder(view);
        return moduleManagerModuleHolder;
    }

    @Override
    public void onBindViewHolder(final ParametresRecyclerAdapter.ParametresModuleHolder holder, int pos) {
        holder.name.setText(this._modulesList.get(pos).getName());
        holder.size.setText("Taille : " + this._modulesList.get(pos).getSize());
        holder.version.setText(this._modulesList.get(pos).getVersion());
        holder.logo.setImageDrawable(this._modulesList.get(pos).getLogo());
        holder.pname = this._modulesList.get(pos).getAppName();
        holder.itemView.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ParametresRecyclerAdapter.this.setContextMenuHolder(holder);
                return false;
            }
        });
        if (this.isUpToDate(this._modulesList.get(pos).getLatestVersion(), this._modulesList.get(pos).getVersion()))
            holder.updateNotif.setVisibility(View.INVISIBLE);
    }

    private boolean isUpToDate(String latest, String current)
    {
        if (latest == null)
            return true;
        String [] lastestArray = latest.split(".");
        String [] currentArray = current.split(".");
        for (int i = 0; i < lastestArray.length; i++)
        {
            if (i >= currentArray.length) {
                return false;
            }
            if (Integer.parseInt(lastestArray[i]) > Integer.parseInt(currentArray[i])) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return this._modulesList.size();
    }

}
