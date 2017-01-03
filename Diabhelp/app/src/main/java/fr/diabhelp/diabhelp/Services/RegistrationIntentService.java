package fr.diabhelp.diabhelp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import fr.diabhelp.diabhelp.API.Asynctasks.TokenPostAPICallTask;
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
    public static final String ID_USER = "fr.diabhelp.diabhelp.Services.ID_USER";

    /**
     * Actions
     */
    public static final String REMOVE_TOKEN = "fr.diabhelp.diabhelp.Services.REMOVE_TOKEN";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        String idUser = null;
        String action = null;

        action = intent.getAction();
        if ((idUser = intent.getStringExtra(ID_USER)) != null)
        {
            System.out.println("RegistrationIntentService idUser = "  + idUser);
            try {
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.i(TAG, "FCM Registration Token = " + token);
                if (action != null && (action.compareToIgnoreCase(REMOVE_TOKEN) == 0))
                    token = "";
                this.sendRegistrationToServer(idUser, token);
            } catch (Exception e) {
                Log.d(TAG, "Failed to complete token refresh", e);
            }
        }

    }

    private void sendRegistrationToServer(String idUser, String token) {
        new TokenPostAPICallTask(getApplicationContext()).execute(idUser, token);
        stopSelf();

    }


}
