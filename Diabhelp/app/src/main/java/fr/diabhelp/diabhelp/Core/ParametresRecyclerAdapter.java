package fr.diabhelp.diabhelp.Core;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.API.ResponseModels.ResponseCatalogue;
import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperAdapter;
import fr.diabhelp.diabhelp.Menu.ParametersActivity;
import fr.diabhelp.diabhelp.Models.CatalogModule;
import fr.diabhelp.diabhelp.Models.ModuleList;
import fr.diabhelp.diabhelp.R;

/**
 * Created by Simon for Diabhelp
 * Started on 14 Oct 2015 at 15:27
 */
public class ParametresRecyclerAdapter extends RecyclerView.Adapter<ParametresRecyclerAdapter.ParametresModuleHolder> implements ItemTouchHelperAdapter{

    private final ParametersActivity context;
    private CoreActivity parent;
    private final ArrayList<ParametresModule> _modulesList;
    private ParametresRecyclerAdapter.ParametresModuleHolder contextMenuHolder;

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

    @Override
    public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
        notifyItemChanged(viewHolder.getAdapterPosition());
        ((ParametresRecyclerAdapter.ParametresModuleHolder) viewHolder).uninstallApp();
    }

    public void setContextMenuHolder(ParametresRecyclerAdapter.ParametresModuleHolder contextMenuHolder) {
        this.contextMenuHolder = contextMenuHolder;
    }

    public ParametresRecyclerAdapter.ParametresModuleHolder getContextMenuHolder() {
        return contextMenuHolder;
    }

    public void setLatestVersion(ResponseCatalogue r) {
        List<CatalogModule> arr = r.getModules();
        if (!arr.isEmpty())
        {
            for (CatalogModule c : arr)
            {
               for (ParametresModule p : this._modulesList)
               {
                   if (c.getName().equals(p.getAppName()))
                   {
                       p.setLatestVersion(c.getVersion());
                   }
               }
            }
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
        this.parent.updateModuleList();
        Log.d("ModuleManager", "Uninstall BR : ModuleList size = " + _modulesList.size());
        notifyDataSetChanged();
    }


    public static class ParametresModuleHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView name;
        public TextView size;
        public TextView version;
        public TextView updateNotif;
        public String pname;
        public ImageView logo;

        public ParametresModuleHolder(View _itemView) {
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
            try {
                itemView.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pname)));
            }
            catch (Exception e) {
                Log.e("OpenStore", "Can't start Activity market");
            }
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

    public ParametresRecyclerAdapter(ModuleList container, ParametersActivity context, CoreActivity parent) {
        this.context = context;
        _modulesList = container.getModulesList();
          /* On vérifie si le package a bien été désinstallé sans reparser la liste des modules */
        UninstallBroadcastReceiver broadcastReceiver = new UninstallBroadcastReceiver(new Handler(), this);
        context.setBroadCast(broadcastReceiver);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        context.registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public ParametresRecyclerAdapter.ParametresModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parametres_cardview_row, parent, false);
        ParametresRecyclerAdapter.ParametresModuleHolder moduleManagerModuleHolder = new ParametresRecyclerAdapter.ParametresModuleHolder(view);
        return moduleManagerModuleHolder;
    }

    @Override
    public void onBindViewHolder(final ParametresRecyclerAdapter.ParametresModuleHolder holder, int pos) {
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
            if (i >= currentArray.length)
                return false;
            if (Integer.parseInt(lastestArray[i]) > Integer.parseInt(currentArray[i]))
                return false;
        }
        return true;
    }

    @Override
    public int getItemCount() {
        return _modulesList.size();
    }

}
