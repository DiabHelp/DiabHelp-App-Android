package fr.diabhelp.prochepatient.Services;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import fr.diabhelp.prochepatient.ApiLinker.ApiErrors;
import fr.diabhelp.prochepatient.ApiLinker.ApiService;
import fr.diabhelp.prochepatient.ApiLinker.ResponseUser;
import fr.diabhelp.prochepatient.ApiLinker.RetrofitHelper;
import fr.diabhelp.prochepatient.User;
import fr.diabhelp.prochepatient.Utils.JsonUtils;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class PositionIntentService extends IntentService implements LocationListener {
    LocationManager locationManager = null;
    Location        location;
    String          idUser;
    public static String ID_USER = "fr.diabhelp.prochepatient.Services.PositionIntentService.ID_USER";

    public PositionIntentService() {
        super("PositionIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null)
        {
            String idUser = intent.getStringExtra(ID_USER);
            boolean isOk = initLocation();
            if (isOk)
                sendPosition();
            }
        }

    private void sendPosition()
    {
        RetrofitHelper retrofitH = new RetrofitHelper(getApplicationContext());
        ApiService serv = retrofitH.createService(RetrofitHelper.Build.DEV);
        String position = String.valueOf(location.getLatitude()) + ";" + String.valueOf(location.getLongitude());
        Call<ResponseBody> call = serv.sendPatientPosition(idUser, position);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private boolean initLocation() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(10000L, (float) 100, criteria, this, null);
        return true;
    }

    @Override
    public void onLocationChanged(Location location) {
        this.location = location;
        sendPosition();

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(10000L, (float) 100, criteria, this, null);

    }
}
