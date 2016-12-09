package fr.diabhelp.patient.Listeners;

import fr.diabhelp.patient.PatientRequest;
import fr.diabhelp.patient.PatientRequestRecyclerAdapter;

/**
 * Created by 4kito on 02/09/2016.
 */
public interface DemandeRecyclerListener {
    void onClickAcceptPatient(PatientRequest request, int position, PatientRequestRecyclerAdapter.PatientRequestHolder holder);
    void onClickDenyPatient(PatientRequest request, int position, PatientRequestRecyclerAdapter.PatientRequestHolder holder);

}
