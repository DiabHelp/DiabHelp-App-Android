package fr.diabhelp.diabhelp.API.ResponseModels;

import fr.diabhelp.diabhelp.Menu.ProfileActivity;

/**
 * Created by Sumbers on 30/06/2016.
 */
public class ResponseProfilSet {

    ProfileActivity.Error error = ProfileActivity.Error.NONE;

    public ProfileActivity.Error getError() {
        return this.error;
    }

    public void setError(ProfileActivity.Error error) {
        this.error = error;
    }
}
