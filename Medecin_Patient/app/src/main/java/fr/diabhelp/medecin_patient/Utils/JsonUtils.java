package fr.diabhelp.medecin_patient.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Sundava on 19/10/2016.
 */
public class JsonUtils {

    static public JSONObject getObj(String serializable){
        try {
            return (JSONObject) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "getObj : Error json invalid = [" + serializable + "]");
            return (null);
        }
    }

    static public JSONObject getObjFromObj(JSONObject obj, String key)
    {
        try {
            return (obj.getJSONObject(key));

        } catch (Exception e) {
            Log.e("JsonUtils", "getObjFromObj : Error json invalid = [" + obj.toString() + "] key = " + key);
            return (null);
        }
    }

    static public JSONArray getArray(String serializable)
    {
        try {
            return (JSONArray) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "getArray : Error json invalid = [" + serializable + "]");
            return (null);
        }
    }

    static public JSONArray getArrayFromObj(JSONObject obj, String key)
    {
        try {
            return (obj.getJSONArray(key));

        } catch (Exception e) {
            Log.e("JsonUtils", "getArrayFromObj : Error json invalid = [" + obj.toString() + "] key = " + key);
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
            Log.e("JsonUtils", "getStringFromKey : Error json invalid = [" + obj.toString() + "] key = " + key);
            return (null);
        }
    }

    static public long getLongFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getLong(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "getLongFromKey : Error json invalid = [" + obj.toString() + "] key = " + key);
            return (-1);
        }
    }


    static public int getIntFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getInt(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "getLongFromKey : Error json invalid = [" + obj.toString() + "] key = " + key);
            return (-1);
        }
    }

    static public Boolean getBoolFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getBoolean(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "getBoolFromKey : Error json invalid = [" + obj.toString() + "]");
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
            Log.e("JsonUtils", "getObjFromArray : Error json invalid = [" + ar.toString() + "]");
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
            Log.e("JsonUtils", "getStringFromArray : Error json invalid = [" + ar.toString() + "]");
            return (null);
        }
    }
}
