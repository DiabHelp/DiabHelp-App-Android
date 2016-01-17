package fr.diabhelp.carnetdesuivi.Carnet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import fr.diabhelp.carnetdesuivi.DataBase.DAO;
import fr.diabhelp.carnetdesuivi.DataBase.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.R;

/**
 * Created by naqued on 27/11/15.
 */
public class EntryActivity extends AppCompatActivity implements LocationListener {

    private String title;
    private String place;
    private Double glucide;
    private String activity;
    private String activityType;
    private String notes;
    private String date;
    private Double fast_insu;
    private Double slow_insu;
    private Double hba1c;
    private String formattedDate;
    EditText time;

    protected GridView grid;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    protected Location location;

    private TimePicker timePicker1;

    private int hour;
    private int minute;

    static final int TIME_DIALOG_ID = 999;

    public enum InputType{
        TITLE(0),
        PLACE(1),
        GLUCIDE(2),
        ACTIVITY(3),
        ACTIVITYTYPE(4),
        NOTES(5),
        DATE(6),
        FAST_INSU(7),
        SLOW_INSU(8),
        HBA1C(9),
        GLYCEMY(10);

        private final int value;

        private InputType(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    };

    public enum IconeType{
        BREAKFAST(0),
        LAUNCH(1),
        DINER(2),
        ENCAS(3),
        SLEEP(4),
        WAKEUP(5),
        NIGHT(6),
        WORKOUT(7),
        HYPO(8),
        HYPER(9),
        WORK(10),
        HOME(11),
        ALCOHOL(12),
        PERIOD(13);

        private final int value;

        private IconeType(int v) {
            this.value = v;
        }

        public int getValue() {
            return value;
        }
    };

    String[] web = {
            "Petit Déjeuner",
            "Déjeuner",
            "Diner",
            "Encas",
            "Coucher",
            "Levée",
            "Nuit",
            "Alcohol",
            "Sport",
            "Travail",
            "Maison",
            "Période"

    } ;
    int[] imageId = {
            R.drawable.ptidej,
            R.drawable.launch,
            R.drawable.diner,
            R.drawable.cassecroute,
            R.drawable.coucher,
            R.drawable.reveil,
            R.drawable.night,
            R.drawable.alcool,
            R.drawable.sport,
            R.drawable.work,
            R.drawable.home,
            R.drawable.period
    };

    List<EditText> _inputlist;
    ListView lv;
    Context context;
    ArrayList entry;
    DAO bdd;
    EntryActivity _this;
    List<Integer> isActiveicon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // in thread
        _this = this;

        final String myDate;
        bdd = new DAO(this);
        init_fields();
        init_icon();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        formattedDate = df.format(c.getTime());
        fill_date();

        new Handler_gps(this, (TextView) findViewById(R.id.editplace)).run();

        time = (EditText) findViewById(R.id.editactivity);
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(EntryActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        time.setText(selectedHour + "H" + selectedMinute + " min");
                    }
                }, 0, 0, true);//Yes 24 hour time
                mTimePicker.setTitle("Temps d'activité");
                mTimePicker.show();

            }
        });
    }
/*        CustomGrid adapter = new CustomGrid(EntryActivity.this, web, imageId);
        grid= (GridView)findViewById(R.id.grid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(EntryActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();

            }
        });

    }*/



/*    public Location getLocation() {
*//*        try {*//*
*//*            locationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);*//*

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

*//*        } catch (Exception e) {
            e.printStackTrace();
        }*//*
        return location;
    }*/

    private void fillPlace(Location location)
    {
        TextView txtplace = (TextView) findViewById(R.id.editplace);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (location == null) {
            txtplace.setText("N/A");
        }
        else {
            try {
/*                Log.e("location lat", String.valueOf(location.getLatitude()));
                Log.e("location lng", String.valueOf(location.getLongitude()));*/
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getAddressLine(0);
                    String stateName = addresses.get(0).getAddressLine(1);
                    //Toast.makeText(getApplicationContext(),stateName , 1).show();
                    String countryName = addresses.get(0).getAddressLine(2);
                    txtplace.setText(cityName);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView txtplace = (TextView) findViewById(R.id.editplace);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (location == null) {
            txtplace.setText("N///A");
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude", "disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude", "enable");
        /*Location tmp = getLocation();
        fillPlace(tmp);*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

/*    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            //your code here
        }
    };*/

    protected void fill_date()
    {
        _inputlist.get(InputType.DATE.getValue()).setText(formattedDate);
        Log.e("date actuel", formattedDate);

    }

    protected void save_data() {
        if (_inputlist.get(InputType.TITLE.getValue()).getText().toString().length() == 0)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Aucune titre renseigné");
            alertDialog.setMessage("Vous n'avez renseigné aucun Titre. êtes vous sur de vouloir continuer ?");
            alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    _inputlist.get(InputType.TITLE.getValue()).setText("Sans nom");
                    saveIt();
                    Log.e("save_date", "oui");
                }
            });
            alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Log.e("save_date", "non");
                }
            });
            alertDialog.show();
            Log.e("save_date", "apres le show");
        } //TODO avertir qu'il n'y a pas de titre
        else {
            Log.e("save_date", "else");
            saveIt();
        }
        //get Date

    }
    protected void saveIt() {

        // save
        EntryOfCDS Entry = new EntryOfCDS(formattedDate);
        Toast.makeText(getApplicationContext(), formattedDate,
                Toast.LENGTH_LONG).show();


        String _minute;
        int hours = new Time(System.currentTimeMillis()).getHours();
        int minutes = new Time(System.currentTimeMillis()).getMinutes();
        //TODO NE PAS UTILISER DE FONCTIONS DEPRECIEES
        if (minutes < 10)
             _minute = "0" + String.valueOf(minutes);
        else
            _minute = String.valueOf(minutes);
        String Hours = String.valueOf(hours) + ":" + _minute;



        Entry.setNotes(_inputlist.get(InputType.NOTES.getValue()).getText().toString());
        Entry.setTitle(_inputlist.get(InputType.TITLE.getValue()).getText().toString());
        Entry.setGlucide(_inputlist.get(InputType.GLUCIDE.getValue()).getText().toString());
        Entry.setActivity(_inputlist.get(InputType.ACTIVITY.getValue()).getText().toString());
        Entry.setActivityType(_inputlist.get(InputType.ACTIVITYTYPE.getValue()).getText().toString());
        Entry.setDate(_inputlist.get(InputType.DATE.getValue()).getText().toString());
        Entry.setFast_insu(_inputlist.get(InputType.FAST_INSU.getValue()).getText().toString());
        Entry.setSlow_insu(_inputlist.get(InputType.SLOW_INSU.getValue()).getText().toString());
        Entry.setHba1c(_inputlist.get(InputType.HBA1C.getValue()).getText().toString());
        Entry.setHour(Hours);
        Entry.setglycemy(_inputlist.get(InputType.GLYCEMY.getValue()).getText().toString());

        Entry.setBreakfast(isActiveicon.get(IconeType.BREAKFAST.getValue()));
        Entry.setLaunch(isActiveicon.get(IconeType.LAUNCH.getValue()));
        Entry.setDiner(isActiveicon.get(IconeType.DINER.getValue()));
        Entry.setEncas(isActiveicon.get(IconeType.ENCAS.getValue()));
        Entry.setSleep(isActiveicon.get(IconeType.SLEEP.getValue()));
        Entry.setWakeup(isActiveicon.get(IconeType.WAKEUP.getValue()));
        Entry.setNight(isActiveicon.get(IconeType.NIGHT.getValue()));
        Entry.setWorkout(isActiveicon.get(IconeType.WORKOUT.getValue()));
        Entry.setHypogly(isActiveicon.get(IconeType.HYPO.getValue()));
        Entry.setHypergly(isActiveicon.get(IconeType.HYPER.getValue()));
        Entry.setAtwork(isActiveicon.get(IconeType.WORK.getValue()));
        Entry.setAthome(isActiveicon.get(IconeType.HOME.getValue()));
        Entry.setAlcohol(isActiveicon.get(IconeType.ALCOHOL.getValue()));
        Entry.setPeriod(isActiveicon.get(IconeType.PERIOD.getValue()));

        bdd.open();
        bdd.AddDay(Entry);
        bdd.close();
        Toast.makeText(this, "Enregistrement efféctué", Toast.LENGTH_LONG).show();
    }

    public void init_fields() {
        _inputlist = new ArrayList<EditText>();

        _inputlist.add(InputType.TITLE.getValue(), (EditText) findViewById(R.id.edittitre));
        _inputlist.add(InputType.PLACE.getValue(), (EditText) findViewById(R.id.editplace));
        _inputlist.add(InputType.GLUCIDE.getValue(), (EditText) findViewById(R.id.editglucide));

        _inputlist.add(InputType.ACTIVITY.getValue(), (EditText) findViewById(R.id.editactivity));
        _inputlist.add(InputType.ACTIVITYTYPE.getValue(), (EditText) findViewById(R.id.editactivitytype));
        _inputlist.add(InputType.NOTES.getValue(), (EditText) findViewById(R.id.editnotes));

        _inputlist.add(InputType.DATE.getValue(), (EditText) findViewById(R.id.editDate));
        _inputlist.add(InputType.FAST_INSU.getValue(), (EditText) findViewById(R.id.editrapide));
        _inputlist.add(InputType.SLOW_INSU.getValue(), (EditText) findViewById(R.id.editlente));

        _inputlist.add(InputType.HBA1C.getValue(), (EditText) findViewById(R.id.edithba1c));
        _inputlist.add(InputType.GLYCEMY.getValue(), (EditText) findViewById(R.id.editglycemy));
    }

    public void init_icon() {
        isActiveicon = new ArrayList<Integer>();

        isActiveicon.add(IconeType.BREAKFAST.getValue(), 0);
        isActiveicon.add(IconeType.LAUNCH.getValue(), 0);
        isActiveicon.add(IconeType.DINER.getValue(), 0);
        isActiveicon.add(IconeType.ENCAS.getValue(), 0);
        isActiveicon.add(IconeType.SLEEP.getValue(), 0);
        isActiveicon.add(IconeType.WAKEUP.getValue(), 0);
        isActiveicon.add(IconeType.NIGHT.getValue(), 0);
        isActiveicon.add(IconeType.WORKOUT.getValue(), 0);
        isActiveicon.add(IconeType.HYPO.getValue(), 0);
        isActiveicon.add(IconeType.HYPER.getValue(), 0);
        isActiveicon.add(IconeType.WORK.getValue(), 0);
        isActiveicon.add(IconeType.HOME.getValue(), 0);
        isActiveicon.add(IconeType.ALCOHOL.getValue(), 0);
        isActiveicon.add(IconeType.PERIOD.getValue(), 0);
    }

    public void save_button(View v)
    {
        if (_inputlist.get(InputType.TITLE.getValue()).getText().toString().length() == 0)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Aucune titre renseigné");
            alertDialog.setMessage("Vous n'avez renseigné aucun Titre. êtes vous sur de vouloir continuer ?");
            alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    _inputlist.get(InputType.TITLE.getValue()).setText("Sans nom");
                    if (!(_inputlist.get(InputType.GLYCEMY.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.GLUCIDE.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.HBA1C.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.FAST_INSU.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.SLOW_INSU.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.NOTES.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.ACTIVITY.getValue()).getText().toString().length() == 0 &&
                            _inputlist.get(InputType.ACTIVITYTYPE.getValue()).getText().toString().length() == 0
                            ))
                        saveIt();
                    Intent Entryintent = new Intent(EntryActivity.this, CarnetActivity.class);
                    EntryActivity.this.startActivity(Entryintent);
                    _this.finish();
                }
            });
            alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            alertDialog.show();
        }
        else {
            saveIt();
            Intent Entryintent = new Intent(EntryActivity.this, CarnetActivity.class);
            EntryActivity.this.startActivity(Entryintent);
            _this.finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent Entryintent = new Intent(EntryActivity.this, CarnetActivity.class);
        EntryActivity.this.startActivity(Entryintent);
        this.finish();
    }

    // Methode icones
    public void add_breakfeast(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgptidej);

        if (isActiveicon.get(IconeType.BREAKFAST.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.BREAKFAST.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.BREAKFAST.getValue(), 0);
        }
    }
    public void add_launch(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imglaunch);

        if (isActiveicon.get(IconeType.LAUNCH.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.LAUNCH.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.LAUNCH.getValue(), 0);
        }
    }

    public void add_diner(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgdiner);

        if (isActiveicon.get(IconeType.DINER.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.DINER.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.DINER.getValue(), 0);
        }
    }

    public void add_encas(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgencas);

        if (isActiveicon.get(IconeType.ENCAS.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.ENCAS.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.ENCAS.getValue(), 0);
        }
    }

    public void add_sleep(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgcoucher);

        if (isActiveicon.get(IconeType.SLEEP.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.SLEEP.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.SLEEP.getValue(), 0);
        }
    }

    public void add_wakeup(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imglevee);

        if (isActiveicon.get(IconeType.WAKEUP.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.WAKEUP.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.WAKEUP.getValue(), 0);
        }
    }

    public void add_night(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgnuit);

        if (isActiveicon.get(IconeType.NIGHT.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.NIGHT.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.NIGHT.getValue(), 0);
        }
    }

    public void add_workout(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgsport);

        if (isActiveicon.get(IconeType.WORKOUT.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.WORKOUT.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.WORKOUT.getValue(), 0);
        }
    }

    public void add_work(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgwork);

        if (isActiveicon.get(IconeType.WORK.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.WORK.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.WORK.getValue(), 0);
        }
    }

    public void add_home(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imghome);

        if (isActiveicon.get(IconeType.HOME.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.HOME.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.HOME.getValue(), 0);
        }
    }

    public void add_alcohol(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgalcool);

        if (isActiveicon.get(IconeType.ALCOHOL.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.ALCOHOL.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.ALCOHOL.getValue(), 0);
        }
    }

    public void add_period(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgperiod);

        if (isActiveicon.get(IconeType.PERIOD.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.PERIOD.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.PERIOD.getValue(), 0);
        }
    }

    public void add_hypo(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imghypo);

        if (isActiveicon.get(IconeType.HYPO.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.HYPO.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.HYPO.getValue(), 0);
        }
    }

    public void add_hyper(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imghyper);

        if (isActiveicon.get(IconeType.HYPER.getValue()) == 0) {
            img.setBackgroundColor(Color.parseColor("#ff00ff00"));
            isActiveicon.set(IconeType.HYPER.getValue(), 1);
        }
        else {
            img.setBackgroundColor(Color.parseColor("#ffffff"));
            isActiveicon.set(IconeType.HYPER.getValue(), 0);
        }
    }
}
