package fr.diabhelp.carnetdesuivi.BDD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import fr.diabhelp.carnetdesuivi.BDD.Ressource.EntryOfCDS;

/**
 * Created by Sumbers on 29/06/2016.
 */
public class EntryOfCDSDAO {

    public static final String Titre = "title";
    public static final String Lieux = "place";
    public static final String Date_hour = "date_hour";
    public static final String glucide = "glucide";
    public static final String activity = "activity";
    public static final String activityType = "activity_type";
    public static final String notes = "notes";
    public static final String fast_insu = "fast_insu";
    public static final String slow_insu = "slow_insu";
    public static final String glycemy = "glycemy";

    public static final String tdate = "date";
    public static final String hba1c = "hba1c";
    public static final String Hour = "hour";

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
    public static final String sqldate = "rdate";
    public static final String dateEdition = "date_edition";
    public static final String idUser = "user_id";

    public static final String TABLE_NAME = "Diabhelp_CDS";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + idUser + " INTEGER," + tdate + " TEXT, " + Titre + " TEXT, " + Lieux + " TEXT, " + Date_hour + " TEXT, " + glucide + " DOUBLE, " + activity + " TEXT, " + activityType + " TEXT, " + notes + " TEXT, " + fast_insu + " DOUBLE, " + slow_insu + " DOUBLE, " + hba1c + " DOUBLE, " + Hour + " TEXT, " + glycemy + " DOUBLE, " + breakfast + " INTEGER, " + launch + " INTEGER, " + diner + " INTEGER, " + encas + " INTEGER, " + sleep + " INTEGER, " + wakeup + " INTEGER, " + night + " INTEGER, " + workout + " INTEGER, " + hypogly + " INTEGER, " + hypergly + " INTEGER, " + work + " INTEGER, " + athome + " INTEGER, " + alcohol + " INTEGER, " + period + " INTEGER, " + "rdate datetime default (datetime(current_timestamp)), " + dateEdition + " default (datetime(current_timestamp)));";
    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static void addDay(EntryOfCDS m, SQLiteDatabase mDb) {
        ContentValues value = new ContentValues();
        value.put(Titre, m.getTitle());
        value.put(Lieux, m.getPlace());
        value.put(Date_hour, m.getDate());
        value.put(glucide, m.getGlucide());
        value.put(activity, m.getActivity());
        value.put(activityType, m.getActivityType());
        value.put(notes, m.getNotes());
        value.put(slow_insu, m.getSlow_insu());
        value.put(fast_insu, m.getFast_insu());
        value.put(hba1c, m.getHba1c());
        value.put(Hour, m.getHour());
        value.put(glycemy, m.getglycemy());
        value.put(tdate, m.getDate());

        value.put(breakfast, m.getBreakfast());
        value.put(launch, m.getLaunch());
        value.put(diner, m.getDiner());
        value.put(encas, m.getEncas());
        value.put(sleep, m.getSleep());
        value.put(wakeup, m.getWakeup());
        value.put(night, m.getNight());
        value.put(workout, m.getWorkout());
        value.put(hypogly, m.getHypogly());
        value.put(hypergly, m.getHypergly());
        value.put(work, m.getAtwork());
        value.put(athome, m.getAthome());
        value.put(period, m.getPeriod());
        value.put(alcohol, m.getAlcohol());
        value.put(idUser, m.getIdUser());
        value.put(dateEdition, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));
        mDb.insert(TABLE_NAME, null, value);
    }

    public static void deleteDay(String _date, String _hour, SQLiteDatabase mDb) {
        mDb.delete(TABLE_NAME, tdate + " = ? and " + Hour + " = ?", new String[] {_date, _hour});
    }

    public static void update(EntryOfCDS m, SQLiteDatabase mDb) {
        ContentValues value = new ContentValues();

        value.put(Titre, m.getTitle());
        value.put(Lieux, m.getPlace());
        value.put(Date_hour, m.getDate());
        value.put(Hour, m.getHour());
        value.put(glucide, m.getGlucide());
        value.put(activity, m.getActivity());
        value.put(activityType, m.getActivityType());
        value.put(notes, m.getNotes());
        value.put(tdate, m.getDate());
        value.put(fast_insu, m.getFast_insu());
        value.put(slow_insu, m.getSlow_insu());
        value.put(hba1c, m.getHba1c());
        value.put(glycemy, m.getglycemy());

        value.put(breakfast, m.getBreakfast());
        value.put(launch, m.getLaunch());
        value.put(diner, m.getDiner());
        value.put(encas, m.getEncas());
        value.put(sleep, m.getSleep());
        value.put(wakeup, m.getWakeup());
        value.put(night, m.getNight());
        value.put(workout, m.getWorkout());
        value.put(hypogly, m.getHypogly());
        value.put(hypergly, m.getHypergly());
        value.put(work, m.getAtwork());
        value.put(athome, m.getAthome());
        value.put(period, m.getPeriod());
        value.put(alcohol, m.getAlcohol());
        value.put(dateEdition, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime()));

        mDb.update(TABLE_NAME, value, Date_hour + " = ?" + " and " + Hour + " = ?", new String[]{String.valueOf(m.getDate()), m.getHour() });
    }

    public static ArrayList<EntryOfCDS> selectBetweenDays(String mtDate, String endMtdate, String idUser, SQLiteDatabase mDb ) {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + tdate + " BETWEEN ? AND ? AND " + idUser + " = ?" , new String[] { mtDate, endMtdate, idUser} );
        if (c == null)
            Log.e("Status SelectBetweenDay", "False");

        while (c.moveToNext()) {

            String rdate = c.getString(c.getColumnIndex("rdate"));

            String _title = c.getString(c.getColumnIndex(Titre));
            String _place = c.getString(c.getColumnIndex(Lieux));
            Double _glucide = c.getDouble(c.getColumnIndex(glucide));
            String _activity = c.getString(c.getColumnIndex(activity));

            String _activityType = c.getString(c.getColumnIndex(activityType));

            String _notes = c.getString(c.getColumnIndex(notes));

            String _date = c.getString(c.getColumnIndex(Date_hour));

            Double _fast_insu = c.getDouble(c.getColumnIndex(fast_insu));
            Double _slow_insu = c.getDouble(c.getColumnIndex(slow_insu));
            Double _hba1c = c.getDouble(c.getColumnIndex(hba1c));
            Double _glycemy = c.getDouble(c.getColumnIndex(glycemy));
            String _hour = c.getString(c.getColumnIndex(Hour));

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

            EntryOfCDS m = new EntryOfCDS(_date);
            m.setDate(_date);
            m.setActivity(_activity);
            m.setActivityType(_activityType);
            m.setFast_insu(_fast_insu);
            m.setGlucide(_glucide);
            m.setNotes(_notes);
            m.setSlow_insu(_slow_insu);
            m.setTitle(_title);
            m.setPlace(_place);
            m.setHba1c(_hba1c);
            m.setHour(_hour);
            m.setglycemy(_glycemy);

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

            mAll.add(m);

        }
        c.close();
        return mAll;
    }

    public static ArrayList<EntryOfCDS> selectAll(String id, SQLiteDatabase mDb)
    {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();
        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " WHERE " + idUser +" = ?"  , new String[] {id});

        while (c.moveToNext()) {

            if (c == null || c.getCount() <= 0)
                return null;

            String _title = c.getString(c.getColumnIndex(Titre));
            String _place = c.getString(c.getColumnIndex(Lieux));
            Double _glucide = c.getDouble(c.getColumnIndex(glucide));
            String _activity = c.getString(c.getColumnIndex(activity));

            String _activityType = c.getString(c.getColumnIndex(activityType));

            String _notes = c.getString(c.getColumnIndex(notes));

            String _date = c.getString(c.getColumnIndex(Date_hour));
            Double _fast_insu = c.getDouble(c.getColumnIndex(fast_insu));
            Double _slow_insu = c.getDouble(c.getColumnIndex(slow_insu));
            Double _hba1c = c.getDouble(c.getColumnIndex(hba1c));
            Double _glycemy = c.getDouble(c.getColumnIndex(glycemy));
            String _hour = c.getString(c.getColumnIndex(Hour));

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

            String _datesql = c.getString(c.getColumnIndex(sqldate));

            EntryOfCDS m = new EntryOfCDS(_date);
            m.setDate(_date);
            m.setDatesql(_datesql);
            m.setActivity(_activity);
            m.setActivityType(_activityType);
            m.setFast_insu(_fast_insu);
            m.setGlucide(_glucide);
            m.setNotes(_notes);
            m.setSlow_insu(_slow_insu);
            m.setTitle(_title);
            m.setPlace(_place);
            m.setHba1c(_hba1c);
            m.setHour(_hour);
            m.setglycemy(_glycemy);

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


            mAll.add(m);
        }
        return mAll;
    }

    public static ArrayList<EntryOfCDS> selectAllOneday(String mtDate, String id, SQLiteDatabase mDb)
    {
        EntryOfCDS m = null;
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + Date_hour + " = ? AND " + idUser + " = ?"  , new String[] { mtDate, id});

        String[] i = c.getColumnNames();

        while (c.moveToNext()) {

            if (c == null || c.getCount() <= 0)
                return null;
            String _date = c.getString(c.getColumnIndex(Date_hour));
            Double _glycemy = c.getDouble(c.getColumnIndex(glycemy));

            m = new EntryOfCDS(_date);
            m.setDateApi(mtDate);
            m.setglycemy(_glycemy);
            mAll.add(m);
        }
        return mAll;
    }

    public static EntryOfCDS selectDay(String mtDate, String _hour, String id, SQLiteDatabase mDb) {
        EntryOfCDS m = null;
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        if (_hour == null)
            _hour = "00h00";
        if (mtDate == null)
            mtDate = "0-0-0";
        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + Date_hour + " = ?" + " and " + Hour + " = ? AND " + idUser + " = ?" , new String[] { mtDate, _hour, id});

        String[] i = c.getColumnNames();

        c.moveToNext();

        if (c == null || c.getCount() <= 0)
            return null;

        String _title = c.getString(c.getColumnIndex(Titre));
        String _place = c.getString(c.getColumnIndex(Lieux));
        Double _glucide = c.getDouble(c.getColumnIndex(glucide));
        String _activity = c.getString(c.getColumnIndex(activity));
        String _hours = c.getString(c.getColumnIndex(Hour));
        String _activityType = c.getString(c.getColumnIndex(activityType));

        String _notes = c.getString(c.getColumnIndex(notes));

        String _date = c.getString(c.getColumnIndex(Date_hour));

        Double _fast_insu = c.getDouble(c.getColumnIndex(fast_insu));
        Double _slow_insu = c.getDouble(c.getColumnIndex(slow_insu));
        Double _hba1c = c.getDouble(c.getColumnIndex(hba1c));
        Double _glycemy = c.getDouble(c.getColumnIndex(glycemy));
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

        m = new EntryOfCDS(_date);
        m.setDateApi(mtDate);
        m.setActivity(_activity);
        m.setActivityType(_activityType);
        m.setDate(_date);
        m.setHour(_hours);
        m.setFast_insu(_fast_insu);
        m.setGlucide(_glucide);
        m.setNotes(_notes);
        m.setSlow_insu(_slow_insu);
        m.setTitle(_title);
        m.setPlace(_place);
        m.setHba1c(_hba1c);
        m.setglycemy(_glycemy);

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

        mAll.add(m);

        return m;
    }

    public static ArrayList<EntryOfCDS> selectDayDateAndIcone(String startDate, String endDate,
                                                              String __breakfast, String __launch, String __diner, String __encas, String __sleep, String __wakeup, String __night, String __workout, String __hypogly, String __hypergly, String __work, String __athome, String __alcohol, String __period, SQLiteDatabase mDb) {
        EntryOfCDS m = null;
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        if (startDate == null)
            startDate = "0-0-0";
        if (endDate == null)
            endDate = "0-0-0";

        String[] args = { startDate, endDate, "", "", "", "", "", "", "", "", "", "", "", "",}; // __breakfast, __launch, __diner, __encas, __sleep, __wakeup, __night, __workout, __hypogly, __hypergly, __work, __athome };
        int idx = 2;

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + tdate + " BETWEEN ? AND ?";

        if (__breakfast.equals("1") == true) {
            if (idx == 2)
                query += " AND (";
            query += breakfast + " = ? ";
            args[idx++] = __breakfast;
        }

        if (__launch.equals("1") == true) {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += launch + " = ? ";
            args[idx++] = __launch;
        }

        if (__diner.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += diner + " = ? ";
            args[idx++] = __diner;
        }

        if (__encas.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += encas + " = ? ";
            args[idx++] = __encas;
        }

        if (__sleep.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += sleep + " = ? ";
            args[idx++] = __sleep;
        }

        if (__wakeup.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += wakeup + " = ? ";
            args[idx++] = __wakeup;
        }

        if (__night.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += night + " = ? ";
            args[idx++] = __night;
        }

        if (__workout.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += workout + " = ? ";
            args[idx++] = __workout;
        }

        if (__hypogly.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += hypogly + " = ? ";
            args[idx++] = __hypogly;
        }

        if (__hypergly.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += hypergly + " = ? ";
            args[idx++] = __hypergly;
        }

        if (__work.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += work + " = ? ";
            args[idx++] = __work;
        }

        if (__athome.equals("1"))
        {
            if (idx == 2)
                query += " AND (";
            if (idx >= 3)
                query += "OR ";
            query += athome + " = ? ";
            args[idx++] = __athome;
        }

        if (idx > 2)
            query += ")";

        String[] argsok = new String[idx];
        int icopy = 0;
        while (icopy < idx)
            argsok[icopy] = args[icopy++];

        Cursor c = mDb.rawQuery(query, argsok);

        String[] i = c.getColumnNames();


        if (c == null || c.getCount() <= 0) {
            Log.e("Selectdaydateicone", "fail");
            return null;
        }
        while (c.moveToNext()) {

            String _title = c.getString(c.getColumnIndex(Titre));
            String _place = c.getString(c.getColumnIndex(Lieux));
            Double _glucide = c.getDouble(c.getColumnIndex(glucide));
            String _activity = c.getString(c.getColumnIndex(activity));
            String _hours = c.getString(c.getColumnIndex(Hour));
            String _activityType = c.getString(c.getColumnIndex(activityType));
            String _notes = c.getString(c.getColumnIndex(notes));
            String _date = c.getString(c.getColumnIndex(Date_hour));
            Double _fast_insu = c.getDouble(c.getColumnIndex(fast_insu));
            Double _slow_insu = c.getDouble(c.getColumnIndex(slow_insu));
            Double _hba1c = c.getDouble(c.getColumnIndex(hba1c));
            Double _glycemy = c.getDouble(c.getColumnIndex(glycemy));
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

            m = new EntryOfCDS(_date);
            m.setActivity(_activity);
            m.setActivityType(_activityType);
            m.setHour(_hours);
            m.setFast_insu(_fast_insu);
            m.setGlucide(_glucide);
            m.setNotes(_notes);
            m.setSlow_insu(_slow_insu);
            m.setTitle(_title);
            m.setPlace(_place);
            m.setHba1c(_hba1c);
            m.setglycemy(_glycemy);

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

            mAll.add(m);
        }
        return mAll;
    }

    public static String getLastEdition(String userId, SQLiteDatabase mDb)
    {
        String[] arr = {dateEdition};
        Cursor c = mDb.rawQuery("SELECT " + dateEdition + " FROM " + TABLE_NAME + " WHERE " + idUser + " = ? ORDER BY " + dateEdition + " DESC limit 1", new String[] {userId});
        if (c.moveToFirst() != false)
        {
            Log.i("EntryOfCDSDAO", "Je renvois la derniere date d'edition");
            return (c.getString(c.getColumnIndex(dateEdition)));
        }
        else{
            System.out.println("je vais retourner vide c'est la merde");
            return ("");
        }
    }
}
