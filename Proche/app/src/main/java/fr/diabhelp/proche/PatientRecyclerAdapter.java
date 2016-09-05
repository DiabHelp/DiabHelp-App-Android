package fr.diabhelp.proche;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
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
 * Created by 4kito on 03/08/2016.
 */
public class PatientRecyclerAdapter extends RecyclerView.Adapter<PatientRecyclerAdapter.PatientHolder> {
    private Context             _activityContext;
    private Fragment            _parentFragment;
    private ArrayList<Patient>  _patientsList = new ArrayList<>();

    public static class PatientHolder extends RecyclerView.ViewHolder {
        ImageView   logo;
        TextView    name;
        TextView    surname;

        public PatientHolder(View itemView) {
            super(itemView);
            logo = (ImageView) itemView.findViewById(R.id.logo);
            name = (TextView) itemView.findViewById(R.id.name);
            surname = (TextView) itemView.findViewById(R.id.surname);
        }
    }

    public PatientRecyclerAdapter(Context context, Fragment parent, ArrayList<Patient> patientsList) {
        _activityContext = context;
        _parentFragment = parent;
        _patientsList = patientsList;
    }

    @Override
    public PatientHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_cardview, parent, false);
        final PatientHolder patientHolder = new PatientHolder(view);
        view.findViewById(R.id.card_view).setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
                alertDialogBuilder.setTitle("Arrêter le suivi");
                alertDialogBuilder.setMessage("Voulez-vous vraiment arrêter le suivi de ce patient ?")
                        .setCancelable(false);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        _patientsList.remove(patientHolder.getAdapterPosition());
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
        view.findViewById(R.id.gps).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setTitle("Location");
                alertDialogBuilder.setMessage("Go to map ?")
                        .setCancelable(false);
                final AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OUI", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent  intent = new Intent(_activityContext, MapActivity.class);
                        _activityContext.startActivity(intent);
                    }
                });
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NON", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
                return;
            }
        });
        return patientHolder;

    }

    @Override
    public void onBindViewHolder(PatientHolder holder, int position) {
        holder.name.setText(_patientsList.get(position).getPrenom());
        holder.surname.setText(_patientsList.get(position).getNom());
        switch (_patientsList.get(position).getState()) {
            case OK:
                holder.itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#60E995"));
                holder.itemView.invalidate();
                return;
            case DANGER:
                holder.itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#EEA33A"));
                holder.itemView.invalidate();
                return;
            case ALERT:
                holder.itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#FB4A4A"));
                holder.itemView.invalidate();
                return;
            default:
                holder.itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#60E995"));
                holder.itemView.invalidate();
        }
    }

    @Override
    public int getItemCount() {
        return _patientsList.size();
    }
}
