package fr.diabhelp.carnetdesuivi.API.Response;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.Utils.JsonUtils;

/**
 * Created by Sumbers on 29/03/2016.
 */
public class ResponseMail {

    private Carnetdesuivi.Error error = Carnetdesuivi.Error.NONE;

    public ResponseMail() {

    }

    public ResponseMail(JSONObject datas) {
        try {
            if (datas != null) {
                Boolean success = JsonUtils.getBoolFromKey(datas, "success");
                if (success == null || success == false) {
                    throw new JSONException(datas.toString());
//                    JSONArray errors = JsonUtils.getArray(obj, "");
//                    for (int i = 0; i < errors.length(); i++) {
//                        if (JsonUtils.getStringFromArray(errors, i).compareToIgnoreCase("") == 0) {
//                            error = Carnetdesuivi.Error.MAIL_NOT_SENT;
//                        } else if (JsonUtils.getStringFromArray(errors, i).compareToIgnoreCase("") == 0) {
//                            error = Carnetdesuivi.Error.SERVER_ERROR;
//                        } else if (JsonUtils.getStringFromArray(errors, i).compareToIgnoreCase("") == 0) {
//                            error = Carnetdesuivi.Error.INVALID_TOKEN;
//                        }
//                    }
                }
            }
            else
                throw new JSONException(datas.toString());
        } catch (JSONException e) {
            Log.e(getClass().getSimpleName(), "Error json invalid = [" + datas.toString() + "]");
            error = Carnetdesuivi.Error.SERVER_ERROR;
        }
    }



    public Carnetdesuivi.Error getError() {
        return error;
    }

    public void setError(Carnetdesuivi.Error error) {
        this.error = error;
    }
}
