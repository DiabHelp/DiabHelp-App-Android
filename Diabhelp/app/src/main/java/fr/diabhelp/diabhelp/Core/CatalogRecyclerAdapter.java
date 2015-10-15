package fr.diabhelp.diabhelp.Core;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import fr.diabhelp.diabhelp.R;

import java.util.ArrayList;

/**
 * Created by Simon for Diabhelp
 * Started on 14 Oct 2015 at 15:27
 */
public class CatalogRecyclerAdapter extends RecyclerView.Adapter<CatalogRecyclerAdapter.CatalogModuleHolder> {
    private ArrayList<CatalogModule>    _modulesList;

    public static class CatalogModuleHolder extends RecyclerView.ViewHolder {
        TextView    name;
        TextView    size;
        TextView    version;
        RatingBar   rating;
        TextView    desc;
        ImageView   logo;

        public CatalogModuleHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            size = (TextView) itemView.findViewById(R.id.size);
            version = (TextView) itemView.findViewById(R.id.version);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            desc = (TextView) itemView.findViewById(R.id.desc);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Êtes-vous sûr de vouloir être redirigé vers le playstore ?");
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            });
        }
    }

    public CatalogRecyclerAdapter(ArrayList<CatalogModule> modulesList) {
        _modulesList = modulesList;
    }

    @Override
    public CatalogModuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalogue_cardview_row, parent, false);
        CatalogModuleHolder catalogModuleHolder = new CatalogModuleHolder(view);
        return catalogModuleHolder;
    }

    @Override
    public void onBindViewHolder(CatalogModuleHolder holder, int pos) {
        holder.name.setText(_modulesList.get(pos).getName());
        holder.size.setText("Taille : " + _modulesList.get(pos).getSize());
        holder.version.setText(_modulesList.get(pos).getVersion());
        holder.rating.setRating(Integer.valueOf(_modulesList.get(pos).getRating()));
        holder.desc.setText(_modulesList.get(pos).getDesc());
        holder.logo.setImageDrawable(_modulesList.get(pos).getLogo());
    }

    @Override
    public int getItemCount() {
        return _modulesList.size();
    }

}
