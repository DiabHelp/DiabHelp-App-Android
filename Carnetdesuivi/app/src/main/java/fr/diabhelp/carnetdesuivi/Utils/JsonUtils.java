package fr.diabhelp.carnetdesuivi.Utils;

import android.util.Log;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Sumbers on 29/03/2016.
 */
public class JsonUtils {

    static public JSONObject getObj(String serializable){
        try {
            return (JSONObject) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "Error json invalid = [" + serializable + "]");
            return (null);
        }
    }

    static public JSONArray getArray(String serializable)
    {
        try {
            return (JSONArray) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "Error json invalid = [" + serializable + "]");
            return (null);
        }
    }

    static public JSONArray getArray(JSONObject obj, String key)
    {
        try {
            return obj.getJSONArray(key);
        } catch (Exception e) {
            Log.e("JsonUtils", "Error array not found with key = [" + key + "\nobject = [" + obj + "]");
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
            Log.e("JsonUtils", "Error json invalid = [" + obj.toString() + "]");
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
            Log.e("JsonUtils", "Error json invalid = [" + obj.toString() + "]");
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
            Log.e("JsonUtils", "Error json invalid = [" + ar.toString() + "]");
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
            Log.e("JsonUtils", "Error String not found at pos = [" + id +"\narray = [" + ar.toString() + "]");
            return (null);
        }
    }
}
