package fr.diabhelp.diabhelp.Core;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.Core.ItemTouchHelper.ItemTouchHelperAdapter;
import fr.diabhelp.diabhelp.R;

/**
 * Created by Simon for Diabhelp
 * Started on 14 Oct 2015 at 15:27
 */
public class ParametresRecyclerAdapter extends RecyclerView.Adapter<ParametresRecyclerAdapter.ParametresModuleHolder> implements ItemTouchHelperAdapter{
    private ArrayList<ParametresModule>    _modulesList;


    public void onItemDismiss(RecyclerView.ViewHolder viewHolder) {
        String pname = _modulesList.get(viewHolder.getAdapterPosition()).getAppName();
        Log.d("ModuleManager", "onItemDismiss called");
        Uri packageUri = Uri.parse("package:" + pname);
        Log.d("ModuleManager", "Uninstalling package : " + pname);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
        viewHolder.itemView.getContext().startActivity(uninstallIntent);
        /* On vérifie si le package a bien été désinstallé sans reparser la liste des modules */
        /*BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getDataString().equals(pname))
                    Log.d("ModuleManager", "Module désisntallé");
                    notifyItemRemoved(position);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        view.getContext().registerReceiver(broadcastReceiver, filter);*/
        //TODO : update liste des modules ici si on a bien uninstall
        //_modulesList.remove(viewHolder.getAdapterPosition());
        notifyItemRemoved(viewHolder.getAdapterPosition());


    }

    public static class ParametresModuleHolder extends RecyclerView.ViewHolder {
        TextView    name;
        TextView    size;
        TextView    version;
        TextView    desc;
        String pname;
        ImageView   logo;

        public ParametresModuleHolder(final View _itemView) {
            super(_itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            version = (TextView) itemView.findViewById(R.id.version);
            desc = (TextView) itemView.findViewById(R.id.desc);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ModuleManager", "Updating package (onclick) : " + pname);
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pname)));
                }
            });
        }
    }

    public ParametresRecyclerAdapter(ArrayList<ParametresModule> modulesList) {
        _modulesList = modulesList;
    }

    @Override
    public ParametresModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modulemanager_cardview_row, parent, false);
        ParametresModuleHolder moduleManagerModuleHolder = new ParametresModuleHolder(view);
        return moduleManagerModuleHolder;
    }

    @Override
    public void onBindViewHolder(ParametresModuleHolder holder, int pos) {
        holder.name.setText(_modulesList.get(pos).getName());
        holder.size.setText("Taille : " + _modulesList.get(pos).getSize());
        holder.version.setText(_modulesList.get(pos).getVersion());
        holder.desc.setText(_modulesList.get(pos).getDesc());
        holder.logo.setImageDrawable(_modulesList.get(pos).getLogo());
        holder.pname = _modulesList.get(pos).getAppName();
    }

    @Override
    public int getItemCount() {
        return _modulesList.size();
    }

}
