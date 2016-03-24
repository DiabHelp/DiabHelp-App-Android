package fr.diabhelp.medecin_patient;

import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sundava on 09/03/16.
 */
public class MedecinRequestRecyclerAdapter extends RecyclerView.Adapter<MedecinRequestRecyclerAdapter.MedecinRequestHolder>{

    private ArrayList<MedecinRequest> _requestList;
    private String APIToken;

    public MedecinRequestRecyclerAdapter(String APIToken)
    {
        this.APIToken = APIToken;
        this._requestList = getRequests();
    }

    private ArrayList<MedecinRequest> getRequests() {
        ArrayList<MedecinRequest> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        for (int i = 0; i < 8; i++)
            requests.add(new MedecinRequest("TestRequestMedecin " + i));

        /* END DEBUG BLOCK */

        return requests;
    }

    public static class MedecinRequestHolder extends RecyclerView.ViewHolder {
        TextView name;
        public MedecinRequestHolder(final View itemView)
        {
            super(itemView);
            Log.d("MedecinView", "Create MedecinRequestHolder");
            this.name = (TextView)itemView.findViewById(R.id.request_medecin_name);
        }
    }

    @Override
    public MedecinRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_request_cardview_row, parent, false);
        MedecinRequestHolder medecinInfoHolder = new MedecinRequestHolder(view);
        return medecinInfoHolder;
    }

    @Override
    public void onBindViewHolder(MedecinRequestHolder holder, int position) {
        Log.d("MedecinView", "onBind MedecinRequestHolder");
        holder.name.setText(_requestList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return _requestList.size();
    }

}
