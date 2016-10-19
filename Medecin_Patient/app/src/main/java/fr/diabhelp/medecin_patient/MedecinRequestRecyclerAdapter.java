package fr.diabhelp.medecin_patient;

import android.os.Handler;
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
import fr.diabhelp.medecin_patient.Listeners.MedecinRequestListener;

/**
 * Created by sundava on 09/03/16.
 */
public class MedecinRequestRecyclerAdapter extends RecyclerView.Adapter<MedecinRequestRecyclerAdapter.MedecinRequestHolder>  {

    private final RecyclerView view;
    private final MedecinRequestListener listener;

    private ArrayList<MedecinRequest> requestList;
    private String APIToken;

 /*   private final int REQUEST = 0;
    private final int ACCEPTED = 1;
    private final int REFUSED = 2;
    private final int WAITING = 3;
*/

    public MedecinRequestRecyclerAdapter(String APIToken, RecyclerView view, MedecinRequestListener listener, ArrayList<MedecinRequest> requestList ) {
        this.APIToken = APIToken;
        this.requestList = requestList;
        this.listener = listener;
        this.view = view;
    }

    private ArrayList<MedecinRequest> getRequests() {
        ArrayList<MedecinRequest> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        requests.add(new MedecinRequest("Dr Charin " , 1, MedecinRequest.State.WAITING));
        requests.add(new MedecinRequest("Dr Delat " , 0, MedecinRequest.State.PROCESSING));
        requests.add(new MedecinRequest("Dr Laffargue" , 2, MedecinRequest.State.ACCEPTED));
        requests.add(new MedecinRequest("Dr Merlot" , 3, MedecinRequest.State.REFUSED));

        /* END DEBUG BLOCK */

        return requests;
    }

    private int getRequestIndexById(int id)
    {
        for (int i = 0; i < requestList.size(); i++)
            if (requestList.get(i).getId() == id)
                return i;
        return -1;
    }

    public void setMedecinRequests(ArrayList<MedecinRequest> medecinRequests) {
        this.requestList = medecinRequests;
        notifyDataSetChanged();
    }


    public class MedecinRequestHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        MedecinRequest request;
        TextView name;
        ImageButton accept_btn;
        ImageButton refuse_btn;

        public MedecinRequestHolder(final View itemView) {
            super(itemView);
            //Log.d("MedecinView", "Create MedecinRequestHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_response_lbl);
            this.accept_btn = (ImageButton) itemView.findViewById(R.id.accept_request_btn);
            this.refuse_btn = (ImageButton) itemView.findViewById(R.id.refuse_request_btn);
            if (this.accept_btn != null && this.refuse_btn != null) {
                this.accept_btn.setOnClickListener(this);
                this.refuse_btn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            this.request.setState(MedecinRequest.State.PROCESSING);
            notifyItemChanged(this.getAdapterPosition());
            if (v == this.accept_btn){
                listener.onClickAcceptRequest(request, this.getAdapterPosition(), this);
            }
            else if (v == this.refuse_btn)
                listener.onClickDenyRequest(request, this.getAdapterPosition(), this);
        }

        public void removeItem(final int position)
        {
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    requestList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position,  requestList.size());
                    listener.setRecyclerViewHeight(view);
                }
            }, 2000);

        }

        public MedecinRequest getRequest() {
            return request;
        }

        public void changeRequestState(MedecinRequest.State state, int position)
        {
            this.request.setState(state);
            notifyItemChanged(position);
        }
    }

    @Override
    public MedecinRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        MedecinRequest.State state = MedecinRequest.State.getById(viewType);
        System.out.println("state = " + state.name());
        switch (state) {
            case ACCEPTED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_request_accepted_view, parent, false);
                break;
            case REFUSED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_request_refused_view, parent, false);
                break;
            case PROCESSING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_requestlist_waiting_view, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_request_cardview_row, parent, false);
                break;
        }
        MedecinRequestHolder medecinInfoHolder = new MedecinRequestHolder(view);
        return medecinInfoHolder;
    }

    @Override
    public void onBindViewHolder(final MedecinRequestHolder holder, final int position) {
        //Log.d("MedecinView", "onBind MedecinRequestHolder");
        holder.request = requestList.get(position);
        if (holder.request.getState() != MedecinRequest.State.WAITING) // Si la requête n'est pas a l'etat neutre (WAITING), pas besoin de bind
            return;
        holder.name.setText(holder.request.getName());

    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position).getState().ordinal();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

}
