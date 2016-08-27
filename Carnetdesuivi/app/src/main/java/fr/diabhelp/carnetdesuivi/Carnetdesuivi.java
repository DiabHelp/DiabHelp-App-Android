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
import android.util.Patterns;
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
import fr.diabhelp.carnetdesuivi.API.Response.ResponseMail;
import fr.diabhelp.carnetdesuivi.API.Service.ServerUdpateService;
import fr.diabhelp.carnetdesuivi.API.Task.BddSyncrhroCDSGetAllEntriesApiCallTask;
import fr.diabhelp.carnetdesuivi.API.Task.ExportAPICallTask;
import fr.diabhelp.carnetdesuivi.BDD.DAO;
import fr.diabhelp.carnetdesuivi.BDD.EntryOfCDSDAO;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;
import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryToSend;
import fr.diabhelp.carnetdesuivi.Carnet.DayResultActivity;
import fr.diabhelp.carnetdesuivi.Carnet.EntryActivity;
import fr.diabhelp.carnetdesuivi.Carnet.ExpandableListAdapters;
import fr.diabhelp.carnetdesuivi.Carnet.Statistics.StatisticsActivity;
import fr.diabhelp.carnetdesuivi.Utils.DateMagnifier;
import fr.diabhelp.carnetdesuivi.Utils.DateUtils;
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
    public static SharedPreferences _settings = null;
    private static Carnetdesuivi mInstance;
    final int sdk = android.os.Build.VERSION.SDK_INT;
    public GridView grid;
    public GridView gridba;
    public DAO dao = null;
    //Expandable
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> laptopCollection;
    ExpandableListView expListView;
    private String idUser;
    private String token = "";
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    private ProgressDialog _progress;
    private SQLiteDatabase db = null;
    private DateMagnifier _dm;
    //export
    private Calendar myCalendar;
    private EditText[] inputdate;
    private String[] inputdateus;
    private int whichInput;
    private String myemail;
    private boolean _isConnected;

    // Net BroadCast
    public static synchronized Carnetdesuivi getInstance() {
        return mInstance;
    }

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
        idUser = _settings.getString(ID_USER, "");
        Integer launch = SharedContext.getLaunch();
        System.out.println("idUser = " + idUser);
        if (launch == 0 && !(idUser.equalsIgnoreCase("")))
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
        if (!idUser.isEmpty()) {
            String lastEditionDateEntryOfCDS = EntryOfCDSDAO.getLastEdition(idUser, db);
            if (lastEditionDateEntryOfCDS.equalsIgnoreCase("")) {
               Log.i(getLocalClassName(),"Récupération de toutes les entrées du serveur");
                displayWaitingTime();
                getRemoteEntriesOfCDS(idUser);
            } else {
                Log.i(getLocalClassName(),"vérification de retard du serveur");
                initActivity();
                Intent updateServer = new Intent(this, ServerUdpateService.class);
                updateServer.putExtra(ServerUdpateService.EXTRA_ID_USER, idUser);
                updateServer.putExtra(ServerUdpateService.EXTRA_ACTION, ServerUdpateService.UPDATE);
                startService(updateServer);
            }
        }
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

        mAll = EntryOfCDSDAO.selectAllOneday(formattedDate, idUser, db);
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

    // This fonction is needed to help user to make a good follow sheet
    // user will modify his entry if he want 2 entry in the same minute

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

        final EntryOfCDS ent = EntryOfCDSDAO.selectDay(formattedDate, Hours, idUser, db);
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
                    Entryintent.putExtra("date", ent.getDateCreation());
                    Entryintent.putExtra("fastInsu", ent.getFast_insu());
                    Entryintent.putExtra("slowInsu", ent.getSlow_insu());
                    Entryintent.putExtra("hba1c", ent.getHba1c());
                    Entryintent.putExtra("hour", ent.getHour());

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
                    Entryintent.putExtra("work", ent.getAtwork());
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

        int iYear = today[2];
        int iMonth = Calendar.FEBRUARY;
        int iDay = 1;

        Calendar mycal = new GregorianCalendar(iYear, today[1], iDay);

        mall = EntryOfCDSDAO.selectAll(idUser, db);
        System.out.println("toutes les entrées = ");
        if (mall == null)
            return null;

        DateMagnifier dt = new DateMagnifier();

        while (idx < mall.size())
        {
            String Title = mall.get(idx).getTitle();
            Title = Title.substring(0, 1).toUpperCase() + Title.substring(1);
            if (Title.length() > 20)
                Title = Title.substring(0, 20) + "..";
            String DisplayDate = Title + " - " + dt.getCleanDate(mall.get(idx).getDateCreation()) + " " + mall.get(idx).getHour() ;
            date.add(DisplayDate);
            idx++;
        }
        return date;
    }

    protected int[] getDate() {
        Calendar c = Calendar.getInstance();
        int today[] = new int[3];

        today[0] = c.get(Calendar.DAY_OF_MONTH);
        today[1]= c.get(Calendar.MONTH);
        today[2] = c.get(Calendar.YEAR);
        return today;
    }

    private void updateLabel() {

        SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATECREATION_DB_FORMAT, Locale.US);

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
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setView(alertDialogView);
        adb.setTitle("Exporter votre carnet de suivi");
        adb.setIcon(R.drawable.diab_logo);

        //On affecte un bouton "OK" à notre AlertDialog et on lui affecte un évènement
        inputdate = new EditText[2];
        myCalendar = Calendar.getInstance();

        final EditText begin = (EditText)alertDialogView.findViewById(R.id.date_begin);
        final EditText ending = (EditText)alertDialogView.findViewById(R.id.date_end);

        begin.setFocusable(false);
        ending.setFocusable(false);

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

        ending.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                whichInput = 1;
                new DatePickerDialog(Carnetdesuivi.this, datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        inputdate[0] = begin;
        inputdate[1] = ending;

        adb.setPositiveButton("Envoyer par mail", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        adb.setNegativeButton("Annuler",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });
        final AlertDialog dialog = adb.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText mail = (EditText) alertDialogView.findViewById(R.id.mail_addr);
                TextView error = (TextView) alertDialogView.findViewById(R.id.error_message);
                System.out.println("cacaprout");
                try {
                    //Lorsque l'on cliquera sur le bouton "OK", on récupère l'EditText correspondant à notre vue personnalisée (cad à alertDialogView)
//                EditText beg = (EditText)alertDialogView.findViewById(R.id.date_begin);
//                EditText end = (EditText)alertDialogView.findViewById(R.id.date_end);

                    SimpleDateFormat sdf = new SimpleDateFormat(DateUtils.DATECREATION_DB_FORMAT);
                    if (inputdateus[0] == null && inputdateus[1] == null)
                        throw new NoSuchFieldException(getString(R.string.export_period_required));
                    else if (inputdateus[0] == null)
                        throw new NoSuchFieldException(getString(R.string.export_begin_date_required));
                    else if (inputdateus[1] == null)
                        throw new NoSuchFieldException(getString(R.string.export_end_date_required));
                    else {
                        if (sdf.parse(inputdateus[0]).after(new Date()))
                            throw new NoSuchFieldException(getString(R.string.export_begin_date_incorrect));
                        else if (sdf.parse(inputdateus[1]).after(new Date()))
                            throw new NoSuchFieldException(getString(R.string.export_end_date_incorrect));
                        else if (sdf.parse(inputdateus[0]).after(sdf.parse(inputdateus[1])))
                            throw new NoSuchFieldException(getString(R.string.export_end_date_before_begin_date));
                        else {
                            ArrayList<EntryToSend> entryOfCDSes = EntryOfCDSDAO.selectBetweenDaysToSend(inputdateus[0], inputdateus[1], idUser, Carnetdesuivi.this.db);
                            if (mail.getText().toString().isEmpty())
                                myemail = null;
                            else {
                                myemail = mail.getText().toString();
                                if (myemail.matches(Patterns.EMAIL_ADDRESS.pattern()) == false)
                                    throw new NoSuchFieldException(getString(R.string.mail_incorrect));

                            }
                            if (entryOfCDSes != null && !entryOfCDSes.isEmpty()) {
                                System.out.println("context = [" + Carnetdesuivi.this + "] listener = [" + (IApiCallTask) Carnetdesuivi.this + "] entries = [" + entryOfCDSes + "]");
                                new ExportAPICallTask(Carnetdesuivi.this, (IApiCallTask) Carnetdesuivi.this, idUser, entryOfCDSes).execute(myemail);
                                dialog.dismiss();
                            } else {
                                throw new NoSuchFieldException("Il n'y a pas d'entrées sur cette periode");
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    error.setVisibility(View.VISIBLE);
                } catch (NoSuchFieldException e) {
                    error.setVisibility(View.VISIBLE);
                    error.setText(e.getMessage());
                }
            }
        });

    }

    @Override
    public void onBackgroundTaskCompleted(Object response, String action, ProgressDialog progress)
    {
        _progress = progress;
        System.out.println("progress = " + progress);
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
                }
                else {
                    Log.i(getLocalClassName(), "pas d'entrées sur le serveur");
                }
                _progress.dismiss();
            }
            initActivity();
        }
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
        SharedContext.setLaunch(0);
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

    public enum Error{
        NONE,
        SERVER_ERROR,
        MAIL_NOT_SENT,
        INVALID_TOKEN
    }
}
