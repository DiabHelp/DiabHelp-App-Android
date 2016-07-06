package fr.diabhelp.carnetdesuivi.API.Response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class ResponseCDSGetLastEdition {

    Carnetdesuivi.Error error = Carnetdesuivi.Error.NONE;

    Date lastEdition;

    public ResponseCDSGetLastEdition(){}

    public ResponseCDSGetLastEdition(JSONObject datas)
    {
        try
        {
            if (datas != null)
            {
                Boolean success = false;
                success = JsonUtils.getBoolFromKey(datas, "success");
                if (success != null)
                {
                    if (success == true) {
                        String lastEditionStr = datas.getJSONObject("dateEdition").getString("timestamp");
                        System.out.println("lastEdition serveur = " + lastEditionStr);
                        lastEdition = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastEditionStr);
                    }
                }
                else
                    throw new JSONException(datas.toString());
            }
            else
                throw new JSONException(datas.toString());

        }catch (JSONException e) {
                Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
                error = Carnetdesuivi.Error.SERVER_ERROR;

        } catch (ParseException e) {
                e.printStackTrace();
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            error = Carnetdesuivi.Error.SERVER_ERROR;
        }

    }

    public Date getLastEdition() {return this.lastEdition;}
    public Carnetdesuivi.Error getError() {
        return this.error;
    }

    public void setError(Carnetdesuivi.Error error) {
        this.error = error;
    }
}
