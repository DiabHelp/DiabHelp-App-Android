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

    private RegisterActivity.Error _error = RegisterActivity.Error.NONE;

    public ResponseRegister() {
    }

    public ResponseRegister(JSONObject datas) {
        System.out.println("ResponseRegister datas = " + datas.toString());
        if (datas != null) {
            try {
                Boolean success = false;
                success = datas.getBoolean("success");
                    if (success == false)
                    {
                        setError(RegisterActivity.Error.SERVER_ERROR);
                        JSONArray arr = datas.getJSONArray("errors");
                        for (int i = 0; i < arr.length(); i++) {
                            if (arr.getString(i).equalsIgnoreCase("Username already used")) {
                                setError(RegisterActivity.Error.LOGIN_ALREADY_USED);
                            }
                            if (arr.getString(i).equalsIgnoreCase("Email already used")) {
                                setError(RegisterActivity.Error.EMAIL_ALREADY_USED);
                            }
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

    public RegisterActivity.Error getError() {return this._error;}

    public void setError(RegisterActivity.Error _error) {this._error = _error;}
}
