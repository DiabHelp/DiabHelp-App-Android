package fr.diabhelp.carnetdesuivi;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


import fr.diabhelp.carnetdesuivi.API.IApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSGetAllEntries;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSGetLastEdition;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseCDSetMissingEntries;
import fr.diabhelp.carnetdesuivi.API.Response.ResponseMail;
import fr.diabhelp.carnetdesuivi.API.ServerUdpateService;
import fr.diabhelp.carnetdesuivi.API.Task.BddSyncrhroCDSGetAllEntriesApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Task.BddSyncrhroCDSGetLastEditionApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Task.BddSyncrhroCDSetMissingEntriesApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Task.ExportAPICallTask;

import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.Carnet.DayResultActivity;
import fr.diabhelp.carnetdesuivi.Carnet.EntryActivity;
import fr.diabhelp.carnetdesuivi.Carnet.ExpandableListAdapters;
import fr.diabhelp.carnetdesuivi.Carnet.Statistics.StatisticsActivity;
import fr.diabhelp.carnetdesuivi.Utils.DateMagnifier;
import fr.diabhelp.carnetdesuivi.Utils.MyToast;
import fr.diabhelp.carnetdesuivi.Utils.NetBroadcast.ConnectivityReceiver;
import fr.diabhelp.carnetdesuivi.Utils.SharedContext;

/**
 * Created by naqued on 10/11/15.
 */
public class Carnetdesuivi extends AppCompatActivity implements IApiCallTask, ConnectivityReceiver.ConnectivityReceiverListener {

    public static final String PREF_FILE = "ConnexionActivityPreferences";
    public static final String TOKEN = "token";
    public static final String ID_USER = "id_user";

    public static final String TYPE_USER = "role";
    private String token = "";
    public static SharedPreferences _settings = null;
    public GridView grid;
    public GridView gridba;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private ProgressDialog _progress;
    public DAO dao = null;
    private SQLiteDatabase db = null;
    private DateMagnifier _dm;
    final int sdk = android.os.Build.VERSION.SDK_INT;

    //export
    private Calendar myCalendar;
    private EditText[] inputdate;
    private String[] inputdateus;
    private int whichInput;
    private String myemail;

    //Expandable
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;

    private static Carnetdesuivi mInstance;
    private boolean _isConnected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = DAO.getInstance(this);
        db = dao.open();
        try {
            SharedContext.setContext(getApplicationContext().createPackageContext(
                    "fr.diabhelp.diabhelp",
                    Context.MODE_PRIVATE));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("launch = " + SharedContext.getLaunch());
        _settings = SharedContext.getSharedContext().getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        if ((SharedContext.getLaunch() == 0) && !(_settings.getString(ID_USER, "").equalsIgnoreCase("")))
        {
            SharedContext.setLaunch(1);
            synchronizeDb(db);
        }
        else
            initActivity();
    }

    private void displayWaitingTime() {
        _progress = new ProgressDialog(this);
        _progress.setCancelable(false);
        _progress.setMessage(getString(R.string.synchro_bd));
        _progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        _progress.show();
    }

    public void initActivity()
    {
        setContentView(R.layout.activity_carnet);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Carnet de Suivi");
        setSupportActionBar(toolbar);
        _dm = new DateMagnifier();
        inputdateus = new String[2];
        mInstance = this;
        TextView noEntry = (TextView) findViewById(R.id.Noentry);

        createGroupList();
        createCollection();


        if (groupList == null) {
            noEntry.setEnabled(true);
            noEntry.setVisibility(View.VISIBLE);
            return;
        }

        expListView = (ExpandableListView) findViewById(R.id.expandableListView);
        final ExpandableListAdapters expListAdapter = new ExpandableListAdapters(
                this, groupList, laptopCollection, this);
        expListView.setAdapter(expListAdapter);

        if (groupList != null && groupList.size() > 0) {
            noEntry.setEnabled(false);
            noEntry.setVisibility(View.INVISIBLE);
            expListView.expandGroup(0);
            if (groupList.size() > 1)
                expListView.expandGroup(1);
        }


        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v,
                                        final int groupPosition, int childPosition, long id) {
                final String selected = (String) expListAdapter.getChild(
                        groupPosition, childPosition);

                View child = expListAdapter.getChildView(groupPosition, childPosition, false, v, parent);
                HorizontalScrollView hz = (HorizontalScrollView) child.findViewById(R.id.horizontalScrollView2);
                hz.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        Intent intent = new Intent(Carnetdesuivi.this, DayResultActivity.class);


                        String _title = (String) expListAdapter.getGroup(groupPosition);
                        String datesplit = _title;
                        String Hour = _title.split("-")[1].split(" ")[4];

                        intent.putExtra("date", datesplit);
                        intent.putExtra("hour", Hour);
                        Carnetdesuivi.this.startActivity(intent);
                        Carnetdesuivi.this.finish();
                        return true;
                    }
                });
                hz.callOnClick();
                return true;
            }

            ;

        });
        fillAverageGly();
    }

    private void synchronizeDb(SQLiteDatabase db) {

        String idUser = _settings.getString(ID_USER, "");
        if (!idUser.isEmpty())
        {
            String lastEditionDateEntryOfCDC = EntryOfCDSDAO.getLastEdition(idUser, db);
            System.out.println("LAST EDITION = " + lastEditionDateEntryOfCDC);
            if (lastEditionDateEntryOfCDC.equalsIgnoreCase("")) {
                displayWaitingTime();
                getRemoteEntriesOfCDS(idUser);
            }
            else
            {
                initActivity();
                Intent updateServer = new Intent(this, ServerUdpateService.class);
                updateServer.putExtra(ServerUdpateService.EXTRA_ID_USER, _settings.getString(ID_USER, ""));
                startService(updateServer);
               // checkIfServerIsUpToDate(idUser);
            }
        }
    }

    private void checkIfServerIsUpToDate(String idUser) {
        new BddSyncrhroCDSGetLastEditionApiCallTask(this, getApplicationContext()).execute(idUser);
    }

    private void getRemoteEntriesOfCDS(String idUser) {
        new BddSyncrhroCDSGetAllEntriesApiCallTask(this, getApplicationContext()).execute(idUser);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void fillAverageGly() {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();
        int idx = 0;
        int total = 0;

        TextView gly = (TextView)
                findViewById(R.id.avgglytextView);

        RelativeLayout glyl = (RelativeLayout) findViewById(R.id.relativeavg);

        Calendar c = Calendar.getInstance();

        String myFormat ="MM-dd-yyyy";
        SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);

        String formattedDate = df.format(c.getTime());

        mAll = EntryOfCDSDAO.selectAllOneday(formattedDate, _settings.getString(ID_USER, ""), db);
        int size = 0;
        if (mAll.size() > 0) {
            while (idx < mAll.size()) {
                if (mAll.get(idx).getglycemy() != 0) {
                    total += mAll.get(idx).getglycemy();
                    size++;
                }
                idx++;
            }
            if (size != 0)
                total = total / size;
            else
                total = 0;
            if (total >= 80 && total <= 120) {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    glyl.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.round_cornerglyclean));
                } else {
                    glyl.setBackground(this.getResources().getDrawable(R.drawable.round_cornerglyclean));
                }
            } else {
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    gly.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.round_cornerglynoclean));
                } else {
                    gly.setBackground(this.getResources().getDrawable(R.drawable.round_cornerglynoclean));
                }
            }
            gly.setText(String.valueOf(total) + "\nmg/dl");
        } else {
            gly.setText("-" + "\nmg/dl");
            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                gly.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.round_cornerglycleannothing));
            } else {
                gly.setBackground(this.getResources().getDrawable(R.drawable.round_cornerglycleannothing));
            }
        }
    }

    private void createGroupList() {
        groupList = new ArrayList<String>();
        groupList = fillDate();
        Collections.reverse(groupList);
    }

    // Preparing collection
    private void createCollection() {
        laptopCollection = new LinkedHashMap<String, List<String>>();
        if (groupList != null) {
            for (String date : groupList) {
                loadChild(fillValueDay(date.split(" ")[0], date.split(" ")[1]));
                laptopCollection.put(date, childList);
            }
        }
    }

    // This fonction is needed to help user to make a good follow sheet
    // user will modify his entry if he want 2 entry in the same minute

    private boolean checkTooFast()
    {
        String _minute;
        int hours = new Time(System.currentTimeMillis()).getHours();
        int minutes = new Time(System.currentTimeMillis()).getMinutes();
        if (minutes < 10)
            _minute = "0" + String.valueOf(minutes);
        else
            _minute = String.valueOf(minutes);
        String Hours = String.valueOf(hours) + ":" + _minute;

        Calendar c = Calendar.getInstance();

        String myFormat ="MM-dd-yyyy";
        SimpleDateFormat df = new SimpleDateFormat(myFormat, Locale.US);
        String formattedDate = df.format(c.getTime());

        final EntryOfCDS ent = EntryOfCDSDAO.selectDay(formattedDate, Hours, _settings.getString(ID_USER, ""), db);
        if (ent != null)
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Attention");
            alertDialog.setMessage("Par soucis de clarté, vous ne pouvez ajouter une nouvelle entrée.. Vous pouvez modifier votre dernière entrée si vous le souhaitez..\nVoulez vous modifier votre dernière entrée ?");
            alertDialog.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent Entryintent = new Intent(Carnetdesuivi.this, EntryActivity.class);

                    Entryintent.putExtra("title", ent.getTitle());
                    Entryintent.putExtra("place", ent.getPlace());
                    Entryintent.putExtra("glucide", ent.getGlucide());
                    Entryintent.putExtra("activity", ent.getActivity());
                    Entryintent.putExtra("activityType", ent.getActivityType());
                    Entryintent.putExtra("notes", ent.getNotes());
                    Entryintent.putExtra("date", ent.getDate());
                    Entryintent.putExtra("fast_insu", ent.getFast_insu());
                    Entryintent.putExtra("slow_insu", ent.getSlow_insu());
                    Entryintent.putExtra("hba1c", ent.getHba1c());
                    Entryintent.putExtra("hour", ent.getHour());
                    Entryintent.putExtra("date", ent.getDate());

                    Entryintent.putExtra("glycemy", ent.getglycemy());

                    Entryintent.putExtra("launch", ent.getLaunch());
                    Entryintent.putExtra("diner", ent.getDiner());
                    Entryintent.putExtra("encas", ent.getEncas());
                    Entryintent.putExtra("sleep", ent.getSleep());
                    Entryintent.putExtra("wakeup", ent.getWakeup());
                    Entryintent.putExtra("night", ent.getNight());
                    Entryintent.putExtra("workout", ent.getWorkout());
                    Entryintent.putExtra("hypogly", ent.getHypogly());
                    Entryintent.putExtra("hypergly", ent.getHypergly());
                    Entryintent.putExtra("atwork", ent.getAtwork());
                    Entryintent.putExtra("athome", ent.getAthome());
                    Entryintent.putExtra("alcohol", ent.getAlcohol());
                    Entryintent.putExtra("period", ent.getPeriod());
                    Entryintent.putExtra("breakfast", ent.getBreakfast());
                    Entryintent.putExtra("activity", "carnet");

                    Carnetdesuivi.this.startActivity(Entryintent);
                }
            });
            alertDialog.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    return ;
                }
            });
            alertDialog.show();
        }
        else {
            Intent Entryintent = new Intent(Carnetdesuivi.this, EntryActivity.class);
            Entryintent.putExtra("activity", "carnet");
            Carnetdesuivi.this.startActivity(Entryintent);
            Carnetdesuivi.this.finish();
        }
        return true;
    }

    public void launch_entry(View v) {
        checkTooFast();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.statistics:
                launch_statistics();
                return true;
            case R.id.export:
                exportPdf();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void launch_statistics() {
        Intent Statsintent = new Intent(Carnetdesuivi.this, StatisticsActivity.class);
        Carnetdesuivi.this.startActivity(Statsintent);
        this.finish();
    }

    private void loadChild(String[] laptopModels) {
        childList = new ArrayList<String>();
        for (String model : laptopModels)
            childList.add(model);
    }


    // Convert pixel to dip
    public int getDipsFromPixel(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    protected String[] fillValueDay(String dateVal, String hour) {
        String value[] = new String[1];
        return value;
    }

    protected ArrayList<String> fillDate() {
        ArrayList<String> date = new ArrayList<String>();
        ArrayList<EntryOfCDS> mall = null;

        int today[] = getDate();
        int idx = 0;

        int iYear = 2016; //TODO attention a rendre dynamique
        int iMonth = Calendar.FEBRUARY;
        int iDay = 1;

        Calendar mycal = new GregorianCalendar(iYear, today[1], iDay);

        // Get the number of days in that month
        mall = EntryOfCDSDAO.selectAll(_settings.getString(ID_USER, ""), db);
        if (mall == null)
            return null;

        DateMagnifier dt = new DateMagnifier();

        while (idx < mall.size())
        {
            String Title = mall.get(idx).getTitle();
            // TODO faire un uppercase sur le titre
            if (Title.length() > 20)
                Title = Title.substring(0, 20) + "..";
            String DisplayDate = Title + " - " + dt.getCleanDate(mall.get(idx).getDate()) + " " + mall.get(idx).getHour() ;
            date.add(DisplayDate);
            idx++;
        }
        return date;
    }

    protected int[] getDate() {
        Calendar c = Calendar.getInstance();
        int daymonth[] = new int[2];

        daymonth[0] = c.get(Calendar.DAY_OF_MONTH);
        daymonth[1]= c.get(Calendar.MONTH);
        return daymonth;
    }


    private void updateLabel() {
        String myFormat = "MM-dd-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        inputdateus[whichInput] = sdf.format(myCalendar.getTime());
        inputdate[whichInput].setText(_dm.getCleanDate(sdf.format(myCalendar.getTime())));
    }

    public void exportPdf()
    {
        LayoutInflater factory = LayoutInflater.from(this);
        checkConnection();
        if (_isConnected == false)
        {
            Toast.makeText(this, "Vous ne pouvez pas accéder à cette fonctionnalité car vous n'êtes pas connécté. Veuillez vous connécter", Toast.LENGTH_LONG).show();
            return ;
        }
        final View alertDialogView = factory.inflate(R.layout.alert_export, null);
        AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Exporter votre carnet de suivi");
        adb.setIcon(R.drawable.diab_logo);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        inputdate = new EditText[2];
        myCalendar = Calendar.getInstance();

        EditText begin = (EditText)alertDialogView.findViewById(R.id.date_begin);
        final EditText endin = (EditText)alertDialogView.findViewById(R.id.date_end);

        begin.setFocusable(false);
        endin.setFocusable(false);

        final DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };


        begin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whichInput = 0;
                new DatePickerDialog(Carnetdesuivi.this, datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        endin.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whichInput = 1;
                new DatePickerDialog(Carnetdesuivi.this, datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        inputdate[0] = begin;
        inputdate[1] = endin;

        adb.setPositiveButton("Envoyer par mail", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
/*                EditText beg = (EditText)alertDialogView.findViewById(R.id.date_begin);
                EditText end = (EditText)alertDialogView.findViewById(R.id.date_end);*/
                EditText mail = (EditText)alertDialogView.findViewById(R.id.mail_addr);

                if (inputdateus[0].isEmpty())
                    Toast.makeText(Carnetdesuivi.this, "La date de début n'a pas été remplis", Toast.LENGTH_SHORT).show();
                else if (inputdateus[1].isEmpty())
                    Toast.makeText(Carnetdesuivi.this, "La date de fin n'a pas été remplis", Toast.LENGTH_SHORT).show();
                else {
                    ArrayList<EntryOfCDS> entryOfCDSes = EntryOfCDSDAO.selectBetweenDays(inputdateus[0], inputdateus[1], _settings.getString(ID_USER, ""), Carnetdesuivi.this.db);
                    if (mail.getText().toString().isEmpty()) {
                        myemail = null;
                    }
                    else
                        myemail = mail.getText().toString();
                    if (entryOfCDSes != null && !entryOfCDSes.isEmpty()) {
                        System.out.println("context = [" + Carnetdesuivi.this + "] listener = [" + (IApiCallTask) Carnetdesuivi.this + "] entries = [" + entryOfCDSes + "]");
                        new ExportAPICallTask(Carnetdesuivi.this, (IApiCallTask) Carnetdesuivi.this, entryOfCDSes).execute(token);
                    }
                    else {
                        MyToast.getInstance().displayWarningMessage("Il n'y a pas d'entrées sur cette periode", Toast.LENGTH_LONG, Carnetdesuivi.this);
                    }
                }

                //On affiche dans un Toast le texte contenu dans l'EditText de notre AlertDialog

            } });

        adb.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        adb.show();
    }

    @Override
    public void onBackgroundTaskCompleted(Object response, String action, ProgressDialog progress)
    {
        Carnetdesuivi.Error error;
        if (action.equalsIgnoreCase("informOfsending"))
        {
            ResponseMail reponse = (ResponseMail) response;
            error = reponse.getError();
            if (error != Error.NONE)
                manageError(error);
            else
                informSuccess();
        }
        else if (action.equalsIgnoreCase("getEntries"))
        {
            ResponseCDSGetAllEntries reponse = (ResponseCDSGetAllEntries) response;
            error = reponse.getError();
            if (error != Error.NONE)
                manageError(error);
            else
            {
                List<EntryOfCDS> entries = reponse.getEntries();
                if (entries != null)
                {
                    for (EntryOfCDS entry : entries) {
                        EntryOfCDSDAO.addDay(entry, db);
                    }
                    //TODO set les entries récupérées dans la base et dans la list
                }
                else {
                    Log.i(getLocalClassName(), "pas d'entrées sur le serveur");
                }
                _progress.dismiss();
            }
            initActivity();
        }
        else if (action.equalsIgnoreCase("getLastEdition"))
        {
            ResponseCDSGetLastEdition reponse = (ResponseCDSGetLastEdition) response;
            error = reponse.getError();
            if (error != Error.NONE) {
                Log.e("getLastEdition", error.toString());
            }
            else
                compareDates(reponse.getLastEdition());
        }
        else if (action.equalsIgnoreCase("setMissingEntries"))
        {
            ResponseCDSetMissingEntries reponse = (ResponseCDSetMissingEntries) response;
            error = reponse.getError();
            if (error != Error.NONE)
                Log.e("setMissingEntries", error.toString());
        }
    }

    private void compareDates(Date lastEditionServer) {
        String lastEditionLocalStr = EntryOfCDSDAO.getLastEdition(_settings.getString(ID_USER, ""), db);
        try {
            Date lastEditionLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(lastEditionLocalStr);
            if (lastEditionServer.before(lastEditionLocal))
                sendMissingEntries(lastEditionServer.toString(), lastEditionLocal.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void sendMissingEntries(String from, String to) {
        ArrayList<EntryOfCDS> missingEntries = EntryOfCDSDAO.selectBetweenDays(from, to, _settings.getString(ID_USER, ""), db);
        if (missingEntries.isEmpty())
            _progress.dismiss();
        else
            new BddSyncrhroCDSetMissingEntriesApiCallTask(this, getApplicationContext(), missingEntries).execute();
    }

    private void informSuccess() {
        _progress.dismiss();
        Snackbar snackbar = Snackbar.make(findViewById(R.id.main_layout), getString(R.string.sending_success),Snackbar.LENGTH_LONG);
    }

    private void manageError(Carnetdesuivi.Error error) {
        _progress.dismiss();
        switch (error)
        {
            case MAIL_NOT_SENT:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_server), Toast.LENGTH_LONG, this);
                Log.e(getLocalClassName(), "Une erreur s'est produite durant l'envoi du mail");
                break;
            }
            case SERVER_ERROR:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_server), Toast.LENGTH_LONG, this);
                Log.e(getLocalClassName(), "Problème survenu lors de la connexion au serveur");
                break;
            }
            case INVALID_TOKEN:{
                MyToast.getInstance().displayWarningMessage(getString(R.string.error_server), Toast.LENGTH_LONG, this);
                Log.e(getLocalClassName(), "Le token est invalide");
                break;
            }
        }
    }

    public enum Error{
        NONE,
        SERVER_ERROR,
        MAIL_NOT_SENT,
        INVALID_TOKEN
    }

    // Net BroadCast
    public static synchronized Carnetdesuivi getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }

    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        String message = null;

        _isConnected = isConnected;

        if (isConnected) {
            Log.e("Connexion stat", "ok");
        } else {
            message = "Sorry! Not connected to internet";
            Toast.makeText(this,(String) message,
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        //Carnetdesuivi.getInstance().setConnectivityListener(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
        _isConnected = false;
    }
}
