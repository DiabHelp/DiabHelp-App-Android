package fr.diabhelp.medecin_patient.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.medecin_patient.MedecinRequest;
import fr.diabhelp.medecin_patient.Utils.JsonUtils;

/**
 * Created by Sundava on 19/10/2016.
 */
public class ResponseRequest {
    public String error;
    public ArrayList<MedecinRequest> medecinRequests;


    public ResponseRequest(String message) {
        this.error = message;
    }

    public ResponseRequest(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                Log.d("MedecinRequestJSON","Success status OK");
                medecinRequests = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "requests");
                for (int i = 0; i < arr.length(); i++)
                {
                    Log.d("MedecinRequestJSON","Loop in msg, i = " + i);
                    JSONObject jsonMessage = JsonUtils.getObjFromArray(arr, i);
                    String name =  JsonUtils.getStringFromKey(jsonMessage, "name");
                    int id =  JsonUtils.getIntFromKey(jsonMessage, "id");
                    MedecinRequest message = new MedecinRequest(name, id, MedecinRequest.State.WAITING);
                    Log.d("MedecinRequestJSON","Loop in msg done");
                    medecinRequests.add(message);
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

    public ArrayList<MedecinRequest> getmedecinRequests() {
        return medecinRequests;
    }

    public void setmedecinRequests(ArrayList<MedecinRequest> medecinRequests) {
        this.medecinRequests = medecinRequests;
    }
}
