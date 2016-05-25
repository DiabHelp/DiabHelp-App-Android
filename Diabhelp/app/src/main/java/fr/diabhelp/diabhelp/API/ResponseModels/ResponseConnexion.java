package fr.diabhelp.diabhelp.API.ResponseModels;

import android.util.Log;

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

    public ResponseConnexion() {
        _error = ConnexionActivity.Error.NONE;
    }

    public ResponseConnexion(JSONObject datas) {
        _error = ConnexionActivity.Error.NONE;
        if (datas != null){
            _typeUser = JsonUtils.getStringFromKey(datas, "type");
            if(_typeUser == null){
                Log.e(getClass().getSimpleName(), "typeUser non retourné par l'API");
            }
        }
        else {
            Log.e(getClass().getSimpleName(), "Chaine JSON vide");
        }
    }

    public ConnexionActivity.Error getError() {
        return this._error;
    }

    public String getCookie() {
        return this._cookie;
    }
    public String getTypeUser() {return this._typeUser;}



    public void setError(ConnexionActivity.Error error) {
        this._error = error;
    }

    public void setCookie(String cookie) {
        this._cookie = cookie;
    }

    public void setTypeUser(String typeUser) {this._typeUser = typeUser;}
}
