package fr.diabhelp.diabhelp.API.ResponseObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

import fr.diabhelp.diabhelp.Connexion_inscription.RegisterActivity;

/**
 * Created by Sumbers on 29/01/2016.
 */
public class ResponseRegister {

    private RegisterActivity.Error _error;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<String>();

    /**
     *
     * @return
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The errors
     */
    public List<String> getErrors() {
        return errors;
    }

    /**
     *
     * @param errors
     * The errors
     */
    public void setErrors(List<String> errors) {
        this.errors = errors;
        System.out.println("coucou je set une erreur");
    }


    public RegisterActivity.Error getError() {
        return this._error;
    }

    public void setError(RegisterActivity.Error _error) {
        this._error = _error;
    }
}
