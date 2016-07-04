package fr.diabhelp.diabhelp.API.ResponseModels;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;

/**
 * Created by Sumbers on 29/01/2016.
 */
public class ResponseConnexion {

    private ConnexionActivity.Error _error;
    private String _cookie = null;
    private String _typeUser = null;
    private String _id = null;


      public ResponseConnexion(JSONObject datas) {
        _error = ConnexionActivity.Error.NONE;
        if (datas != null){
            _typeUser = JsonUtils.getStringFromKey(datas, "type");
            if(_typeUser == null){
                Log.e(getClass().getSimpleName(), "typeUser non retourné par l'API");
            }
            _id = JsonUtils.getStringFromKey(datas, "id_user");
            if(_id == null){
                Log.e(getClass().getSimpleName(), "id non retourné par l'API");
            }
            _typeUser = JsonUtils.getStringFromKey(datas, "role");
            if(_typeUser == null){
                Log.e(getClass().getSimpleName(), "type user non retourné par l'API");
            }
        }
        else {
            Log.e(getClass().getSimpleName(), "Chaine JSON vide");
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
