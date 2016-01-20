package fr.diabhelp.diabhelp.Core;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
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
        TextView    version;
        RatingBar   rating;
        TextView    desc;
        ImageView   logo;
        String      URLStore;
        //String      URLWeb;
        //TextView    size;
        TextView    isNew;

        public CatalogModuleHolder(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            version = (TextView) itemView.findViewById(R.id.version);
            rating = (RatingBar) itemView.findViewById(R.id.rating);
            LayerDrawable stars = (LayerDrawable) rating.getProgressDrawable();
            stars.getDrawable(2).setColorFilter(Color.rgb(255, 215, 0), PorterDuff.Mode.SRC_ATOP);
            desc = (TextView) itemView.findViewById(R.id.desc);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            //size = (TextView) itemView.findViewById(R.id.size);
            isNew = (TextView) itemView.findViewById(R.id.isnew);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog alertDialog = new AlertDialog.Builder(v.getContext()).create();
                    alertDialog.setTitle("Confirmation");
                    alertDialog.setMessage("Voulez-vous etre redirige vers le Playstore ou vers la page Web du module ?");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "STORE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String urlstore = URLStore.replace("https://play.google.com/store/apps/", "market://");
                                System.out.println("URL store: " + urlstore);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(urlstore));
                                itemView.getContext().startActivity(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(URLStore));
                                itemView.getContext().startActivity(intent);
                            }
                        }
                    });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "WEB", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String urlstore = URLStore.replace("https://play.google.com/store/apps/", "market://");
                                System.out.println("URL store: " + urlstore);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(urlstore));
                                itemView.getContext().startActivity(intent);
                            } catch (Exception e) {
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(URLStore));
                                itemView.getContext().startActivity(intent);
                            }
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
        holder.version.setText(_modulesList.get(pos).getVersion());
        holder.rating.setRating(Integer.valueOf(_modulesList.get(pos).getRating()));
        holder.desc.setText(_modulesList.get(pos).getDesc());
        holder.logo.setImageResource(R.drawable.diab_logo);
        holder.URLStore = _modulesList.get(pos).getURLStore();
        //holder.URLWeb = _modulesList.get(pos).getURLWeb();
        //holder.size.setText("Taille : " + _modulesList.get(pos).getSize());
        holder.isNew.setText("NEW");
    }

    @Override
    public int getItemCount() { return _modulesList.size(); }

}
