package fr.diabhelp.prochepatient.ApiLinker;

import android.util.Log;


import fr.diabhelp.prochepatient.User;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sumbers on 30/08/2016.
 */
public class ResponseSearch {
    public String error;
    public ArrayList<User> users;


    public ResponseSearch(String message) {
        this.error = message;
    }

    public ResponseSearch(JSONObject datas) {
        try{
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null && success == true)
            {
                users = new ArrayList<>();
                JSONArray arr = JsonUtils.getArrayFromObj(datas, "users");
                for (int i = 0; i < arr.length(); i++)
                {
                    User user = new User();
                    JSONObject jsonUser = JsonUtils.getObjFromArray(arr, i);
                    user.setId(JsonUtils.getStringFromKey(jsonUser, "id"));
                    user.setNom(JsonUtils.getStringFromKey(jsonUser, "lastname"));
                    user.setPrenom(JsonUtils.getStringFromKey(jsonUser, "firstname"));
                    user.setMail(JsonUtils.getStringFromKey(jsonUser, "email"));
                    user.setTel(JsonUtils.getStringFromKey(jsonUser, "phone"));
                    user.setPhoto(JsonUtils.getStringFromKey(jsonUser, "profilePictureAbsolutePath"));
                    //TODO add coordonnées GPS
                    users.add(user);
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

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}
