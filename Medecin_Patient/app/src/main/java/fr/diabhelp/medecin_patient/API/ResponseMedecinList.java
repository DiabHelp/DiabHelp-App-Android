package fr.diabhelp.medecin_patient.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.medecin_patient.MedecinInfo;
import fr.diabhelp.medecin_patient.MedecinRequest;
import fr.diabhelp.medecin_patient.Utils.JsonUtils;

/**
 * Created by Sundava on 19/10/2016.
 */
public class ResponseMedecinList {
    public String error;
    public ArrayList<MedecinInfo> medecinInfos;


    public ResponseMedecinList(String message) {
        this.error = message;
    }

    public ResponseMedecinList(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                Log.d("MedecinListJSON","Success status OK");
                medecinInfos = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "medecins");
                for (int i = 0; i < arr.length(); i++)
                {
                    Log.d("MedecinListJSON","Loop in msg, i = " + i);
                    JSONObject jsonMessage = JsonUtils.getObjFromArray(arr, i);
                    String name =  JsonUtils.getStringFromKey(jsonMessage, "name");
                    int id =  JsonUtils.getIntFromKey(jsonMessage, "id");
                    MedecinInfo medecin = new MedecinInfo(name, id, MedecinInfo.State.PRESENT);
                    Log.d("MedecinListJSON","Loop in msg done");
                    medecinInfos.add(medecin);
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

    public ArrayList<MedecinInfo> getMedecinInfos() {
        return medecinInfos;
    }

    public void setMedecinInfos(ArrayList<MedecinInfo> medecinInfos) {
        this.medecinInfos = medecinInfos;
    }
}
