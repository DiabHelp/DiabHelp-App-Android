package fr.diabhelp.patient.ApiLinker;

/**
 * Created by Sumbers on 26/07/2016.
 */
public abstract class ResponseBase {
    public String message;

    public ResponseBase(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
