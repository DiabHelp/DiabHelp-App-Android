package fr.diabhelp.diabhelp.API;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;

/**
 * Created by Sumbers on 29/01/2016.
 */
public class ResponseConnexion {

    private ConnexionActivity.Error _error;
    private String _cookie = null;


    public ResponseConnexion() {
        _error = ConnexionActivity.Error.NONE;
    }

    public ConnexionActivity.Error getError() {
        return this._error;
    }

    public String getCookie() {
        return this._cookie;
    }

    public void setError(ConnexionActivity.Error error) {
        this._error = error;
    }

    public void setCookie(String cookie) {
        this._cookie = cookie;
    }
}
