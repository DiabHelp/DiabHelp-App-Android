package fr.diabhelp.diabhelp.API.ResponseObjects;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.Connexion_inscription.RegisterActivity;

/**
 * Created by Sumbers on 29/01/2016.
 */
public class ResponseRegister {

    private RegisterActivity.Error _error;
    private String _cookie = null;


    public ResponseRegister() {
        _error = RegisterActivity.Error.NONE;
    }

    public RegisterActivity.Error getError() {
        return this._error;
    }

    public String getCookie() {
        return this._cookie;
    }

    public void setError(RegisterActivity.Error error) {
        this._error = error;
    }

    public void setCookie(String cookie) {
        this._cookie = cookie;
    }
}
