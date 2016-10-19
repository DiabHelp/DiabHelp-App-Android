package fr.diabhelp.proche.Widget;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.EditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by naqued on 04/01/16.
 */
public class Handler_gps extends Service implements LocationListener {

    boolean localisationOn = false;
    Location location;
    Context _this;
    EditText _txtviewplace;

    LocationManager locationManager;
    private final Handler handler = new Handler();

    public class MyBinder extends Binder {
        Handler_gps getService(){
            return Handler_gps.this;
        }
    }

    Handler_gps(Context _e, EditText txt) {
        _this = _e;
        _txtviewplace = txt;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return new MyBinder();
    }

    public void run()
    {
        fillPlace(getLocation());
    }

    @Override
    public void onCreate() {
        Log.e("activity", "handler_gps : Created");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        locationManager.removeUpdates(this);
        Log.e("activity", "handler_gps : destroy");
        super.onDestroy();
    }

    public Location getLocation() {
/*        try {*/
            locationManager = (LocationManager) _this.getSystemService(LOCATION_SERVICE);

        // getting GPS status
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        // getting network status
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            System.out.println("nothing.....");
        } else {
            // First get location from Network Provider
            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER,  0,  0, this);
                Log.d("Network", "Network");
                System.out.println("nework on.........");
                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Log.e("debug", "Pass internet");
                    if (location != null) {
                        Log.e("debug", "location pas null");
                    }
                }
            }
            //get the location by gps
            if (isGPSEnabled) {
                System.out.println("gps on.........");
                if (location == null) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0, this);
                    Log.d("GPS Enabled", "GPS Enabled");
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        Log.e("debug", "Pass gps");
                        if (location != null) {
                            Log.e("debug", "location pas null");
                        }
                    }
                }
            }
        }

/*        } catch (Exception e) {
            e.printStackTrace();
        }*/
        return location;
    }

    private void fillPlace(Location location)
    {
        EditText txtplace = _txtviewplace;

        Geocoder geocoder = null;
        List<Address> addresses;
        geocoder = new Geocoder(_this, Locale.getDefault());
 /*       try {
            geocoder = new Geocoder(this, Locale.getDefault());
        } catch (Exception e) {
            e.printStackTrace();
            txtplace.setText("Non renseigné");
            Log.e("geocoder", "crash catch");
            return ;
        }*/
        if (location == null) {
            txtplace.setText("Non renseigné");
        }
        else {
            try {
/*                Log.e("location lat", String.valueOf(location.getLatitude()));
                Log.e("location lng", String.valueOf(location.getLongitude()));*/
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getAddressLine(0);
/*                    String stateName = addresses.get(0).getAddressLine(1);
                    //Toast.makeText(getApplicationContext(),stateName , 1).show();
                    String countryName = addresses.get(0).getAddressLine(2);*/
                    txtplace.setText(cityName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {

        /*MyThread mythread = new MyThread();
                    MyThread.start();*/
        handler.post(getData);
        super.onStart(intent, startId);
    }

    private final Runnable getData = new Runnable() {
        public void run() {
            getDataFrame();
        }
    };

    private void getDataFrame() {
        if (localisationOn){
            localisationOn = false;
            stopGPS();
        }
        if (!localisationOn){
            startGPS();
                        /* On active le flag de localisation */
            localisationOn = true;
        }
        handler.postDelayed(getData,10000);

    }
    public void onLocationChanged(Location location) {
        if ((location != null) && (localisationOn))
        {
            Log.d("Localisation", "Envoi des informations de localisation avec :");
            Log.d("Latitude", String.valueOf(location.getLatitude()));
            Log.d("Longitude", String.valueOf(location.getLongitude()));
        }
        EditText txtplace = _txtviewplace;

        Geocoder geocoder = null;
        List<Address> addresses;
        geocoder = new Geocoder(_this, Locale.getDefault());

        if (location == null) {
            txtplace.setText("");
        }
        else {
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getAddressLine(0);
/*                    String stateName = addresses.get(0).getAddressLine(1);
                    //Toast.makeText(getApplicationContext(),stateName , 1).show();
                    String countryName = addresses.get(0).getAddressLine(2);*/
                    txtplace.setText(cityName);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    public void startGPS()
    {
        /* Intent du service de localisation */
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /* On active l'actualisation par le GPS et par le réseau téléphonique */
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1,1,this);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1,1,this);

        Log.d("GPS", "GPS Activé");
    }

    public void stopGPS()
    {

        locationManager.removeUpdates(this);
        Log.d("GPS", "GPS Désactivé");
    }

}