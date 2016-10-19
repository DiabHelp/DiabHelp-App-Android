package fr.diabhelp.medecin_patient;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import fr.diabhelp.medecin_patient.API.ApiCallTask;
import fr.diabhelp.medecin_patient.API.IApiCallTask;
import fr.diabhelp.medecin_patient.Listeners.MedecinListListener;

/**
 * Created by sundava on 09/03/16.
 */
public class MedecinListRecyclerAdapter extends RecyclerView.Adapter<MedecinListRecyclerAdapter.MedecinInfoHolder>  {

    private final RecyclerView view;
    private final MedecinListListener listener;

    private ArrayList<MedecinInfo> medecinList;
    private String APIToken;

    public MedecinListRecyclerAdapter(String APIToken, RecyclerView view, MedecinListListener listener, ArrayList<MedecinInfo> medecinList ) {
        this.APIToken = APIToken;
        this.medecinList = medecinList;
        this.listener = listener;
        this.view = view;
    }

    public void setMedecinList(ArrayList<MedecinInfo> medecinInfos) {
        this.medecinList = medecinInfos;
        notifyDataSetChanged();
    }
    public class MedecinInfoHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        TextView name;
        MedecinInfo info;
        ImageButton remove_btn;

        public MedecinInfoHolder(final View itemView) {
            super(itemView);
            //Log.d("MedecinView", "Create MedecinInfoHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_medecin_name);
            this.remove_btn = (ImageButton) itemView.findViewById(R.id.remove_medecin_btn);
            if (this.remove_btn != null)
                this.remove_btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.info.setState(MedecinInfo.State.PROCESSING);
            notifyItemChanged(this.getAdapterPosition());
            if (v == this.remove_btn)
                listener.onDeleteMedecin(info, this.getAdapterPosition(), this);
        }

        public void removeItem(final int position)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    medecinList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,  medecinList.size());
                    listener.setRecyclerViewHeight(view);

                }
            }, 2000);

        }

        public MedecinInfo getInfo() {
            return info;
        }

        public void changeRequestState(MedecinInfo.State state, int position)
        {
            this.info.setState(state);
            notifyItemChanged(position);
        }
    }

    @Override
    public MedecinInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MedecinInfo.State state = MedecinInfo.State.getById(viewType);
        System.out.println("state = " + state.name());
        switch (state) {
            case REMOVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_list_removed_view, parent, false);
                break;
            case PROCESSING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_requestlist_waiting_view, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_list_cardview_row, parent, false);
            break;
        }
        MedecinInfoHolder medecinInfoHolder = new MedecinInfoHolder(view);
        return medecinInfoHolder;
    }

    @Override
    public void onBindViewHolder(final MedecinInfoHolder holder, final int position) {
        //Log.d("MedecinView", "onBind MedecinInfoHolder");
        holder.info = medecinList.get(position);
        // Si la requête n'est pas a l'etat neutre (PRESENT), pas besoin de bind
        if (holder.info.getState() != MedecinInfo.State.PRESENT)
            return;
        holder.name.setText(holder.info.getName());
    }

    @Override
    public int getItemCount() {
        return medecinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return medecinList.get(position).getState().ordinal();
    }
}
