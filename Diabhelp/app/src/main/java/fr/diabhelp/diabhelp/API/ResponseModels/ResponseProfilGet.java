package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.Core.CatalogueFragment;
import fr.diabhelp.diabhelp.Menu.ProfileActivity;
import fr.diabhelp.diabhelp.Models.CatalogModule;
import fr.diabhelp.diabhelp.Utils.JsonUtils;

/**
 * Created by naqued on 05/07/16.
 */
public class ResponseProfilGet {


    private ProfileActivity.Error error = ProfileActivity.Error.NONE;


    private List<CatalogModule> modules;

    public ResponseProfilGet(){}


    public ResponseProfilGet(JSONObject data) {


/*        try {
            String toto = data.getString("email");
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + arr.toString() + "]");
            error = ProfileActivity.Error.SERVER_ERROR;
        }*/
    }

    public List<CatalogModule> getModules() {return this.modules;}

    public void setModules(List<CatalogModule> modules) {this.modules = modules;}

    public ProfileActivity.Error getError() {return this.error;}

    public void setError(ProfileActivity.Error error) {this.error = error;}
}

