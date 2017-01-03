package fr.diabhelp.prochepatient.ApiLinker;

import android.location.Location;
import android.util.Log;
import fr.diabhelp.prochepatient.Suivi.ExpandableListAdapter;
import fr.diabhelp.prochepatient.Suivi.ExpandableListAdapter.Item;
import fr.diabhelp.prochepatient.User;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by 4kito on 04/11/2016.
 */
public class ResponseSuivi {
    private String                                error;
    private ArrayList<ExpandableListAdapter.Item> userList = new ArrayList<>();

    public ResponseSuivi(String message) {
        this.error = message;
    }

    public ResponseSuivi(JSONObject datas, User.Role role) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                userList = new ArrayList<>();
                JSONArray   array = datas.getJSONArray("users");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item;
                    if (role.equals(User.Role.PATIENT)) {
                        if ((item = array.getJSONObject(i).getJSONObject("proche")) != null) {
                            ExpandableListAdapter.Item patient = new ExpandableListAdapter.Item(JsonUtils.getStringFromKey(item, "id"), ExpandableListAdapter.HEADER, 2, item.getString("firstname"), item.getString("lastname"), new Location(""), null);
                            patient.getInvisibleChildren().add(new ExpandableListAdapter.Item(null, ExpandableListAdapter.CHILD, ExpandableListAdapter.PHONE, null, null, null, JsonUtils.getStringFromKey(item, "phone")));
                            //                        patient.invisibleChildren.add(new ExpandableListAdapter.Item(null, ExpandableListAdapter.CHILD, ExpandableListAdapter.ALERT, null, null, null, null));
                            userList.add(patient);
                        }
                    }
                    else if (role.equals(User.Role.PROCHE)) {
                        if ((item = array.getJSONObject(i).getJSONObject("patient")) != null) {
                            ExpandableListAdapter.Item patient = new ExpandableListAdapter.Item(JsonUtils.getStringFromKey(item, "id"), ExpandableListAdapter.HEADER, 2, item.getString("firstname"), item.getString("lastname"), new Location(""), null);
                            patient.getInvisibleChildren().add(new ExpandableListAdapter.Item(null, ExpandableListAdapter.CHILD, ExpandableListAdapter.PHONE, null, null, null, JsonUtils.getStringFromKey(item, "phone")));
                            //                        patient.invisibleChildren.add(new ExpandableListAdapter.Item(null, ExpandableListAdapter.CHILD, ExpandableListAdapter.ALERT, null, null, null, null));
                            userList.add(patient);
                        }
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

    public ArrayList<Item> getUserList() {
        return this.userList;
    }
}
