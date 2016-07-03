package fr.diabhelp.glucocompteur;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Simon on 23-Nov-15.
 */
public class AlimentRecyclerAdapter extends RecyclerView.Adapter<AlimentRecyclerAdapter.AlimentHolder> {
    private final ArrayList<Aliment> _alimentsList;

    public static class AlimentHolder extends RecyclerView.ViewHolder {
        TextView    name;
        TextView    weight;
        TextView    glucids;
        TextView    totalGlucids;
        ImageView   alimentLogo;

        public AlimentHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            weight = (TextView) itemView.findViewById(R.id.weight);
            glucids = (TextView) itemView.findViewById(R.id.glucids);
            totalGlucids = (TextView) itemView.findViewById(R.id.totalGlucids);
            //alimentLogo = (ImageView) itemView.findViewById(R.id.alimentLogo);
        }
    }

     public AlimentRecyclerAdapter(ArrayList<Aliment> alimentsList) { _alimentsList = alimentsList; }

    @Override
    public AlimentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aliment_cardview, parent, false);
        final AlimentHolder alimentHolder = new AlimentHolder(view);
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Supprimer l'aliment");
                alertDialogBuilder.setMessage("Voulez-vous vraiment supprimer cet aliment ?")
                        .setCancelable(false);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _alimentsList.remove(alimentHolder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                alertDialog.show();
                return true;
            }
        });
        return alimentHolder;
    }

    @Override
    public void onBindViewHolder(AlimentHolder holder, int position) {
        holder.name.setText(_alimentsList.get(position).getName());
        holder.weight.setText(String.valueOf("Quantité : " + _alimentsList.get(position).getWeight()) + "g");
        holder.glucids.setText(String.valueOf("Glucides : " + _alimentsList.get(position).getGlucids()) + "g/100");
        holder.totalGlucids.setText("Total : " + String.valueOf(_alimentsList.get(position).getTotalGlucids()) + "g");
        //holder.alimentLogo.setImageResource(R.drawable.aliment);
    }

    @Override
    public int getItemCount() {
        return _alimentsList.size();
    }

}
