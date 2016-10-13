package fr.diabhelp.proche;

import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import fr.diabhelp.proche.Listeners.DemandeRecyclerListener;

/**
 * Created by 4kito on 02/08/2016.
 */
public class PatientRequestRecyclerAdapter extends RecyclerView.Adapter<PatientRequestRecyclerAdapter.PatientRequestHolder>{
    private DemandeRecyclerListener listener;
    private ArrayList<PatientRequest>   requestList;
    private String  _APIToken;

    public PatientRequestRecyclerAdapter(String APIToken, DemandeRecyclerListener listener, ArrayList<PatientRequest> requestList) {
        _APIToken = APIToken;
        this.listener = listener;
        this.requestList = requestList;
    }

    public class PatientRequestHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        PatientRequest request;
        TextView name;
        ImageButton acceptBtn;
        ImageButton refuseBtn;

        public PatientRequestHolder(final View itemView) {
            super(itemView);
            //Log.d("PatientView", "Create MedecinRequestHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_response_lbl);
            this.acceptBtn = (ImageButton) itemView.findViewById(R.id.accept_request_btn);
            this.refuseBtn = (ImageButton) itemView.findViewById(R.id.refuse_request_btn);
            if (this.acceptBtn != null && this.refuseBtn != null) {
                this.acceptBtn.setOnClickListener(this);
                this.refuseBtn.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            this.request.setState(PatientRequest.State.PROCESSING);
            notifyItemChanged(this.getAdapterPosition());
            if (v == this.acceptBtn){
                listener.onClickAcceptPatient(request, this.getAdapterPosition(), this);
            }

            else if (v == this.refuseBtn)
                listener.onClickDenyPatient(request, this.getAdapterPosition(), this);
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
                }
            }, 2000);

        }

        public PatientRequest getRequest() {
            return request;
        }

        public void changeRequestState(PatientRequest.State state, int position)
        {
            this.request.setState(state);
            notifyItemChanged(position);
        }
    }

    @Override
    public PatientRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        PatientRequest.State state = PatientRequest.State.getById(viewType);
        System.out.println("state = " + state.name());
        switch (state) {
            case ACCEPTED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_request_accepted_view, parent, false);
                break;
            case REFUSED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_request_refused_view, parent, false);
                break;
            case PROCESSING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_requestlist_processing_view, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_request_cardview_row, parent, false);
                break;
        }
        PatientRequestHolder patientInfoHolder = new PatientRequestHolder(view);
        return patientInfoHolder;
    }

    @Override
    public void onBindViewHolder(final PatientRequestHolder holder, final int position) {
        holder.request = requestList.get(position);
        if (holder.request.getState() != PatientRequest.State.WAITING) // Si la requête est en train d'être acceptée ou refusée, pas besoin de bind
            return;
        Log.d("PatientRequest", "Bind ViewHolder, name = " + holder.request.getName());
        holder.name.setText(holder.request.getName());
    }


    public void setRequestsList(ArrayList<PatientRequest> patientsList) {
        this.requestList = patientsList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return requestList.get(position).getState().ordinal();
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public ArrayList<PatientRequest> getRequestList() {
        return requestList;
    }

    public void setRequestList(ArrayList<PatientRequest> requestList) {
        this.requestList = requestList;
        this.notifyDataSetChanged();
    }
}
