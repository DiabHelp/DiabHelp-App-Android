package fr.diabhelp.proche;

import android.support.v7.widget.RecyclerView;
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


//    private ArrayList<PatientRequest> getRequests() {
//        ArrayList<PatientRequest> requests = new ArrayList<>();
//
//        /* DEBUG BLOCK */
//
//        for (int i = 0; i < 8; i++)
//            requests.add(new PatientRequest("TestRequestPatient " + i, i, REQUEST));
//
//        /* END DEBUG BLOCK */
//
//        return requests;
//    }

//    private int getRequestIndexById(int id)
//    {
//        for (int i = 0; i < requestList.size(); i++)
//            if (requestList.get(i).getId() == id)
//                return i;
//        return -1;
//    }

//    @Override
//    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
//        if (s.startsWith("ERROR :")) {
//            Log.d("PatientRequest", "Could not connect to API (" + s + ")");
//            onApiCallFailure();
//            return;
//        }
//        Gson gson = new Gson();
//        Log.d("PatientRequest", "Api response : "  + s + " for action " + action);
//        Map<String, String> response = gson.fromJson(s, Map.class);
//        String status = response.get("status");
//        if (status.equals("success") == false)
//            onApiCallFailure();
//        else {
//            int id = Integer.parseInt(response.get("id"));
//            int index = getRequestIndexById(id);
//            String reqAction = response.get("action");
//            if (reqAction.equals("ACCEPT"))
//                requestList.get(index).setState(ACCEPTED);
//            else if (reqAction.equals("REFUSE"))
//                requestList.get(index).setState(REFUSED);
//            else
//                onApiCallFailure();
//            Log.d("PatientRequest", "Notify item removed for index : " + index + " (Name : " + requestList.get(index).getName() + ")");
//            notifyItemChanged(index);
//            requestList.remove(index);
//            notifyItemRemoved(index);
//            notifyItemRangeChanged(index, getItemCount());
//            //_context.setRecyclerViewHeight(_view);
//        }
//    }

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
            notifyItemChanged(this.getLayoutPosition());
            if (v == this.acceptBtn)
                listener.onClickAcceptPatient(request, this.getLayoutPosition(), this);
            else if (v == this.refuseBtn)
                listener.onClickDenyPatient(request, this.getLayoutPosition(), this);
            //TODO verifier que l'on rentre bien dans une condition


        }

        public PatientRequest getRequest() {
            return request;
        }

        public void changeRequestState(PatientRequest.State state)
        {
            this.request.setState(state);
            notifyItemChanged(this.getLayoutPosition());
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
        holder.name.setText(holder.request.getName());
//        holder.acceptBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("MedecinRequest", "Accept for request : " + holder.request.getName() + " and position : " + position);
//                holder.request.setState(WAITING);
//                new ApiCallTask(PatientRequestRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("2", "requestResponse", "id", String.valueOf(holder.request.getId()), "response", "ACCEPT");
//                notifyItemChanged(position);
//
//                /*
//                // A appeller une fois que la requête API renvoie OK
//              requestList.remove(holder.request);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, getItemCount());
//                */
//
//            }
//        });
//        holder.refuseBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("MedecinRequest", "Refuse for request : " + holder.request.getName() + " and position : " + position);
//                holder.request.setState(WAITING);
//                new ApiCallTask(PatientRequestRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("2", "requestResponse", "id", String.valueOf(holder.request.getId()), "response", "REFUSE");
//                notifyItemChanged(position);
//                    /*
//                // A appeller une fois que la requête API renvoie OK
//                requestList.remove(holder.request);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, getItemCount());
//                 */
//
//            }
//        });
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
    }
}
