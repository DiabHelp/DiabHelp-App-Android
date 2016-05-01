package fr.diabhelp.diabhelp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 *
 * helper methods.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        SharedPreferences _settings = getSharedPreferences(ConnexionActivity.PREF_FILE, Context.MODE_PRIVATE);
        try {
            InstanceID instanceID = InstanceID.getInstance(this);
            String token = instanceID.getToken(getString(R.string.project_number),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.i(TAG, "GCM Registration Token = " + token);
            //TODO ENVOYER TOKEN AU SERVEUR
            //_settings.edit().putBoolean(ConnexionActivity.SENT_TOKEN_TO_SERVER, true).commit();
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            _settings.edit().putBoolean(ConnexionActivity.SENT_TOKEN_TO_SERVER, false).commit();
        }
    }


}
