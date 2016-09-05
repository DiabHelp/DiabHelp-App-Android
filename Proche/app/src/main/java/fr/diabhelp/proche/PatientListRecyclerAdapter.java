package fr.diabhelp.proche;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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
public class PatientListRecyclerAdapter extends RecyclerView.Adapter<PatientListRecyclerAdapter.PatientInfoHolder> implements IApiCallTask {
    private final RecyclerView      _view;
    private final DemandesFragment  _context;
    private ArrayList<PatientInfo>  _patientsList;
    private String                  _APIToken;
    private final int               PRESENT = 0;
    private final int               REMOVED = 1;
    private final int               WAITING = 2;


    public PatientListRecyclerAdapter(String APIToken,  DemandesFragment context, RecyclerView view) {
        APIToken = APIToken;
        _patientsList = getPatients();
        _context = context;
        _view = view;

    }

    private ArrayList<PatientInfo> getPatients() {
        ArrayList<PatientInfo> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        for (int i = 0; i < 3; i++)
            requests.add(new PatientInfo("TestPatientList " + i, i + 5, PRESENT));

        /* END DEBUG BLOCK */

        return requests;
    }

    private void onApiCallFailure() {
        for (int i = 0; i < _patientsList.size(); i++)
            if (_patientsList.get(i).getState() == WAITING) {
                _patientsList.get(i).setState(PRESENT);
                notifyItemChanged(i);
            }
    }

    private int getMedecinIndexById(int id)
    {
        for (int i = 0; i < _patientsList.size(); i++)
            if (_patientsList.get(i).getId() == id)
                return i;
        return -1;
    }

    @Override
    public void onBackgroundTaskCompleted(String s, int type, String action) throws JSONException {
        if (s.startsWith("ERROR :")) {
            Log.d("MedecinList", "Could not connect to API (" + s + ")");
            onApiCallFailure();
            return;
        }
        Gson gson = new Gson();
        Log.d("MedecinList", "Api response : "  + s + " for action " + action);
        Map<String, String> response = gson.fromJson(s, Map.class);
        String status = response.get("status");
        if (status.equals("success") == false)
            onApiCallFailure();
        else {
            int id = Integer.parseInt(response.get("id"));
            int index = getMedecinIndexById(id);
            Log.d("PatientsList", "API Call success, removing item at index "  + index + " (Name : " + _patientsList.get(index).getName() + ")");
            _patientsList.get(index).setState(REMOVED);
            notifyItemChanged(index);
            _patientsList.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount());
            //_context.setRecyclerViewHeight(_view);
        }
    }

    public static class PatientInfoHolder extends RecyclerView.ViewHolder {
        TextView name;
        PatientInfo info;
        ImageButton remove_btn;

        public PatientInfoHolder(final View itemView) {
            super(itemView);
            //Log.d("PatientView", "Create PatientInfoHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_patient_name);
            this.remove_btn = (ImageButton) itemView.findViewById(R.id.remove_patient_btn);
        }
    }

    @Override
    public PatientInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case REMOVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_removed_view, parent, false);
                break;
            case WAITING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_requestlist_waiting_view, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_cardview_row, parent, false);
                break;
        }
        PatientInfoHolder patientRequestHolder = new PatientInfoHolder(view);
        return patientRequestHolder;
    }


    @Override
    public void onBindViewHolder(final PatientInfoHolder holder, final int position) {
        //Log.d("MedecinView", "onBind MedecinInfoHolder");
        holder.info = _patientsList.get(position);
        // Si le medecin est en train d'être remove, pas besoin de bind (IMPORTANT)
        if (holder.info.getState() != PRESENT)
            return;
        holder.name.setText(holder.info.getName());
        holder.remove_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MedecinList", "Dialog confirmation for medecin: " + holder.info.getName() + " and position : " + position);
                new AlertDialog.Builder(holder.itemView.getContext())
                        .setTitle("Supprimer le médecin")
                        .setMessage("Voulez vous vraiment retirer de médecin de vos médecins ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Log.d("MedecinList", "Remove YES for medecin: " + holder.info.getName() + " and position : " + position);
                                holder.info.setState(WAITING);
                                new ApiCallTask(PatientListRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("1", "removeMedecin", "id", String.valueOf(holder.info.getId()));
                                notifyItemChanged(position);
                                //_medecinList.remove(holder.info);
                                //notifyItemRemoved(position);
                                //notifyItemRangeChanged(position, getItemCount());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return _patientsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return _patientsList.get(position).getState();
    }

}
