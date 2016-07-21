package fr.diabhelp.diabhelp.Utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

/**
 * Created by Sumbers on 13/10/2015.
 */
public class JsonUtils {

    public static JSONObject getObj(String serializable){
        try {
            return (JSONObject) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "Error json invalid = [" + serializable + "]");
            return null;
        }
    }

    public static JSONObject getObjFromObj(JSONObject obj, String key)
    {
        try {
            return obj.getJSONObject(key);

        } catch (Exception e) {
            Log.e("JsonUtils", "Error json invalid = [" + obj + "] key = " + key);
            return null;
        }
    }

    public static JSONArray getArray(String serializable)
    {
        try {
            return (JSONArray) new JSONTokener(serializable).nextValue();

        } catch (Exception e) {
            Log.e("JsonUtils", "Error json invalid = [" + serializable + "]");
            return null;
        }
    }

    public static JSONArray getArrayFromObj(JSONObject obj, String key)
    {
        try {
            return obj.getJSONArray(key);

        } catch (Exception e) {
            Log.e("JsonUtils", "Error json invalid = [" + obj + "] key = " + key);
            return null;
        }
    }

    public static String getStringFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getString(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error json invalid = [" + obj + "]");
            return null;
        }
    }

    public static Boolean getBoolFromKey(JSONObject obj, String key)
    {
        try {
            return obj.getBoolean(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error json invalid = [" + obj + "]");
            return null;
        }
    }

    public static Long getLongFromKey(JSONObject obj, String key)
    {
        try{
            return obj.getLong(key);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error json invalid = [" + obj + "]");
            return null;
        }
    }

    public static Integer getIntFromKey(JSONObject obj, String key)
    {
        try{
            return Integer.valueOf(obj.getInt(key));
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error json invalid = [" + obj + "]");
            return null;
        }
    }




    public static JSONObject getObjFromArray(JSONArray ar, int id)
    {
        try {
            return ar.getJSONObject(id);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error json invalid = [" + ar + "]");
            return null;
        }
    }

    public static String getStringFromArray(JSONArray ar, int id)
    {
        try {
            return ar.getString(id);
        }
        catch (Exception e)
        {
            Log.e("JsonUtils", "Error json invalid = [" + ar + "]");
            return null;
        }
    }
}
