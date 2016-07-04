package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.Utils.JsonUtils;

/**
 * Created by Sumbers on 29/01/2016.
 */
public class ResponseConnexion {

    private ConnexionActivity.Error _error;
    private String _cookie = null;
    private String _typeUser = null;
    private String _id = null;


    public ResponseConnexion() {
    }

    public ResponseConnexion(JSONObject datas) {
        _error = ConnexionActivity.Error.NONE;
        if (datas != null){
            Boolean success = JsonUtils.getBoolFromKey(datas, "success");
            if (success != null) {
                if (success == true) {
                    _id = JsonUtils.getStringFromKey(datas, "id_user");
                    if (_id == null) {
                        Log.e(getClass().getSimpleName(), "id non retourné par l'API");
                    }
                    _typeUser = (JsonUtils.getArrayFromObj(datas, "role")).toString();
                    if (_typeUser == null) {
                        Log.e(getClass().getSimpleName(), "type user non retourné par l'API");
                    }
                }
                else
                    _error = ConnexionActivity.Error.BAD_CREDENTIALS;
            }
            else
            _error = ConnexionActivity.Error.SERVER_ERROR;
        }
        else {
            Log.e(getClass().getSimpleName(), "Chaine JSON vide");
            _error = ConnexionActivity.Error.SERVER_ERROR;
        }
    }

    public ConnexionActivity.Error getError() {return this._error; }

    public String getCookie() {return this._cookie; }

    public String getTypeUser() {return this._typeUser;}
    public String getIdUser() {return this._id;}


    public void setError(ConnexionActivity.Error error) {this._error = error; }
    public void setCookie(String cookie) {this._cookie = cookie;}
    public void setTypeUser(String typeUser) {this._typeUser = typeUser;}
    public void setId(String _id) {this._id = _id;}
}
