package fr.diabhelp.diabhelp.Carnet_de_suivi.API.Response;

import fr.diabhelp.diabhelp.Carnet_de_suivi.Carnetdesuivi;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class ResponseCDSetMissingEntries {

    Carnetdesuivi.Error error = Carnetdesuivi.Error.NONE;

    public Carnetdesuivi.Error getError() {
        return this.error;
    }

    public void setError(Carnetdesuivi.Error error) {
        this.error = error;
    }
}
