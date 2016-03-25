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

    CatalogueFragment.Error error;

    List<CatalogModule> modules;

    public ResponseCatalogue(){}


    public ResponseCatalogue(JSONObject obj) {
        try {
            //TODO recuperer les erreurs et les bons noms de variables json
            modules = new ArrayList<CatalogModule>();
            JSONArray arr = obj.getJSONArray("modules");
            for (int i = 0; i < arr.length(); i++) {
                JSONObject jsonModule = JsonUtils.getObjFromArray(arr, i);
                CatalogModule module = new CatalogModule();
                module.setVersion(JsonUtils.getStringFromKey(jsonModule, ""));
                module.setName(JsonUtils.getStringFromKey(jsonModule, ""));
                module.setDesc(JsonUtils.getStringFromKey(jsonModule, ""));
                module.setSize(JsonUtils.getStringFromKey(jsonModule, ""));
                module.setRating(JsonUtils.getStringFromKey(jsonModule, ""));
                module.setNew(JsonUtils.getBoolFromKey(jsonModule, ""));
                module.setMaker(JsonUtils.getStringFromKey(jsonModule, ""));
                JSONArray coms = jsonModule.getJSONArray("commentaires");
                List<String> commentaires = new ArrayList<String>();
                for (int j = 0; j < coms.length(); j++) {
                    commentaires.add(JsonUtils.getStringFromArray(coms, j));
                }
                module.setCommentaires(commentaires);
                module.setURLStore(JsonUtils.getStringFromKey(jsonModule, ""));
                module.setURLWeb(JsonUtils.getStringFromKey(jsonModule, ""));
                //module.setLogo();
                modules.add(module);
            }
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + obj.toString() + "]");
        }
    }

    public List<CatalogModule> getModules() {return this.modules;}

    public void setModules(List<CatalogModule> modules) {this.modules = modules;}

    public CatalogueFragment.Error getError() {return this.error;}

    public void setError(CatalogueFragment.Error error) {this.error = error;}
}
