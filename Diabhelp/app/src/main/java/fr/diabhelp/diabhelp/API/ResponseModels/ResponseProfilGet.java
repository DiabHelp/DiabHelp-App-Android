package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.Models.CatalogModule;
import fr.diabhelp.diabhelp.Utils.DateUtils;
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
        return email;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOrganism() {
        return organism;
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
                        this.email = JsonUtils.getStringFromKey(user, "email");
                        this.firstname = JsonUtils.getStringFromKey(user, "firstname");
                        this.lastname = JsonUtils.getStringFromKey(user, "lastname");
                        this.mobile = JsonUtils.getStringFromKey(user, "phone");
                        System.out.println("tout va bien");
                        Long birth = JsonUtils.getLongFromKey(user, "birthdate");
                        System.out.println("birth = " + birth);
                        SimpleDateFormat sf = new SimpleDateFormat(DateUtils.DATE_PATERN);
                        Date d = new Date(birth * 1000L);
                        this.birthdate = sf.format(d);
                        this.organism = JsonUtils.getStringFromKey(user, "organisme");
                    }
                    else
                        throw new Exception();
                }
                else
                    throw new Exception();
            }
            else
                throw new Exception();
            }catch (Exception e)
        {
            Log.e(this.getClass().getSimpleName(), "Error json invalid = [" + datas + "]");
            e.printStackTrace();
            this.error = ProfileActivity.Error.SERVER_ERROR;
        }

    }

    public ProfileActivity.Error getError() {return error;}

    public void setError(ProfileActivity.Error error) {this.error = error;}
}

