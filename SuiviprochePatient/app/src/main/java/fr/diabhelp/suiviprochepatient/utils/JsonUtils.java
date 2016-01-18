package fr.diabhelp.suiviprochepatient.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Sumbers on 13/10/2015.
 */
public class JsonUtils {
    static public JSONObject get_obj(String serializable){
        System.out.println("retour du serveur = " + serializable);
        try {
            return (JSONObject) new JSONTokener(serializable).nextValue();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (null);
    }

    static public JSONArray get_array(String serializable)
    {
        try {
            return (JSONArray) new JSONTokener(serializable).nextValue();

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return (null);
    }

    static public String getStringfromKey(JSONObject obj, String key)
    {
        try {
            return obj.getString(key);
        }
        catch (JSONException e)
        {
            return (null);
        }
    }

    static public JSONObject getObjfromArray(JSONArray ar, int id)
    {
        try {
            return ar.getJSONObject(id);
        }
        catch (JSONException e)
        {
            return (null);
        }
    }
}
