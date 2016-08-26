package fr.diabhelp.carnetdesuivi.Carnet;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import fr.diabhelp.carnetdesuivi.API.Service.ServerUdpateService;
import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnet.Statistics.StatisticsActivity;
import fr.diabhelp.carnetdesuivi.Carnetdesuivi;
import fr.diabhelp.carnetdesuivi.R;
import fr.diabhelp.carnetdesuivi.Utils.DateMagnifier;
import fr.diabhelp.carnetdesuivi.Utils.DateUtils;

/**
 * Created by naqued on 27/11/15.
 */
public class EntryActivity extends AppCompatActivity implements LocationListener {

    protected LocationManager locationManager;
    List<EditText> _inputlist;
    EntryActivity _this;
    List<Integer> isActiveicon;
    String _activitycycle;
    private String formattedDate;;
    private EditText time;;
    private DAO dao = null;
    private SQLiteDatabase db = null;
    private DateMagnifier _dm;
    private String _title;
    private String _place;
    private Double _glucide;
    private String _activity;
    private String _activityType;
    private String _notes;
    private String _date;
    private Double _fast_insu;
    private Double _slow_insu;
    private Double _hba1c;
    private String _hour;
    private Double _glycemy;
    private Integer _launch;
    private Integer _diner;
    private Integer _encas;
    private Integer _sleep;
    private Integer _wakeup;
    private Integer _night;
    private Integer _workout;
    private Integer _hypogly;
    private Integer _hypergly;
    private Integer _atwork;
    private Integer _athome;
    private Integer _alcohol;
    private Integer _period;
    private Integer _breakfast;
    private Handler_gps _gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_activity);
        dao = DAO.getInstance(this);
        db = dao.open();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE); // in thread
        _this = this;

        _dm = new DateMagnifier();
        init_fields();
        init_icon();
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat(DateUtils.DATECREATION_DB_FORMAT, Locale.US);
        formattedDate = df.format(c.getTime());

        fill_date();
        Intent intent = getIntent();

        _activitycycle = intent.getExtras().getString("activity");

        if (intent.hasExtra("title"))
            _title = intent.getExtras().getString("title");

        if (intent.hasExtra("place"))
            _place = intent.getExtras().getString("place");
        if (intent.hasExtra("glucide"))
            _glucide = intent.getExtras().getDouble("glucide");
        if (intent.hasExtra("activity"))
            _activity = intent.getExtras().getString("activity");
        if (intent.hasExtra("activityType"))
            _activityType = intent.getExtras().getString("activityType");
        if (intent.hasExtra("notes"))
            _notes = intent.getExtras().getString("notes");
        if (intent.hasExtra("fastInsu"))
            _fast_insu = intent.getExtras().getDouble("fastInsu");
        if (intent.hasExtra("slowInsu"))
            _slow_insu = intent.getExtras().getDouble("slowInsu");
        if (intent.hasExtra("hba1c"))
            _hba1c = intent.getExtras().getDouble("hba1c");
        if (intent.hasExtra("glycemy"))
            _glycemy = intent.getExtras().getDouble("glycemy");

        if (intent.hasExtra("hour")) {
            _hour = intent.getExtras().getString("hour");
        }
        if (intent.hasExtra("date")) {
            _date = intent.getExtras().getString("date");
            formattedDate = _date;
        }
        if (intent.hasExtra("launch"))
            _launch = intent.getExtras().getInt("launch");
        if (intent.hasExtra("diner"))
            _diner = intent.getExtras().getInt("diner");
        if (intent.hasExtra("encas"))
            _encas = intent.getExtras().getInt("encas");
        if (intent.hasExtra("sleep"))
            _sleep = intent.getExtras().getInt("sleep");
        if (intent.hasExtra("wakeup"))
            _wakeup = intent.getExtras().getInt("wakeup");
        if (intent.hasExtra("night"))
            _night = intent.getExtras().getInt("night");
        if (intent.hasExtra("workout"))
            _workout = intent.getExtras().getInt("workout");
        if (intent.hasExtra("hypogly"))
            _hypogly = intent.getExtras().getInt("hypogly");
        if (intent.hasExtra("hypergly"))
            _hypergly = intent.getExtras().getInt("hypergly");
        if (intent.hasExtra("work"))
            _atwork = intent.getExtras().getInt("work");
        if (intent.hasExtra("athome"))
            _athome = intent.getExtras().getInt("athome");
        if (intent.hasExtra("alcohol"))
            _alcohol = intent.getExtras().getInt("alcohol");
        if (intent.hasExtra("period"))
            _period = intent.getExtras().getInt("period");
        if (intent.hasExtra("breakfast"))
            _breakfast = intent.getExtras().getInt("breakfast");


        _gps = new Handler_gps(this, (TextView) findViewById(R.id.editplace)); // TODO soucis sur le fill_place
        _gps.run();

        time = (EditText) findViewById(R.id.editactivity);
        time.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
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
        if (intent.hasExtra("date"))
            fill_fields();
    }

    @Override
    public void onLocationChanged(Location location) {
        TextView txtplace = (TextView) findViewById(R.id.editplace);

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        if (location == null) {
            txtplace.setText("Not found");
        }
        else {
            try {
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0) {
                    String cityName = addresses.get(0).getAddressLine(0);
                    txtplace.setText(cityName);
                }
            } catch (IOException e) {
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
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude", "status");
    }

    protected void fill_date()
    {
        _inputlist.get(InputType.DATE.getValue()).setText(_dm.getCleanDate(formattedDate));
    }

    protected void saveIt() {

        // save
        EntryOfCDS entry = new EntryOfCDS(formattedDate);
        entry.setDateEdition(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

        String _minute;
        int hours = new Time(System.currentTimeMillis()).getHours();
        int minutes = new Time(System.currentTimeMillis()).getMinutes();
        if (minutes < 10)
             _minute = "0" + String.valueOf(minutes);
        else
            _minute = String.valueOf(minutes);
        String Hours = String.valueOf(hours) + ":" + _minute;

        entry.setNotes(_inputlist.get(InputType.NOTES.getValue()).getText().toString());
        entry.setPlace(_inputlist.get(InputType.PLACE.getValue()).getText().toString());
        entry.setTitle(_inputlist.get(InputType.TITLE.getValue()).getText().toString());
        entry.setGlucide(_inputlist.get(InputType.GLUCIDE.getValue()).getText().toString());
        entry.setActivity(_inputlist.get(InputType.ACTIVITY.getValue()).getText().toString());
        entry.setActivityType(_inputlist.get(InputType.ACTIVITYTYPE.getValue()).getText().toString());
        entry.setDateCreation(formattedDate);
        entry.setFast_insu(_inputlist.get(InputType.FAST_INSU.getValue()).getText().toString());
        entry.setSlow_insu(_inputlist.get(InputType.SLOW_INSU.getValue()).getText().toString());
        entry.setHba1c(_inputlist.get(InputType.HBA1C.getValue()).getText().toString());

        entry.setglycemy(_inputlist.get(InputType.GLYCEMY.getValue()).getText().toString());

        entry.setBreakfast(isActiveicon.get(IconeType.BREAKFAST.getValue()));
        entry.setLaunch(isActiveicon.get(IconeType.LAUNCH.getValue()));
        entry.setDiner(isActiveicon.get(IconeType.DINER.getValue()));
        entry.setEncas(isActiveicon.get(IconeType.ENCAS.getValue()));
        entry.setSleep(isActiveicon.get(IconeType.SLEEP.getValue()));
        entry.setWakeup(isActiveicon.get(IconeType.WAKEUP.getValue()));
        entry.setNight(isActiveicon.get(IconeType.NIGHT.getValue()));
        entry.setWorkout(isActiveicon.get(IconeType.WORKOUT.getValue()));
        entry.setHypogly(isActiveicon.get(IconeType.HYPO.getValue()));
        entry.setHypergly(isActiveicon.get(IconeType.HYPER.getValue()));
        entry.setAtwork(isActiveicon.get(IconeType.WORK.getValue()));
        entry.setAthome(isActiveicon.get(IconeType.HOME.getValue()));
        entry.setAlcohol(isActiveicon.get(IconeType.ALCOHOL.getValue()));
        entry.setPeriod(isActiveicon.get(IconeType.PERIOD.getValue()));
        System.out.println("EntryActivity idUser = " + Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""));
        entry.setIdUser(Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""));
        if (EntryOfCDSDAO.selectDay(_date, _hour, Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""),  db) == null) {
            entry.setHour(Hours);
            EntryOfCDSDAO.addDay(entry, db);
            Intent updateServer = new Intent(this, ServerUdpateService.class);
            System.out.println("je set l'id lors de l'ajout d'une entrée sur le serveur = " + Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""));
            updateServer.putExtra(ServerUdpateService.EXTRA_ID_USER, Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""));
            updateServer.putExtra(ServerUdpateService.EXTRA_ACTION, ServerUdpateService.UPDATE);
            startService(updateServer);
        }
        else {
            entry.setHour(_hour);
            EntryOfCDSDAO.update(entry, db);
            Intent updateServer = new Intent(this, ServerUdpateService.class);
            System.out.println("je set l'id lors de l'edition d'une entrée sur le serveur = " + Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""));
            updateServer.putExtra(ServerUdpateService.EXTRA_ID_USER, Carnetdesuivi._settings.getString(Carnetdesuivi.ID_USER, ""));
            updateServer.putExtra(ServerUdpateService.EXTRA_ACTION, ServerUdpateService.UPDATE);
            startService(updateServer);
        }
        Toast.makeText(this, "Enregistrement efféctué", Toast.LENGTH_LONG).show();
    }

    private void fill_fields()
    {
        _inputlist.get(InputType.DATE.getValue()).setText(_dm.getCleanDate(_date));
        formattedDate = _date;
        if (_title != null)
            _inputlist.get(InputType.TITLE.getValue()).setText(_title);
        if (_place != null)
            _inputlist.get(InputType.PLACE.getValue()).setText(_place);
        if (_glycemy != null)
            _inputlist.get(InputType.GLYCEMY.getValue()).setText(String.valueOf(_glycemy));
        if (_glucide != null)
            _inputlist.get(InputType.GLUCIDE.getValue()).setText(String.valueOf(_glucide));
        if (_activity != null)
            _inputlist.get(InputType.ACTIVITY.getValue()).setText(_activity);
        if (_activityType != null)
            _inputlist.get(InputType.ACTIVITYTYPE.getValue()).setText(_activityType);
        if (_fast_insu != null)
            _inputlist.get(InputType.FAST_INSU.getValue()).setText(String.valueOf(_fast_insu));
        if (_slow_insu != null)
            _inputlist.get(InputType.SLOW_INSU.getValue()).setText(String.valueOf(_slow_insu));
        if (_hba1c != null)
            _inputlist.get(InputType.HBA1C.getValue()).setText(String.valueOf(_hba1c));
        if (_notes != null)
            _inputlist.get(InputType.NOTES.getValue()).setText(_notes);
        if (_place != null)
            _inputlist.get(InputType.PLACE.getValue()).setText(_place);


        if (_launch != null && _launch == 1) {
            add_launch(null);
        }
        if (_diner != null && _diner == 1) {
            add_diner(null);
        }
        if (_encas != null && _encas == 1) {
            add_encas(null);
        }
        if (_sleep != null && _sleep == 1) {
            add_sleep(null);
        }
        if (_wakeup != null && _wakeup == 1) {
            add_wakeup(null);
        }
        if (_night != null && _night == 1) {
            add_night(null);
        }
        if (_workout != null && _workout == 1) {
            add_workout(null);
        }
        if (_hypogly != null && _hypogly == 1) {
            add_hypo(null);
        }
        if (_hypergly != null && _hypergly == 1) {
            add_hyper(null);
        }
        if (_atwork != null && _atwork == 1) {
            add_work(null);
        }
        if (_athome != null && _athome == 1) {
            add_home(null);
        }
        if (_alcohol != null && _alcohol == 1) {
            add_alcohol(null);
        }
        if (_period != null && _period == 1) {
            add_period(null);
        }
        if (_breakfast != null && _breakfast == 1) {
            add_breakfeast(null);
        }

    }

    private void init_fields() {
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

    private void init_icon() {
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
            alertDialog.setMessage("Vous n'avez renseigné aucun titre. êtes vous sur de vouloir continuer ?");
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
                    Intent Entryintent = new Intent(EntryActivity.this, Carnetdesuivi.class);
                    EntryActivity.this.startActivity(Entryintent);
                    _gps.stopGPS();
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
            Intent Entryintent = new Intent(EntryActivity.this, Carnetdesuivi.class);
            EntryActivity.this.startActivity(Entryintent);
            _gps.stopGPS();
            _this.finish();
        }
    }

    @Override
    public void onBackPressed() {

        Intent Entryintent = null;

        if (_activitycycle.equals("carnet"))
             Entryintent = new Intent(EntryActivity.this, Carnetdesuivi.class);
        else if (_activitycycle.equals("infoday")) {
            Entryintent = new Intent(EntryActivity.this, DayResultActivity.class);
            Entryintent.putExtra("date", _date);
            Entryintent.putExtra("hour", _hour);
        }
        else if (_activitycycle.equals("stat"))
            Entryintent = new Intent(EntryActivity.this, StatisticsActivity.class);

        EntryActivity.this.startActivity(Entryintent);
        _gps.stopGPS();
        this.finish();



    }

    // Methode icones
    public void add_breakfeast(View v)
    {
        ImageView imgdej = (ImageView) findViewById(R.id.imgptidej);
        ImageView imglunch = (ImageView) findViewById(R.id.imglaunch);
        ImageView imgencas = (ImageView) findViewById(R.id.imgencas);
        ImageView imgdiner = (ImageView) findViewById(R.id.imgdiner);

        if (isActiveicon.get(IconeType.BREAKFAST.getValue()) == 0) {
            isActiveicon.set(IconeType.BREAKFAST.getValue(), 1);
            imgdej.setImageResource(R.drawable.ptidejgreen);

            imglunch.setImageResource(R.drawable.lunch);
            isActiveicon.set(IconeType.LAUNCH.getValue(), 0);

            imgdiner.setImageResource(R.drawable.diner);
            isActiveicon.set(IconeType.DINER.getValue(), 0);

            imgencas.setImageResource(R.drawable.cassecroute);
            isActiveicon.set(IconeType.ENCAS.getValue(), 0);
        }
        else {
            isActiveicon.set(IconeType.BREAKFAST.getValue(), 0);
            imgdej.setImageResource(R.drawable.ptidej);
        }
    }

    public void add_launch(View v)
    {
        ImageView imgdej = (ImageView) findViewById(R.id.imgptidej);
        ImageView imglunch = (ImageView) findViewById(R.id.imglaunch);
        ImageView imgencas = (ImageView) findViewById(R.id.imgencas);
        ImageView imgdiner = (ImageView) findViewById(R.id.imgdiner);

        if (isActiveicon.get(IconeType.LAUNCH.getValue()) == 0) {
            imglunch.setImageResource(R.drawable.launchgreen);
            isActiveicon.set(IconeType.LAUNCH.getValue(), 1);

            isActiveicon.set(IconeType.BREAKFAST.getValue(), 0);
            imgdej.setImageResource(R.drawable.ptidej);

            imgdiner.setImageResource(R.drawable.diner);
            isActiveicon.set(IconeType.DINER.getValue(), 0);

            imgencas.setImageResource(R.drawable.cassecroute);
            isActiveicon.set(IconeType.ENCAS.getValue(), 0);
        }
        else {
            imglunch.setImageResource(R.drawable.lunch);
            isActiveicon.set(IconeType.LAUNCH.getValue(), 0);
        }
    }

    public void add_diner(View v)
    {
        ImageView imgdej = (ImageView) findViewById(R.id.imgptidej);
        ImageView imglunch = (ImageView) findViewById(R.id.imglaunch);
        ImageView imgencas = (ImageView) findViewById(R.id.imgencas);
        ImageView imgdiner = (ImageView) findViewById(R.id.imgdiner);

        if (isActiveicon.get(IconeType.DINER.getValue()) == 0) {
            imgdiner.setImageResource(R.drawable.dinergreen);
            isActiveicon.set(IconeType.DINER.getValue(), 1);

            isActiveicon.set(IconeType.BREAKFAST.getValue(), 0);
            imgdej.setImageResource(R.drawable.ptidej);

            imglunch.setImageResource(R.drawable.lunch);
            isActiveicon.set(IconeType.LAUNCH.getValue(), 0);

            imgencas.setImageResource(R.drawable.cassecroute);
            isActiveicon.set(IconeType.ENCAS.getValue(), 0);

        }
        else {
            imgdiner.setImageResource(R.drawable.diner);
            isActiveicon.set(IconeType.DINER.getValue(), 0);
        }
    }

    public void add_encas(View v)
    {
        ImageView imgdej = (ImageView) findViewById(R.id.imgptidej);
        ImageView imglunch = (ImageView) findViewById(R.id.imglaunch);
        ImageView imgencas = (ImageView) findViewById(R.id.imgencas);
        ImageView imgdiner = (ImageView) findViewById(R.id.imgdiner);

        if (isActiveicon.get(IconeType.ENCAS.getValue()) == 0) {
            imgencas.setImageResource(R.drawable.cassecroutegreen);
            isActiveicon.set(IconeType.ENCAS.getValue(), 1);

            isActiveicon.set(IconeType.BREAKFAST.getValue(), 0);
            imgdej.setImageResource(R.drawable.ptidej);

            imglunch.setImageResource(R.drawable.lunch);
            isActiveicon.set(IconeType.LAUNCH.getValue(), 0);

            imgdiner.setImageResource(R.drawable.diner);
            isActiveicon.set(IconeType.DINER.getValue(), 0);
        }
        else {
            imgencas.setImageResource(R.drawable.cassecroute);
            isActiveicon.set(IconeType.ENCAS.getValue(), 0);
        }
    }

    public void add_sleep(View v)
    {
        ImageView imgcoucher = (ImageView) findViewById(R.id.imgcoucher);
        ImageView imglevee = (ImageView) findViewById(R.id.imglevee);

        if (isActiveicon.get(IconeType.SLEEP.getValue()) == 0) {
            imgcoucher.setImageResource(R.drawable.couchergreen);
            isActiveicon.set(IconeType.SLEEP.getValue(), 1);
            imglevee.setImageResource(R.drawable.reveil);
            isActiveicon.set(IconeType.WAKEUP.getValue(), 0);
        }
        else {
            imgcoucher.setImageResource(R.drawable.coucher);
            isActiveicon.set(IconeType.SLEEP.getValue(), 0);
        }
    }

    public void add_wakeup(View v)
    {
        ImageView imgcoucher = (ImageView) findViewById(R.id.imgcoucher);
        ImageView imglevee = (ImageView) findViewById(R.id.imglevee);

        if (isActiveicon.get(IconeType.WAKEUP.getValue()) == 0) {
            imglevee.setImageResource(R.drawable.reveilgreen);
            isActiveicon.set(IconeType.WAKEUP.getValue(), 1);
            imgcoucher.setImageResource(R.drawable.coucher);
            isActiveicon.set(IconeType.SLEEP.getValue(), 0);
        }
        else {
            imglevee.setImageResource(R.drawable.reveil);
            isActiveicon.set(IconeType.WAKEUP.getValue(), 0);
        }
    }

    public void add_night(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgnuit);

        if (isActiveicon.get(IconeType.NIGHT.getValue()) == 0) {
            img.setImageResource(R.drawable.nightgreen);
            isActiveicon.set(IconeType.NIGHT.getValue(), 1);
        }
        else {
            img.setImageResource(R.drawable.night);
            isActiveicon.set(IconeType.NIGHT.getValue(), 0);
        }
    }

    public void add_workout(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgsport);

        if (isActiveicon.get(IconeType.WORKOUT.getValue()) == 0) {
            img.setImageResource(R.drawable.sportgreen);
            isActiveicon.set(IconeType.WORKOUT.getValue(), 1);
        }
        else {
            img.setImageResource(R.drawable.sport);
            isActiveicon.set(IconeType.WORKOUT.getValue(), 0);
        }
    }

    public void add_work(View v)
    {
        ImageView imghome = (ImageView) findViewById(R.id.imghome);
        ImageView imgwork = (ImageView) findViewById(R.id.imgwork);

        if (isActiveicon.get(IconeType.WORK.getValue()) == 0) {
            imgwork.setImageResource(R.drawable.workgreen);
            isActiveicon.set(IconeType.WORK.getValue(), 1);

            imghome.setImageResource(R.drawable.home);
            isActiveicon.set(IconeType.HOME.getValue(), 0);
        }
        else {
            imgwork.setImageResource(R.drawable.work);
            isActiveicon.set(IconeType.WORK.getValue(), 0);
        }
    }

    public void add_home(View v)
    {
        ImageView imghome = (ImageView) findViewById(R.id.imghome);
        ImageView imgwork = (ImageView) findViewById(R.id.imgwork);

        if (isActiveicon.get(IconeType.HOME.getValue()) == 0) {
            imghome.setImageResource(R.drawable.homegreen);
            isActiveicon.set(IconeType.HOME.getValue(), 1);

            imgwork.setImageResource(R.drawable.work);
            isActiveicon.set(IconeType.WORK.getValue(), 0);
        }
        else {
            imghome.setImageResource(R.drawable.home);
            isActiveicon.set(IconeType.HOME.getValue(), 0);
        }
    }

    public void add_alcohol(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgalcool);

        if (isActiveicon.get(IconeType.ALCOHOL.getValue()) == 0) {
            img.setImageResource(R.drawable.alcoolgreen);
            isActiveicon.set(IconeType.ALCOHOL.getValue(), 1);
        }
        else {
            img.setImageResource(R.drawable.alcool);
            isActiveicon.set(IconeType.ALCOHOL.getValue(), 0);
        }
    }

    public void add_period(View v)
    {
        ImageView img = (ImageView) findViewById(R.id.imgperiod);

        if (isActiveicon.get(IconeType.PERIOD.getValue()) == 0) {
            img.setImageResource(R.drawable.periodgreen);
            isActiveicon.set(IconeType.PERIOD.getValue(), 1);
        }
        else {
            img.setImageResource(R.drawable.period);
            isActiveicon.set(IconeType.PERIOD.getValue(), 0);
        }
    }

    public void add_hypo(View v)
    {
        ImageView imghypo = (ImageView) findViewById(R.id.imghypo);
        ImageView imghyper = (ImageView) findViewById(R.id.imghyper);

        if (isActiveicon.get(IconeType.HYPO.getValue()) == 0) {
            imghypo.setImageResource(R.drawable.hypogreen);
            isActiveicon.set(IconeType.HYPO.getValue(), 1);

            imghyper.setImageResource(R.drawable.hyper);
            isActiveicon.set(IconeType.HYPER.getValue(), 0);
        }
        else {
            imghypo.setImageResource(R.drawable.hypo);
            isActiveicon.set(IconeType.HYPO.getValue(), 0);
        }
    }

    public void add_hyper(View v)
    {
        ImageView imghypo = (ImageView) findViewById(R.id.imghypo);
        ImageView imghyper = (ImageView) findViewById(R.id.imghyper);

        if (isActiveicon.get(IconeType.HYPER.getValue()) == 0) {
            imghyper.setImageResource(R.drawable.hypergreen);
            isActiveicon.set(IconeType.HYPER.getValue(), 1);

            imghypo.setImageResource(R.drawable.hypo);
            isActiveicon.set(IconeType.HYPO.getValue(), 0);
        }
        else {
            imghyper.setImageResource(R.drawable.hyper);
            isActiveicon.set(IconeType.HYPER.getValue(), 0);
        }
    }

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
    }

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
    }
}
