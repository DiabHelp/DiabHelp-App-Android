package com.glucocompteur.app;

import android.support.v7.widget.RecyclerView;
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
            alimentLogo = (ImageView) itemView.findViewById(R.id.alimentLogo);
        }
    }

    public AlimentRecyclerAdapter(ArrayList<Aliment> alimentsList) { _alimentsList = alimentsList; }

    @Override
    public AlimentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        System.out.println("PROUT");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.aliment_cardview, parent, false);
        System.out.println("PROUT");
        AlimentHolder alimentHolder = new AlimentHolder(view);
        return alimentHolder;
    }

    @Override
    public void onBindViewHolder(AlimentHolder holder, int position) {
        System.out.println("POUET");
        holder.name.setText(_alimentsList.get(position).getName());
        holder.weight.setText(String.valueOf(_alimentsList.get(position).getWeight()));
        holder.glucids.setText(String.valueOf(_alimentsList.get(position).getGlucids()));
        holder.totalGlucids.setText(String.valueOf(_alimentsList.get(position).getTotalGlucids()));
        holder.alimentLogo.setImageResource(R.drawable.aliment);
    }

    @Override
    public int getItemCount() {
        return _alimentsList.size();
    }

}
