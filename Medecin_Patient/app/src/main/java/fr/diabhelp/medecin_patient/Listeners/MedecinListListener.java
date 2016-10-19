package fr.diabhelp.medecin_patient.Listeners;


import android.support.v7.widget.RecyclerView;

import fr.diabhelp.medecin_patient.MedecinInfo;
import fr.diabhelp.medecin_patient.MedecinListRecyclerAdapter;
import fr.diabhelp.medecin_patient.MedecinRequest;
import fr.diabhelp.medecin_patient.MedecinRequestRecyclerAdapter;

/**
 * Created by 4kito on 02/09/2016.
 */
public interface MedecinListListener {
    void onDeleteMedecin(MedecinInfo medecin, int position, MedecinListRecyclerAdapter.MedecinInfoHolder holder);
    void setRecyclerViewHeight(RecyclerView view);

}
