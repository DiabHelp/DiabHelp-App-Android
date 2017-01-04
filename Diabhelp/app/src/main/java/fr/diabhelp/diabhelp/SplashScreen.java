package fr.diabhelp.diabhelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;


import java.util.HashMap;
import java.util.Map;

import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.Core.CoreActivity;
import fr.diabhelp.diabhelp.Utils.NotificationsDatasHandler;

/**
 * Created by naqued on 28/09/15.
 */
public class SplashScreen extends Activity {

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 1000;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.splash_screen);
        Bundle bundle;
        if (getIntent() != null && ((bundle = getIntent().getExtras()) != null))
            handleBundle(bundle);
        else
        {
         /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(SplashScreen.this, ConnexionActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    private void handleBundle(Bundle bundle)
    {
        Map<String, String> map = new HashMap<>();
        for (String key : bundle.keySet())
        {
            map.put(key, bundle.get(key).toString());
            Log.d("SplashScreen", String.format("%s %s (%s)", key,
                    bundle.get(key).toString(), bundle.get(key).getClass().getName()));
        }
        if (map.containsKey("PNAME"))
            NotificationsDatasHandler.handle(map, getApplicationContext());
    }
}
