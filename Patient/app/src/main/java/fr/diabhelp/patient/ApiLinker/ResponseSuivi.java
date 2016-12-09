package fr.diabhelp.patient.ApiLinker;

import android.location.Location;
import android.util.Log;
import fr.diabhelp.patient.Suivi.ExpandableListAdapter;
import fr.diabhelp.patient.Utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 4kito on 04/11/2016.
 */
public class ResponseSuivi {
    public String                                   error;
    public ArrayList<ExpandableListAdapter.Item>    patientsList = new ArrayList<>();

    public ResponseSuivi(String message) {
        this.error = message;
    }

    public ResponseSuivi(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                patientsList = new ArrayList<>();
                JSONArray   array = datas.getJSONArray("users");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item;
                    if ((item = array.getJSONObject(i).getJSONObject("proche")) != null) {
                        ExpandableListAdapter.Item patient = new ExpandableListAdapter.Item(ExpandableListAdapter.HEADER, 2, item.getString("firstname"), item.getString("lastname"), new Location(""), null);
                        patient.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.PHONE, null, null, null, "0632524154"));
                        patient.invisibleChildren.add(new ExpandableListAdapter.Item(ExpandableListAdapter.CHILD, ExpandableListAdapter.ALERT, null, null, null, null));
                        patientsList.add(patient);
                    }
                }

            }
            else
                this.error = ApiErrors.NO_USERS_FOUND.getServerMessage();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            this.error = ApiErrors.SERVER_ERROR.getServerMessage();
        }
    }

    public String getError() {
        return this.error;
    }
}
