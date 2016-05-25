package fr.diabhelp.diabhelp.Utils;

import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.Serializable;

/**
 * Created by Sumbers on 13/10/2015.
 */
public class JsonUtils {
    static public JSONObject getObj(String serializable){
        try {
            return ((JSONObject) new JSONTokener(serializable).nextValue());

        } catch (Exception e) {
            Log.e("JsonUtils", "getObj Error = [" + e.getMessage() + "] on " + serializable);
            return (null);
        }
    }

    static public JSONArray getArray(String serializable)
    {
        try {
            return (JSONArray) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "getArray Error = [" + e.getMessage() + "] on " + serializable);
            return (null);
        }
    }

    static public String getStringFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getString(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "getStringFromKey Error  = [" + e.getMessage() + "] on " + obj.toString());
            return (null);
        }
    }

    static public Boolean getBoolFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getBoolean(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", " getBoolFromKey Error  = [" + e.getMessage() + "] on " + obj.toString());
            return (null);
        }
    }




    static public JSONObject getObjFromArray(JSONArray ar, int id)
    {
        try {
            return ar.getJSONObject(id);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error Error  = [" + e.getMessage() + "] on " + ar.toString());
            return (null);
        }
    }

    static public String getStringFromArray(JSONArray ar, int id)
    {
        try {
            return ar.getString(id);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error Error  = [" + e.getMessage() + "] on " + ar.toString());
            return (null);
        }
    }
}
