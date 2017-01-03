package fr.diabhelp.prochepatient.ApiLinker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import fr.diabhelp.prochepatient.PatientRequest;
import fr.diabhelp.prochepatient.User;
import fr.diabhelp.prochepatient.Utils.JsonUtils;


/**
 * Created by Sumbers on 30/08/2016.
 */
public class ResponseUser {
    private String error;
    private User.Role role;


    public ResponseUser(String message) {
        this.error = message;
    }

    public ResponseUser(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success)
            {
                JSONObject user = JsonUtils.getObjFromObj(datas, "user");
                if (user != null)
                {
                    JSONArray arr = JsonUtils.getArrayFromObj(user, "roles");
                    if (arr != null)
                    {
                        for (int i = 0; i < arr.length(); i++)
                        {
                            for (User.Role r: User.Role.values())
                            {
                                if (r.getRole().compareToIgnoreCase(arr.getString(i)) == 0)
                                {
                                    this.role = r;
                                    break;
                                }
                            }
                        }
                    }
                    else
                    {
                        System.out.println("role is null !!");
                        this.error = ApiErrors.SERVER_ERROR.getServerMessage();
                    }

                }
                else
                {
                    System.out.println("user is null !!");
                    this.error = ApiErrors.SERVER_ERROR.getServerMessage();
                }
            }
            else
            {
                System.out.println("success is null !!");
                this.error = ApiErrors.SERVER_ERROR.getServerMessage();
            }
        }catch (Exception e){
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            this.error = ApiErrors.SERVER_ERROR.getServerMessage();
        }
    }

    public String getError() {
        return this.error;
    }

    public User.Role getRole() { return this.role; }

}
