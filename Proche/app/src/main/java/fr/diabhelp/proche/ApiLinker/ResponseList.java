package fr.diabhelp.proche.ApiLinker;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.proche.Patient;
import fr.diabhelp.proche.PatientRequest;
import fr.diabhelp.proche.Utils.JsonUtils;

/**
 * Created by Sumbers on 30/08/2016.
 */
public class ResponseList {
    private String error;
    private ArrayList<Patient> patientsList;


    public ResponseList(String message) {
        this.error = message;
    }

    public ResponseList(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                patientsList = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "users");
                for (int i = 0; i < arr.length(); i++)
                {
                    JSONObject obj;
                    if ((obj = JsonUtils.getObjFromArray(arr, i)) != null)
                    {
                        if (obj.has("patient")){
                            JSONObject patient = JsonUtils.getObjFromObj(obj, "patient");
                            String firstName = JsonUtils.getStringFromKey(patient, "firstname");
                            String lastName = JsonUtils.getStringFromKey(patient, "lastname");
                            Patient.State state = Patient.State.OK;
                            patientsList.add(new Patient(firstName, lastName, null, state));
                        }
                        else
                            continue;
                    }
                    else
                        throw new Exception("");
                }
            }
            else {
               throw new Exception("");
            }
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "] reason = " + e.getMessage());
            this.error = ApiErrors.SERVER_ERROR.getServerMessage();
        }
    }

    public String getError() {
        return this.error;
    }

    public ArrayList<Patient> getPatientsList() {
        return patientsList;
    }

    public void setListPatients(ArrayList<Patient> listPatients) {
        this.patientsList = listPatients;
    }
}
