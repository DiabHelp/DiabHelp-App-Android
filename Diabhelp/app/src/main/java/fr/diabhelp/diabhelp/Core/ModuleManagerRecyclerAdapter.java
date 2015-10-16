package fr.diabhelp.diabhelp.Core;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

import fr.diabhelp.diabhelp.R;

/**
 * Created by Simon for Diabhelp
 * Started on 14 Oct 2015 at 15:27
 */
public class ModuleManagerRecyclerAdapter extends RecyclerView.Adapter<ModuleManagerRecyclerAdapter.ModuleManagerModuleHolder> {
    private ArrayList<CatalogModule>    _modulesList;

    public static class ModuleManagerModuleHolder extends RecyclerView.ViewHolder {
        TextView    name;
        TextView    size;
        TextView    version;
        TextView    desc;
        String pname;
        Button updateButton;
        Button uninstallButton;
        ImageView   logo;

        public ModuleManagerModuleHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            version = (TextView) itemView.findViewById(R.id.version);
            desc = (TextView) itemView.findViewById(R.id.desc);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            updateButton = (Button) itemView.findViewById(R.id.updateButton);
            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pname)));

                }
            });
            uninstallButton = (Button) itemView.findViewById(R.id.uninstallButton);
            uninstallButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    Uri packageUri = Uri.parse("package:" + pname);
                    Log.d("ModuleManager", "Uninstalling package : " + pname);
                    Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                    v.getContext().startActivity(uninstallIntent);
                    /* Useless, Android gère déjà le confirm quand on lance l'intent */
                    /*AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(v.getContext());
                    confirmBuilder.setTitle("Désinstaller l'application");
                    confirmBuilder.setMessage("Voulez vous vraiment désinstaller cette application ?");
                    confirmBuilder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    confirmBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageUri = Uri.parse("package:" + pname);
                            Log.d("ModuleManager", "Uninstalling package : " + pname);
                            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageUri);
                            v.getContext().startActivity(uninstallIntent);
                        }
                    });
                    confirmBuilder.show();
                    */
                }
            });

        }
    }

    public ModuleManagerRecyclerAdapter(ArrayList<CatalogModule> modulesList) {
        _modulesList = modulesList;
    }

    @Override
    public ModuleManagerModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.modulemanager_cardview_row, parent, false);
        ModuleManagerModuleHolder moduleManagerModuleHolder = new ModuleManagerModuleHolder(view);
        return moduleManagerModuleHolder;
    }

    @Override
    public void onBindViewHolder(ModuleManagerModuleHolder holder, int pos) {
        holder.name.setText(_modulesList.get(pos).getName());
        holder.size.setText("Taille : " + _modulesList.get(pos).getSize());
        holder.version.setText(_modulesList.get(pos).getVersion());
        holder.desc.setText(_modulesList.get(pos).getDesc());
        holder.logo.setImageDrawable(_modulesList.get(pos).getLogo());
        holder.pname = _modulesList.get(pos).getRating(); // TODO: Ranger le pname ailleurs que le rating (refaire une classe ? (Encore ?))
        //TODO : Check latest version avec l'API
        //holder.updateButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return _modulesList.size();
    }

}
