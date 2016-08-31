package fr.diabhelp.proche;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fr.diabhelp.proche.Models.Patient;
import fr.diabhelp.proche.Utils.FieldUtils;
import fr.diabhelp.proche.Utils.SearchRecyclerListerner;

/**
 * Created by Rémi for Diabhelp
 * Started on 31 Aou 2016 at 15:27
 */
public class SearchRecyclerAdapter extends RecyclerView.Adapter<SearchRecyclerAdapter.PatientHolder> {
    public List<fr.diabhelp.proche.Models.Patient> patientsList;
    private static SearchRecyclerListerner itemListener;

    public List<Patient> getPatientsList() {
        return this.patientsList;
    }

    public static class PatientHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView avatar;
        TextView identity;
        TextView phone;
        Button add;

        public PatientHolder(final View itemView) {
            super(itemView);
            avatar = (ImageView) itemView.findViewById(R.id.logo);
            identity = (TextView) itemView.findViewById(R.id.identity);
            phone = (TextView) itemView.findViewById(R.id.phone);
            add = (Button) itemView.findViewById(R.id.add_button);
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemListener.onClickAddPatient(getLayoutPosition());

        }
    }

    public SearchRecyclerAdapter(List<Patient> patientsList, SearchRecyclerListerner listener) {
        this.patientsList = patientsList;
        itemListener = listener;

    }

    @Override
    public PatientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_cardview_item, parent, false);
        PatientHolder patientHolder = new PatientHolder(view);
        return patientHolder;
    }

    @Override
    public void onBindViewHolder(PatientHolder holder, int pos) {
        holder.identity.setText(patientsList.get(pos).getPrenom() + " " + patientsList.get(pos).getNom().toUpperCase());
        String phone = patientsList.get(pos).getTel();
        if (FieldUtils.isStringValid(phone, 10, FieldUtils.MATCH_REQUIRED))
            holder.phone.setText(phone.substring(0,2) + "******" + phone.substring(8,2));
        //TODO set l'image
    }

    @Override
    public int getItemCount() { return patientsList.size(); }

    public void clearData() {
        int size = patientsList.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                this.patientsList.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }
}
