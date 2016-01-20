package fr.diabhelp.diabhelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;



import fr.diabhelp.diabhelp.Connexion_inscription.ConnexionActivity;
import fr.diabhelp.diabhelp.Core.CoreActivity;

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
