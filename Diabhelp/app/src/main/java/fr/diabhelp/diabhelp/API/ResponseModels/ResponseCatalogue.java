package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.Core.CatalogueFragment;
import fr.diabhelp.diabhelp.Models.CatalogModule;
import fr.diabhelp.diabhelp.Utils.JsonUtils;

/**
 * Created by Sumbers on 24/03/2016.
 */
public class ResponseCatalogue {

    CatalogueFragment.Error error = CatalogueFragment.Error.NONE;

    List<CatalogModule> modules;

    public ResponseCatalogue(){}


    public ResponseCatalogue(JSONArray arr) {
        try {
            modules = new ArrayList<CatalogModule>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonModule = JsonUtils.getObjFromArray(arr, i);
                CatalogModule module = new CatalogModule();
                module.setVersion(JsonUtils.getStringFromKey(jsonModule, "version"));
                module.setName(JsonUtils.getStringFromKey(jsonModule, "name"));
                module.setDesc(JsonUtils.getStringFromKey(jsonModule, "description"));
                module.setSize(JsonUtils.getStringFromKey(jsonModule, "size"));
                module.setRating(JsonUtils.getStringFromKey(jsonModule, "note"));
                module.setNew(Boolean.parseBoolean(JsonUtils.getStringFromKey(jsonModule, "isNew")));
                module.setMaker(JsonUtils.getStringFromKey(jsonModule, "organisme"));
                module.setType(JsonUtils.getStringFromKey(jsonModule, "type"));
                JSONArray coms = jsonModule.getJSONArray("comments");
                List<String> commentaires = new ArrayList<String>();
                for (int j = 0; j < coms.length(); j++) {
                    commentaires.add(JsonUtils.getStringFromArray(coms, j));
                }
                module.setCommentaires(commentaires);
                module.setURLStore(JsonUtils.getStringFromKey(jsonModule, "urlStore"));
                module.setURLWeb(JsonUtils.getStringFromKey(jsonModule, "urlSiteWeb"));
                //module.setLogo();
                modules.add(module);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + arr.toString() + "]");
        }
    }

    public List<CatalogModule> getModules() {return this.modules;}

    public void setModules(List<CatalogModule> modules) {this.modules = modules;}

    public CatalogueFragment.Error getError() {return this.error;}

    public void setError(CatalogueFragment.Error error) {this.error = error;}
}
