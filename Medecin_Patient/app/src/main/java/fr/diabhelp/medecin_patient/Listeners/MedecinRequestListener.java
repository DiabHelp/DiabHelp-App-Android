package fr.diabhelp.medecin_patient.Listeners;


import android.support.v7.widget.RecyclerView;

import fr.diabhelp.medecin_patient.MedecinRequest;
import fr.diabhelp.medecin_patient.MedecinRequestRecyclerAdapter;

/**
 * Created by 4kito on 02/09/2016.
 */
public interface MedecinRequestListener {
    void onClickAcceptRequest(MedecinRequest request, int position, MedecinRequestRecyclerAdapter.MedecinRequestHolder holder);
    void onClickDenyRequest(MedecinRequest request, int position, MedecinRequestRecyclerAdapter.MedecinRequestHolder holder);
    void setRecyclerViewHeight(RecyclerView view);
}
