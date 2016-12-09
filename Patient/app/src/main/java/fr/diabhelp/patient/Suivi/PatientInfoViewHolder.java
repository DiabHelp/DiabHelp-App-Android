package fr.diabhelp.patient.Suivi;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;
import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import fr.diabhelp.patient.R;

/**
 * Created by 4kito on 19/10/2016.
 */
public class PatientInfoViewHolder extends ChildViewHolder {
    private TextView    _phone;

    public PatientInfoViewHolder(@NonNull View itemView) {
        super(itemView);
        _phone = (TextView) itemView.findViewById(R.id.phone);
    }

    public void bind(@NonNull PatientInfo patientInfo) {
        _phone.setText(patientInfo.getPhone());
    }
}
