package fr.diabhelp.carnetdesuivi.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by naqued on 21/11/15.
 */
public class DAO extends DAOBase {

    public static final String tdate = "date";

    public static final String glycemy = "glycemy";
    public static final String Titre = "title";
    public static final String Lieux = "place";
    public static final String Date_hour = "date_hour";
    public static final String glucide = "glucide";
    public static final String activity = "activity";
    public static final String activityType = "activity_type";
    public static final String notes = "notes";
    public static final String fast_insu = "fast_insu";
    public static final String slow_insu = "slow_insu";
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

    public static final String TABLE_NAME = "Diabhelp_CDS";
    public static final String TABLE_NAME_STAT = "Diabhelp_CDS_Statistiques";

    public static final String DATE_BEG = "beg_date";
    public static final String DATE_END = "end_date";


    public DAO(Context context)
    {
        this.mHandler = new Bdd_manager(context, NOM, null, VERSION);
    }

    public void AddDay(EntryOfCDS m) {
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
        mDb.insert(DAO.TABLE_NAME, null, value);
    }

    public void deleteDay(String _date, String _hour) {
        mDb.delete(TABLE_NAME, tdate + " = ? and " + Hour + " = ?", new String[] {_date, _hour});
    }

    public void Update(EntryOfCDS m) {
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

        mDb.update(TABLE_NAME, value, Date_hour + " = ?" + " and " + Hour + " = ?", new String[]{String.valueOf(m.getDate()), m.getHour() });

    }

    public ArrayList<EntryOfCDS> selectBetweenDays(String mtDate, String endMtdate) {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + tdate + " BETWEEN ? AND ?" , new String[] { mtDate, endMtdate} );
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

    public ArrayList<EntryOfCDS> SelectAll()
    {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();
        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME , null);

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

    public ArrayList<EntryOfCDS> SelectAllOneday(String mtDate)
    {
        EntryOfCDS m = null;
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + Date_hour + " = ?"  , new String[] { mtDate});

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

    public EntryOfCDS SelectDay(String mtDate, String _hour) {
        EntryOfCDS m = null;
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        if (_hour == null)
            _hour = "00h00";
        if (mtDate == null)
            mtDate = "0-0-0";
        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + Date_hour + " = ?" + " and " + Hour + " = ?" , new String[] { mtDate, _hour});

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

    public ArrayList<EntryOfCDS> SelectDayDateAndIcone(String startDate, String endDate,
                                String __breakfast, String __launch, String __diner, String __encas, String __sleep, String __wakeup, String __night, String __workout, String __hypogly, String __hypergly, String __work, String __athome, String __alcohol, String __period) {
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


    //-------Statistiques method------

    public void AddStat(EntryOfStats st) {

        ContentValues value = new ContentValues();

        value.put(DATE_BEG, st.getBeg_date());
        value.put(DATE_END, st.getEnd_date());

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

        mDb.insert(DAO.TABLE_NAME_STAT, null, value);
    }

    public EntryOfStats SelectStat() {
        EntryOfStats m = null;


        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME_STAT, null);

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
            String _beg_date = c.getString(c.getColumnIndex(DATE_BEG));
            String _end_date = c.getString(c.getColumnIndex(DATE_END));

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
