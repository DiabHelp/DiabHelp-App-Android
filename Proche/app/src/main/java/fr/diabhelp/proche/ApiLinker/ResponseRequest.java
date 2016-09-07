package fr.diabhelp.proche.ApiLinker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.proche.PatientRequest;
import fr.diabhelp.proche.Utils.JsonUtils;

/**
 * Created by Sumbers on 30/08/2016.
 */
public class ResponseRequest {
    public String error;
    public ArrayList<PatientRequest> patientRequests;


    public ResponseRequest(String message) {
        this.error = message;
    }

    public ResponseRequest(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                patientRequests = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "users");
                for (int i = 0; i < arr.length(); i++)
                {
                    PatientRequest request = new PatientRequest();
                    JSONObject jsonUser = JsonUtils.getObjFromArray(arr, i);
                    JSONObject patient = JsonUtils.getObjFromObj(jsonUser, "proche");
                    request.setId(JsonUtils.getStringFromKey(patient,"id"));
                    String lastname = JsonUtils.getStringFromKey(patient, "lastname");
                    String firstname = JsonUtils.getStringFromKey(patient, "firstname");
                    request.setName(firstname.concat(" " + lastname.toUpperCase()));
                    request.setState(PatientRequest.State.WAITING);
                    patientRequests.add(request);
                }
            }
            else
                this.error = ApiErrors.NO_USERS_FOUND.getServerMessage();
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            this.error = ApiErrors.SERVER_ERROR.getServerMessage();
        }
    }

    public String getError() {
        return this.error;
    }

    public ArrayList<PatientRequest> getRequests() {
        return patientRequests;
    }

    public void setPatientRequests(ArrayList<PatientRequest> patientRequests) {
        this.patientRequests = patientRequests;
    }
}
