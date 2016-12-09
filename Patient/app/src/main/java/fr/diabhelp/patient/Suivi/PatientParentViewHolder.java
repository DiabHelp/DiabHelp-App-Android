package fr.diabhelp.patient.Suivi;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import fr.diabhelp.patient.R;

/**
 * Created by 4kito on 19/10/2016.
 */
public class PatientParentViewHolder extends ParentViewHolder {
    private ImageView   logo;
    private TextView    name;
    private TextView    surname;
    private ImageButton gps;

    public PatientParentViewHolder(@NonNull View itemView) {
        super(itemView);
        logo = (ImageView) itemView.findViewById(R.id.logo);
        name = (TextView) itemView.findViewById(R.id.name);
        surname = (TextView) itemView.findViewById(R.id.surname);
        gps = (ImageButton) itemView.findViewById(R.id.gps);
    }

    public void bind(@NonNull PatientParent patientParent) {
        name.setText(patientParent.getName());
        surname.setText(patientParent.getSurname());
        switch (patientParent.getState()) {
            case OK:
                itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#60E995"));
                itemView.invalidate();
                return;
            case DANGER:
                itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#EEA33A"));
                itemView.invalidate();
                return;
            case ALERT:
                itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#FB4A4A"));
                itemView.invalidate();
                return;
            default:
                itemView.findViewById(R.id.bglayout).setBackgroundColor(Color.parseColor("#60E995"));
                itemView.invalidate();
        }
    }
}
