package fr.diabhelp.medecin_patient;

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

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

import fr.diabhelp.medecin_patient.API.ApiCallTask;
import fr.diabhelp.medecin_patient.API.IApiCallTask;

/**
 * Created by sundava on 09/03/16.
 */
public class MedecinListRecyclerAdapter extends RecyclerView.Adapter<MedecinListRecyclerAdapter.MedecinInfoHolder> implements IApiCallTask {

    private final RecyclerView view;
    private final MedecinsFragment context;

    private ArrayList<MedecinInfo> _medecinList;
    private String APIToken;
    private final int PRESENT = 0;
    private final int REMOVED = 1;
    private final int WAITING = 2;


    public MedecinListRecyclerAdapter(String APIToken,  MedecinsFragment context, RecyclerView view) {
        this.APIToken = APIToken;
        this._medecinList = getMedecins();
        this.context = context;
        this.view = view;

    }

    private ArrayList<MedecinInfo> getMedecins() {
        ArrayList<MedecinInfo> requests = new ArrayList<>();

        /* DEBUG BLOCK */

        for (int i = 0; i < 3; i++)
            requests.add(new MedecinInfo("TestMedecinList " + i, i + 5, PRESENT));

        /* END DEBUG BLOCK */

        return requests;
    }

    private void onApiCallFailure() {
        for (int i = 0; i < _medecinList.size(); i++)
            if (_medecinList.get(i).getState() == WAITING) {
                _medecinList.get(i).setState(PRESENT);
                notifyItemChanged(i);
            }
    }

    private int getMedecinIndexById(int id)
    {
        for (int i = 0; i < _medecinList.size(); i++)
            if (_medecinList.get(i).getId() == id)
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
            Log.d("MedecinList", "API Call success, removing item at index "  + index + " (Name : " + _medecinList.get(index).getName() + ")");
            _medecinList.get(index).setState(REMOVED);
            notifyItemChanged(index);
            _medecinList.remove(index);
            notifyItemRemoved(index);
            notifyItemRangeChanged(index, getItemCount());
            context.setRecyclerViewHeight(view);
        }
    }

    public static class MedecinInfoHolder extends RecyclerView.ViewHolder {
        TextView name;
        MedecinInfo info;
        ImageButton remove_btn;

        public MedecinInfoHolder(final View itemView) {
            super(itemView);
            //Log.d("MedecinView", "Create MedecinInfoHolder");
            this.name = (TextView) itemView.findViewById(R.id.list_medecin_name);
            this.remove_btn = (ImageButton) itemView.findViewById(R.id.remove_medecin_btn);
        }
    }

    @Override
    public MedecinInfoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case REMOVED:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_list_removed_view, parent, false);
                break;
            case WAITING:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_requestlist_waiting_view, parent, false);
                break;
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medecin_list_cardview_row, parent, false);
            break;
        }
        MedecinInfoHolder medecinRequestHolder = new MedecinInfoHolder(view);
        return medecinRequestHolder;
    }

    @Override
    public void onBindViewHolder(final MedecinInfoHolder holder, final int position) {
        //Log.d("MedecinView", "onBind MedecinInfoHolder");
        holder.info = _medecinList.get(position);
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
                                new ApiCallTask(MedecinListRecyclerAdapter.this, ApiCallTask.POST, ApiCallTask.OBJECT, "MedecinPatient").execute("1", "removeMedecin", "id", String.valueOf(holder.info.getId()));
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
        return _medecinList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return _medecinList.get(position).getState();
    }

}
