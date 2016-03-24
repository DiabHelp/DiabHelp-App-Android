package fr.diabhelp.medecin_patient;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by sundava on 09/03/16.
 */
public class MedecinListRecyclerAdapter extends RecyclerView.Adapter<MedecinListRecyclerAdapter.MedecinInfoHolder>{

    private ArrayList<MedecinInfo> _medecinList;
    private String APIToken;

    public MedecinListRecyclerAdapter(String APIToken)
    {
        this.APIToken = APIToken;
        this._medecinList = getMedecins();
    }

    private ArrayList<MedecinInfo> getMedecins() {
        ArrayList<MedecinInfo> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        for (int i = 0; i < 3; i++)
            requests.add(new MedecinInfo("TestMedecinList " + i));

        /* END DEBUG BLOCK */

        return requests;
    }

    public static class MedecinInfoHolder extends RecyclerView.ViewHolder {
        TextView name;
        public MedecinInfoHolder(final View itemView)
        {
            super(itemView);
            Log.d("MedecinView", "Create MedecinInfoHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_medecin_name);
        }
    }

    @Override
    public MedecinInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_list_cardview_row, parent, false);
        MedecinInfoHolder medecinRequestHolder = new MedecinInfoHolder(view);
        return medecinRequestHolder;
    }

    @Override
    public void onBindViewHolder(MedecinInfoHolder holder, int position) {
        Log.d("MedecinView", "onBind MedecinInfoHolder");
        holder.name.setText(_medecinList.get(position).name);
    }

    @Override
    public int getItemCount() {
        return _medecinList.size();
    }

}
