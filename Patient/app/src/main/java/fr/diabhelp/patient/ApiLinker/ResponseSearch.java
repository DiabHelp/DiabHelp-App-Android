package fr.diabhelp.patient.ApiLinker;

import android.util.Log;


import fr.diabhelp.patient.Patient;
import fr.diabhelp.patient.Utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumbers on 30/08/2016.
 */
public class ResponseSearch {
    public String error;
    public ArrayList<Patient> patients;


    public ResponseSearch(String message) {
        this.error = message;
    }

    public ResponseSearch(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success == true)
            {
                patients = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "users");
                for (int i = 0; i < arr.length(); i++)
                {
                    Patient patient = new Patient();
                    JSONObject jsonUser = JsonUtils.getObjFromArray(arr, i);
                    patient.setId(JsonUtils.getStringFromKey(jsonUser, "id"));
                    patient.setNom(JsonUtils.getStringFromKey(jsonUser, "lastname"));
                    patient.setPrenom(JsonUtils.getStringFromKey(jsonUser, "firstname"));
                    patient.setMail(JsonUtils.getStringFromKey(jsonUser, "email"));
                    patient.setTel(JsonUtils.getStringFromKey(jsonUser, "phone"));
                    patient.setPhoto(JsonUtils.getStringFromKey(jsonUser, "profilePictureAbsolutePath"));
                    //TODO add coordonnées GPS
                    patients.add(patient);
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

    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }
}
