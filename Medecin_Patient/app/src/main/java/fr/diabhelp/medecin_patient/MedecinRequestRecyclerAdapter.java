package fr.diabhelp.medecin_patient;

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

/**
 * Created by sundava on 09/03/16.
 */
public class MedecinRequestRecyclerAdapter extends RecyclerView.Adapter<MedecinRequestRecyclerAdapter.MedecinRequestHolder> implements IApiCallTask {

    private final RecyclerView view;
    private final MedecinsFragment context;

    private ArrayList<MedecinRequest> _requestList;
    private String APIToken;

    private final int REQUEST = 0;
    private final int ACCEPTED = 1;
    private final int REFUSED = 2;
    private final int WAITING = 3;


    public MedecinRequestRecyclerAdapter(String APIToken, MedecinsFragment context, RecyclerView view) {
        this.APIToken = APIToken;
        this._requestList = getRequests();
        this.context = context;
        this.view = view;
    }

    private ArrayList<MedecinRequest> getRequests() {
        ArrayList<MedecinRequest> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        for (int i = 0; i < 8; i++)
            requests.add(new MedecinRequest("TestRequestMedecin " + i, i, REQUEST));

        /* END DEBUG BLOCK */

        return requests;
    }

    private int getRequestIndexById(int id)
    {
        for (int i = 0; i < _requestList.size(); i++)
            if (_requestList.get(i).getId() == id)
                return i;
        return -1;
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (s.startsWith("ERROR :")) {
            Log.d("MedecinRequest", "Could not connect to API (" + s + ")");
            onApiCallFailure();
            return;
        }
        Gson gson = new Gson();
        Log.d("MedecinRequest", "Api response : "  + s + " for action " + action);
        Map<String, String> response = gson.fromJson(s, Map.class);
        String status = response.get("status");
        if (status.equals("success") == false)
            onApiCallFailure();
        else {
            int id = Integer.parseInt(response.get("id"));
            int index = getRequestIndexById(id);
            String reqAction = response.get("action");
            if (reqAction.equals("ACCEPT"))
                _requestList.get(index).setState(ACCEPTED);
            else if (reqAction.equals("REFUSE"))
                _requestList.get(index).setState(REFUSED);
            else
                onApiCallFailure();
            Log.d("MedecinRequest", "Notify item removed for index : " + index + " (Name : " + _requestList.get(index).getName() + ")");
            notifyItemChanged(index);
            _requestList.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount());
            context.setRecyclerViewHeight(view);
        }
    }

    private void onApiCallFailure() {
        for (int i = 0; i < _requestList.size(); i++)
            if (_requestList.get(i).getState() == WAITING) {
                _requestList.get(i).setState(REQUEST);
                notifyItemChanged(i);
            }
    }


    public class MedecinRequestHolder extends RecyclerView.ViewHolder {
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
        }
    }

    @Override
    public MedecinRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ACCEPTED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_request_accepted_view, parent, false);
                break;
            case REFUSED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_request_refused_view, parent, false);
                break;
            case WAITING:
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
        holder.request = _requestList.get(position);
        if (holder.request.getState() != REQUEST) // Si la requête est en train d'être acceptée ou refusée, pas besoin de bind
            return;
        holder.name.setText(holder.request.getName());
        holder.accept_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MedecinRequest", "Accept for request : " + holder.request.getName() + " and position : " + position);
                holder.request.setState(WAITING);
                new ApiCallTask(MedecinRequestRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("2", "requestResponse", "id", String.valueOf(holder.request.getId()), "response", "ACCEPT");
                notifyItemChanged(position);

                /*
                // A appeller une fois que la requête API renvoie OK
              _requestList.remove(holder.request);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                */

            }
        });
        holder.refuse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MedecinRequest", "Refuse for request : " + holder.request.getName() + " and position : " + position);
                holder.request.setState(WAITING);
                new ApiCallTask(MedecinRequestRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("2", "requestResponse", "id", String.valueOf(holder.request.getId()), "response", "REFUSE");
                notifyItemChanged(position);
                    /*
                // A appeller une fois que la requête API renvoie OK
                _requestList.remove(holder.request);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount());
                 */

            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return _requestList.get(position).getState();
    }

    @Override
    public int getItemCount() {
        return _requestList.size();
    }

}
