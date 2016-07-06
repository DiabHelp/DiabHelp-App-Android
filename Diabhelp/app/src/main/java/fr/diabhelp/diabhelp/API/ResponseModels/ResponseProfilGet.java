package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import fr.diabhelp.diabhelp.Core.CatalogueFragment;
import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.Models.CatalogModule;
import fr.diabhelp.diabhelp.Utils.JsonUtils;

/**
 * Created by naqued on 05/07/16.
 */
public class ResponseProfilGet {

    String lastname;
    String birthdate;
    String mobile;
    String organism;
    String email ;
    String firstname;

    private ProfileActivity.Error error = ProfileActivity.Error.NONE;

    public String getEmail() {
        return this.email;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() {
        return this.lastname;
    }

    public String getBirthdate() {
        return this.birthdate;
    }

    public String getMobile() {
        return this.mobile;
    }

    public String getOrganism() {
        return this.organism;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setOrganism(String organism) {
        this.organism = organism;
    }

    private List<CatalogModule> modules;

    public ResponseProfilGet(){}


    public ResponseProfilGet(JSONObject datas) {
        try {
            if (datas != null)
            {
                Boolean success = false;
                success = JsonUtils.getBoolFromKey(datas, "success");
                if (success != null)
                {
                    if (success == true)
                    {
                        JSONObject user = JsonUtils.getObjFromObj(datas, "user");
                        email = JsonUtils.getStringFromKey(user, "email");
                        firstname = JsonUtils.getStringFromKey(user, "firstname");
                        lastname = JsonUtils.getStringFromKey(user, "lastname");
                        mobile = JsonUtils.getStringFromKey(user, "phone");
                        birthdate = JsonUtils.getStringFromKey(user, "birthdate");
                        organism = JsonUtils.getStringFromKey(user, "organisme");
                    }
                    else
                        throw new Exception();
                }
                else
                    throw new Exception();
            }
            else
                throw new Exception();
            }catch (Exception e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            error = ProfileActivity.Error.SERVER_ERROR;
        }

    }

    public ProfileActivity.Error getError() {return this.error;}

    public void setError(ProfileActivity.Error error) {this.error = error;}
}

