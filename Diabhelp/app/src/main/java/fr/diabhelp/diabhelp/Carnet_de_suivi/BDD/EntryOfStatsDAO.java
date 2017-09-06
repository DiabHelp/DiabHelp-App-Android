package fr.diabhelp.diabhelp.Carnet_de_suivi.BDD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import fr.diabhelp.diabhelp.Carnet_de_suivi.BDD.Ressource.EntryOfStats;


public class EntryOfStatsDAO {

    public static final String glucide = "glucide"; // y
    public static final String activity = "activity"; //
    public static final String notes = "notes"; //
    public static final String hba1c = "hba1c"; // y

    public static final String breakfast = "breakfast";
    public static final String launch = "lunch";
    public static final String diner = "diner";
    public static final String encas = "encas";
    public static final String sleep = "sleep";
    public static final String wakeup = "wakeup";
    public static final String night = "night";
    public static final String workout = "workout";
    public static final String hypogly = "hypogly";
    public static final String hypergly = "hypergly";
    public static final String work = "work";
    public static final String athome = "athome";
    public static final String alcohol = "alcohol";
    public static final String period = "period";

    public static final String DATE_DEBUT = "beg_date";
    public static final String DATE_FIN = "end_date";

    public static final String TABLE_NAME = "Diabhelp_CDS_Statistiques"
            ;
    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + breakfast + " INTEGER, " + launch + " INTEGER, " + diner + " INTEGER, " + encas + " INTEGER, " + sleep + " INTEGER, " + wakeup + " INTEGER, " + night + " INTEGER, " + workout + " INTEGER, " + hypogly + " INTEGER, " + hypergly + " INTEGER, " + work + " INTEGER, " + athome + " INTEGER, " + alcohol + " INTEGER, " + period + " INTEGER, " + DATE_DEBUT + " TEXT, " + DATE_FIN + " TEXT, " + "rdate datetime default (datetime(current_timestamp)));";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static void addStat(EntryOfStats st, SQLiteDatabase mDb) {

        ContentValues value = new ContentValues();

        Log.e("beg date addstat", "date :" + st.getBeg_date());
        value.put(DATE_DEBUT, st.getBeg_date());
        value.put(DATE_FIN, st.getEnd_date());

        value.put(breakfast, st.getBreakfast());
        value.put(launch, st.getLaunch());
        value.put(diner, st.getDiner());
        value.put(encas, st.getEncas());
        value.put(sleep, st.getSleep());
        value.put(wakeup, st.getWakeup());
        value.put(night, st.getNight());
        value.put(workout, st.getWorkout());
        value.put(hypogly, st.getHypogly());
        value.put(hypergly, st.getHypergly());
        value.put(work, st.getAtwork());
        value.put(athome, st.getAthome());
        value.put(alcohol, st.getAlcohol());
        value.put(period, st.getPeriod());

        mDb.insert(TABLE_NAME, null, value);
    }

    public static EntryOfStats selectStat(SQLiteDatabase mDb) {
        EntryOfStats m = null;


        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME, null);

        String[] i = c.getColumnNames();

        // TODO SELECT_STAT FIX_UPDATE
        // FAIRE L'UPDATE
        // ENLEVER LE WHILE

        while (c.moveToNext()) {

            if (c == null || c.getCount() <= 0) {
                Log.e("Select statistiques", "Select echoué");
                return null;
            }
            Integer _launch = c.getInt(c.getColumnIndex(launch));
            Integer _diner = c.getInt(c.getColumnIndex(diner));
            Integer _encas = c.getInt(c.getColumnIndex(encas));
            Integer _sleep = c.getInt(c.getColumnIndex(sleep));
            Integer _wakeup = c.getInt(c.getColumnIndex(wakeup));
            Integer _night = c.getInt(c.getColumnIndex(night));
            Integer _workout = c.getInt(c.getColumnIndex(workout));
            Integer _hypogly = c.getInt(c.getColumnIndex(hypogly));
            Integer _hypergly = c.getInt(c.getColumnIndex(hypergly));
            Integer _atwork = c.getInt(c.getColumnIndex(work));
            Integer _athome = c.getInt(c.getColumnIndex(athome));
            Integer _alcohol = c.getInt(c.getColumnIndex(alcohol));
            Integer _period = c.getInt(c.getColumnIndex(period));
            Integer _breakfast = c.getInt(c.getColumnIndex(breakfast));
            String _beg_date = c.getString(c.getColumnIndex(DATE_DEBUT));
            String _end_date = c.getString(c.getColumnIndex(DATE_FIN));

            m = new EntryOfStats();

            m.setBreakfast(_breakfast);
            m.setLaunch(_launch);
            m.setDiner(_diner);
            m.setEncas(_encas);
            m.setSleep(_sleep);
            m.setWakeup(_wakeup);
            m.setNight(_night);
            m.setWorkout(_workout);
            m.setHypogly(_hypogly);
            m.setHypergly(_hypergly);
            m.setAtwork(_atwork);
            m.setAthome(_athome);
            m.setAlcohol(_alcohol);
            m.setPeriod(_period);

            m.setBeg_date(_beg_date);
            m.setEnd_date(_end_date);
        }

        return m;
    }
}
