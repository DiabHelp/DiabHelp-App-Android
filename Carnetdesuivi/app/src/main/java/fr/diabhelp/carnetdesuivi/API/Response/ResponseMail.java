package fr.diabhelp.carnetdesuivi.API.Response;

import org.json.JSONArray;
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

    public ResponseMail(JSONObject obj) {
        if (obj != null){
                String status = JsonUtils.getStringFromKey(obj, "");
                if (status.compareToIgnoreCase("error") == 0)
                {
                    JSONArray errors = JsonUtils.getArray(obj, "");
                    for (int i = 0; i < errors.length(); i++)
                    {
                        if (JsonUtils.getStringFromArray(errors, i).compareToIgnoreCase("") == 0){
                            error = Carnetdesuivi.Error.MAIL_NOT_SENT;
                        }
                        else if (JsonUtils.getStringFromArray(errors, i).compareToIgnoreCase("") == 0){
                            error = Carnetdesuivi.Error.SERVER_ERROR;
                        }
                        else if (JsonUtils.getStringFromArray(errors, i).compareToIgnoreCase("") == 0){
                            error = Carnetdesuivi.Error.INVALID_TOKEN;
                        }
                    }
                }
        }
    }



    public Carnetdesuivi.Error getError() {
        return error;
    }

    public void setError(Carnetdesuivi.Error error) {
        this.error = error;
    }
}
