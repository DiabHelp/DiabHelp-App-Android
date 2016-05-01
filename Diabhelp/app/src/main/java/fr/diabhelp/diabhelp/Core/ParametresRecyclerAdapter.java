package fr.diabhelp.diabhelp.Core;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperAdapter;
import fr.diabhelp.diabhelp.Utils.JsonUtils;
import fr.diabhelp.diabhelp.R;

/**
 * Created by Simon for Diabhelp
 * Started on 14 Oct 2015 at 15:27
 */
public class ParametresRecyclerAdapter extends RecyclerView.Adapter<ParametresRecyclerAdapter.ParametresModuleHolder> implements ItemTouchHelperAdapter{
    private final CoreActivity _activity;
    private ArrayList<ParametresModule> _modulesList;
    private ParametresModuleHolder contextMenuHolder;

    private int getModulePosition(String appname) {
        Log.d("ModuleManager", "getModulePosition: Looking for app : " + appname);
        int listsize = _modulesList.size();
        for (int i = 0; i < listsize; i++) {
            Log.d("ModuleManager", "_modulesList.get(i) : " + _modulesList.get(i).getAppName());
            if (_modulesList.get(i).getAppName().equals(appname))
                return i;
        }
        return -1;
    }

    public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
        notifyItemChanged(viewHolder.getAdapterPosition());
        ((ParametresModuleHolder) viewHolder).uninstallApp();
    }

    public void setContextMenuHolder(ParametresModuleHolder contextMenuHolder) {
        this.contextMenuHolder = contextMenuHolder;
    }

    public ParametresModuleHolder getContextMenuHolder() {
        return contextMenuHolder;
    }

    public void setLatestVersion(String s) {
        JSONArray array = null;
        try {
            array = JsonUtils.getArray(s);
        Log.d("ModuleManager", "SetLatestVersion : ModuleList size = " + _modulesList.size());
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj;
                if ((obj = JsonUtils.getObjFromArray(array, i)) != null) {
                    String name;
                    if ((name = JsonUtils.getStringFromKey(obj, "name")) != null) {
                        for (int j = 0; j < _modulesList.size(); j++) {
                            if (_modulesList.get(i).getName().equals(name) || j == 0) {
                                String version;
                                if ((version = JsonUtils.getStringFromKey(obj, "version")) != null) {
                                    _modulesList.get(i).setLatestVersion(version);
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
        int position = getModulePosition(appname);
        Log.d("ModuleManager", "removing holder at position : " + position);
        if (position >= 0) {
            notifyItemRemoved(position);
            _modulesList.remove(position);
        }
        // Update app list in core
        _activity.updateModuleList();
        Log.d("ModuleManager", "Uninstall BR : ModuleList size = " + _modulesList.size());
        notifyDataSetChanged();
    }


    public static class ParametresModuleHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        TextView name;
        TextView size;
        TextView version;
        TextView updateNotif;
        String pname;
        ImageView logo;

        public ParametresModuleHolder(final View _itemView) {
            super(_itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            version = (TextView) itemView.findViewById(R.id.version);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            updateNotif = (TextView) itemView.findViewById(R.id.updateNotif);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ModuleManager", "Updating package : " + pname);
                    openStore();
                }
            });
        }

        public void openStore() {
            itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pname)));
        }

        public void uninstallApp() {
            Uri packageUri = Uri.parse("package:" + pname);
            Log.d("ModuleManager", "Uninstalling package : " + pname + ", holder position : " + getAdapterPosition());
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
            itemView.getContext().startActivity(uninstallIntent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, R.id.action_store, Menu.NONE, "Visiter la page");
            menu.add(Menu.NONE, R.id.action_uninstall, Menu.NONE, "Désinstaller");
        }

    }

    public ParametresRecyclerAdapter(ArrayList<ParametresModule> modulesList, FragmentActivity activity) {
        _activity = (CoreActivity) activity;
        _modulesList = modulesList;
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
    public ParametresModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parametres_cardview_row, parent, false);
        ParametresModuleHolder moduleManagerModuleHolder = new ParametresModuleHolder(view);
        return moduleManagerModuleHolder;
    }

    @Override
    public void onBindViewHolder(final ParametresModuleHolder holder, int pos) {
        holder.name.setText(_modulesList.get(pos).getName());
        holder.size.setText("Taille : " + _modulesList.get(pos).getSize());
        holder.version.setText(_modulesList.get(pos).getVersion());
        holder.logo.setImageDrawable(_modulesList.get(pos).getLogo());
        holder.pname = _modulesList.get(pos).getAppName();
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setContextMenuHolder(holder);
                return false;
            }
        });
        if (isUpToDate(_modulesList.get(pos).getLatestVersion(), _modulesList.get(pos).getVersion()))
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
        return _modulesList.size();
    }

}
