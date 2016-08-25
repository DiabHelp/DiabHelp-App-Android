package fr.diabhelp.proche;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.gson.Gson;
import fr.diabhelp.proche.API.ApiCallTask;
import fr.diabhelp.proche.API.IApiCallTask;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 4kito on 02/08/2016.
 */
public class PatientRequestRecyclerAdapter extends RecyclerView.Adapter<PatientRequestRecyclerAdapter.PatientRequestHolder> implements IApiCallTask {
    private final RecyclerView          _view;
    private final DemandesFragment      _context;
    private ArrayList<PatientRequest>   _requestList;
    private String                      _APIToken;
    private final int                   REQUEST = 0;
    private final int                   ACCEPTED = 1;
    private final int                   REFUSED = 2;
    private final int                   WAITING = 3;

    public PatientRequestRecyclerAdapter(String APIToken, DemandesFragment context, RecyclerView view) {
        _APIToken = APIToken;
        _requestList = getRequests();
        _context = context;
        this._view = view;
    }

    private ArrayList<PatientRequest> getRequests() {
        ArrayList<PatientRequest> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        for (int i = 0; i < 8; i++)
            requests.add(new PatientRequest("TestRequestPatient " + i, i, REQUEST));

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
            Log.d("PatientRequest", "Could not connect to API (" + s + ")");
            onApiCallFailure();
            return;
        }
        Gson gson = new Gson();
        Log.d("PatientRequest", "Api response : "  + s + " for action " + action);
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
            Log.d("PatientRequest", "Notify item removed for index : " + index + " (Name : " + _requestList.get(index).getName() + ")");
            notifyItemChanged(index);
            _requestList.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount());
            _context.setRecyclerViewHeight(_view);
        }
    }

    private void onApiCallFailure() {
        for (int i = 0; i < _requestList.size(); i++)
            if (_requestList.get(i).getState() == WAITING) {
                _requestList.get(i).setState(REQUEST);
                notifyItemChanged(i);
            }
    }


    public class PatientRequestHolder extends RecyclerView.ViewHolder {
        PatientRequest request;
        TextView name;
        ImageButton accept_btn;
        ImageButton refuse_btn;

        public PatientRequestHolder(final View itemView) {
            super(itemView);
            //Log.d("PatientView", "Create MedecinRequestHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_response_lbl);
            this.accept_btn = (ImageButton) itemView.findViewById(R.id.accept_request_btn);
            this.refuse_btn = (ImageButton) itemView.findViewById(R.id.refuse_request_btn);
        }
    }

    @Override
    public PatientRequestHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ACCEPTED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_request_accepted_view, parent, false);
                break;
            case REFUSED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_request_refused_view, parent, false);
                break;
            case WAITING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_requestlist_waiting_view, parent, false);
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
                new ApiCallTask(PatientRequestRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("2", "requestResponse", "id", String.valueOf(holder.request.getId()), "response", "ACCEPT");
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
                new ApiCallTask(PatientRequestRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("2", "requestResponse", "id", String.valueOf(holder.request.getId()), "response", "REFUSE");
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
