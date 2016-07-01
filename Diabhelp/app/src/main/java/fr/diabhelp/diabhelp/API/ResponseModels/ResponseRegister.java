package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import fr.diabhelp.diabhelp.Connexion_inscription.RegisterActivity;

/**
 * Created by Sumbers on 29/01/2016.
 */
public class ResponseRegister {

    private RegisterActivity.Error _error = null;

    public ResponseRegister() {
    }

    public ResponseRegister(JSONObject datas) {
        if (datas != null) {
            try {
                JSONArray arr = datas.getJSONArray("errors");
                for (int i = 0; i < arr.length(); i++) {
                    if (arr.getString(i).equalsIgnoreCase("Username already use")) {
                        setError(RegisterActivity.Error.LOGIN_ALREADY_USED);
                    }
                    if (arr.getString(i).equalsIgnoreCase("Email already use")){
                        setError(RegisterActivity.Error.EMAIL_ALREADY_USED);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else {
            Log.e(getClass().getSimpleName(), "Chaine JSON vide");
        }
    }

    public RegisterActivity.Error getError() {
        if (this._error != null)
            return this._error;
        return (RegisterActivity.Error.NONE);

    }

    public void setError(RegisterActivity.Error _error) {
        this._error = _error;
    }
}
