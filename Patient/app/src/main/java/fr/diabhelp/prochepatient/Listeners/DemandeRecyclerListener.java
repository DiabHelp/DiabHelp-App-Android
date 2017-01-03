package fr.diabhelp.prochepatient.Listeners;

import fr.diabhelp.prochepatient.PatientRequest;
import fr.diabhelp.prochepatient.PatientRequestRecyclerAdapter;

/**
 * Created by 4kito on 02/09/2016.
 */
public interface DemandeRecyclerListener {
    void onClickAcceptPatient(PatientRequest request, int position, PatientRequestRecyclerAdapter.PatientRequestHolder holder);
    void onClickDenyPatient(PatientRequest request, int position, PatientRequestRecyclerAdapter.PatientRequestHolder holder);

}
