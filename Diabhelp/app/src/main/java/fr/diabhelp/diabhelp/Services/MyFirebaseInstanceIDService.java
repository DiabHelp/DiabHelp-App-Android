package fr.diabhelp.diabhelp.Services;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.firebase.iid.FirebaseInstanceIdService;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        SharedPreferences settings = getSharedPreferences(ConnexionActivity.PREF_FILE, Context.MODE_WORLD_READABLE);
        String idUser = settings.getString(ConnexionActivity.ID_USER, null);
        if (idUser != null)
        {
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
            stopSelf();
        }
    }



}
