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

    public static final String glycemy = "glycemy"; // y
    public static final String Titre = "title"; // y
    public static final String Lieux = "place"; //y
    public static final String Date_hour = "date_hour"; // y
    public static final String glucide = "glucide"; // y
    public static final String activity = "activity"; //
    public static final String activityType = "activity_type"; //
    public static final String notes = "notes"; //
    public static final String fast_insu = "fast_insu"; // y
    public static final String slow_insu = "slow_insu"; // y
    public static final String hba1c = "hba1c"; // y
    public static final String Hour = "hour"; // y

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
        Log.e("add Day date", m.getDate());
        value.put(Titre, m.getTitle());
        Log.e("place saved", m.getPlace());
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

/*
        String tmpdate[] = new String[3];

        String final_date = new String();

        tmpdate = m.getDate().split("-");

        String month = new String();
*/
/*
        if (getMonthint(tmpdate[1]) < 10)
            month  = "0".concat(String.valueOf(getMonthint(tmpdate[1])));
        else
            month = String.valueOf(tmpdate[1]);

        final_date = tmpdate[2].concat("-").concat(month).concat("-").concat(tmpdate[0]);
*/

        // attention ici : 2015-MARS-13

/*        value.put("rdate", final_date);
        Log.e("final date", final_date);*/
        Log.e("bdd-action :", "insert");
        Log.e("bdd-value", String.valueOf(m.getBreakfast()));

        mDb.insert(DAO.TABLE_NAME, null, value);
    }

    /**
     * @param // l'identifiant du métier à supprimer
     */

    private int    getMonthint(String month) {
        if (month.contains("Janvier"))
            return (1);
        else if (month.contains("Fevrier"))
            return (2);
        else if (month.contains("Mars"))
            return (3);
        else if (month.contains("Avril"))
            return (4);
        else if (month.contains("Mai"))
            return (5);
        else if (month.contains("Juin"))
            return (6);
        else if (month.contains("Juillet"))
            return (7);
        else if (month.contains("Aout"))
            return (8);
        else if (month.contains("Septembre"))
            return (9);
        else if (month.contains("Octobre"))
            return (10);
        else if (month.contains("Novembre"))
            return (11);
        else if (month.contains("Decembre"))
            return (12);
        return (-1);
    }

    public void deleteDay(String _date, String _hour) {
        mDb.delete(TABLE_NAME, tdate + " = ? and " + Hour + " = ?", new String[] {_date, _hour});
    }

    /**
     * @param m le métier modifié
     */

    public void Update(EntryOfCDS m) {
        Log.e("bdd-action :", "update");
        ContentValues value = new ContentValues();



        value.put(Titre, m.getTitle());
        value.put(Lieux, m.getPlace());
        Log.e("date_hour in update", m.getDate());
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

        Log.e("date request Update", m.getDate());
        Log.e("hour request Update", m.getHour());
        mDb.update(TABLE_NAME, value, Date_hour + " = ?" + " and " + Hour + " = ?", new String[]{String.valueOf(m.getDate()), m.getHour() });

    }

    public ArrayList<EntryOfCDS> selectBetweenDays(String mtDate, String endMtdate) {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

//        String final_begdate = new String();
//        String final_enddate = new String();
//        String tmpdate[] = new String[3];
//
//        tmpdate = mtDate.split("-");
//        final_begdate = "20" + tmpdate[2].concat("-").concat(tmpdate[0]).concat("-").concat(tmpdate[1]);
//
//        tmpdate = endMtdate.split("-");
//        final_enddate = "20" + tmpdate[2].concat("-").concat(tmpdate[0]).concat("-").concat(tmpdate[1]);
//
//
//        Log.e("begin date", final_begdate);
//        Log.e("end date", final_enddate);

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where rdate BETWEEN ? AND  ?" , new String[] { mtDate, endMtdate} );

        while (c.moveToNext()) {

            String rdate = c.getString(c.getColumnIndex("rdate"));

            Log.e("date sqllite :", rdate.toString());

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
/*            m.setDate(_date); annciennement setDateAPI */
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

=======
>>>>>>> modules/Carnet_de_suivi-db_stat

            mAll.add(m);
            // Faire quelque chose

        }
        c.close();
        Log.e("status ok with size of :", String.valueOf(mAll.size()));
        return mAll;
    }

    /**
     * @param //id l'identifiant du métier à récupérer
     */
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
            Log.e("SQL DATE", _datesql);

            EntryOfCDS m = new EntryOfCDS(_date);
            m.setDate(_date);
            m.setDatesql(_datesql);
            m.setActivity(_activity);
            m.setActivityType(_activityType);
/*            m.setDate(_date); annciennement setDateAPI */
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

/*
        String _title = c.getString(c.getColumnIndex(Titre));
        String _place = c.getString(c.getColumnIndex(Lieux));
        Double _glucide = c.getDouble(c.getColumnIndex(glucide));
        String _activity = c.getString(c.getColumnIndex(activity));

        String _activityType = c.getString(c.getColumnIndex(activityType));

        String _notes = c.getString(c.getColumnIndex(notes));


        Double _fast_insu = c.getDouble(c.getColumnIndex(fast_insu));
        Double _slow_insu = c.getDouble(c.getColumnIndex(slow_insu));
        Double _hba1c = c.getDouble(c.getColumnIndex(hba1c));
*/
            String _date = c.getString(c.getColumnIndex(Date_hour));
            Double _glycemy = c.getDouble(c.getColumnIndex(glycemy));
<<<<<<< HEAD
/*        Integer _launch = c.getInt(c.getColumnIndex(lunch));
=======
/*        Integer _launch = c.getInt(c.getColumnIndex(launch));
>>>>>>> modules/Carnet_de_suivi-db_stat
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
        Integer _breakfast = c.getInt(c.getColumnIndex(breakfast));*/

            m = new EntryOfCDS(_date);
            m.setDateApi(mtDate);
/*        m.setActivity(_activity);
        m.setActivityType(_activityType);
        m.setDate(_date);
        m.setFast_insu(_fast_insu);
        m.setGlucide(_glucide);
        m.setNotes(_notes);
        m.setSlow_insu(_slow_insu);
        m.setTitle(_title);
        m.setPlace(_place);
        m.setHba1c(_hba1c);*/
            m.setglycemy(_glycemy);
            Log.e("glycemie in DAO", String.valueOf(_glycemy));
/*        m.setBreakfast(_breakfast);
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
        m.setPeriod(_period);*/

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

    public EntryOfCDS SelectDayDateAndIcone(String mtDate, String _hour,
                                String __breakfast, String __launch, String __diner, String __encas, String __sleep, String __wakeup, String __night , String __workout , String __hypogly , String __hypergly , String __work , String __athome , String __alcohol, String __period) {
        EntryOfCDS m = null;
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        if (_hour == null)
            _hour = "00h00";
        if (mtDate == null)
            mtDate = "0-0-0";
        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + Date_hour + " = ?" + " and " + Hour + " = ?" + " and " + breakfast + " = ? " + " and " + launch + " = ? " + " and " + diner + " = ? " + " and " + encas + " = ? " + " and " + sleep + " = ? " + " and " + wakeup + " = ? " + " and " + night + " = ? " + " and " + workout + " = ? " + " and " + hypogly + " = ? " + " and " + hypergly + " = ? " + " and " + work + " = ? " + " and " + athome + " = ? " + " and " + alcohol + " = ? " + " and " + period + " = ? ", new String[] { mtDate, _hour, __breakfast, __launch + __diner + __encas + __sleep + __wakeup + __night + __workout + __hypogly + __hypergly + __work + __athome + __alcohol + __period});

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

    public ArrayList<EntryOfCDS> SelectAllDay(String mtDate, String endMtdate) {
        ArrayList<EntryOfCDS> m = new ArrayList<EntryOfCDS>();

        String final_begdate = new String();
        String final_enddate = new String();
        String tmpdate[] = new String[3];

        tmpdate = mtDate.split("-");
        final_begdate = "20" + tmpdate[2].concat("-").concat(tmpdate[0]).concat("-").concat(tmpdate[1]);

        tmpdate = endMtdate.split("-");
        final_enddate = "20" + tmpdate[2].concat("-").concat(tmpdate[0]).concat("-").concat(tmpdate[1]);


        Log.e("begin date", final_begdate);
        Log.e("end date", final_enddate);

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where rdate BETWEEN ? AND  ?" , new String[] { final_begdate, final_enddate} );

        while (c.moveToNext()) {

            String rdate = c.getString(c.getColumnIndex("rdate"));

            Log.e("date sqllite :", rdate.toString());

            EntryOfCDS oneday = new EntryOfCDS(rdate.toString());
/*            oneday.setGlycMorning(c.getDouble(c.getColumnIndex(Morning_before)), c.getDouble(c.getColumnIndex(Morning_after)));
            oneday.setInsuMorning(c.getDouble(c.getColumnIndex(Morning_insu)));

            oneday.setGlycMidDay(c.getDouble(c.getColumnIndex(Midnight_after)), c.getDouble(c.getColumnIndex(Midnight_after)));
            oneday.setInsuMidDay(c.getDouble(c.getColumnIndex(Midnight_insu)));

            oneday.setNight(c.getDouble(c.getColumnIndex(Night)));

            oneday.setGlycEndDay(c.getDouble(c.getColumnIndex(EndDay_before)), c.getDouble(c.getColumnIndex(EndDay_after)));
            oneday.setInsuEndDay(c.getDouble(c.getColumnIndex(EndDay_insu)));

            oneday.SetSlowInsu(c.getDouble(c.getColumnIndex(lentus)));

            oneday.setCommentofDay(c.getString(c.getColumnIndex(CommentofDay)));
            oneday.setDate(c.getString(c.getColumnIndex(tdate)));
            oneday.setDateApi(oneday.getDate());*/ //TODO Changer ça avec les nouvelle valeurs

            Log.e("date of day", oneday.getDate());
            Log.e("date of cds", oneday.getDateApi());
            m.add(oneday);
            // Faire quelque chose

        }
        c.close();
        return m;
    }

    public ArrayList<EntryOfCDS> selectBetweenDays(String mtDate, String endMtdate) {
        ArrayList<EntryOfCDS> mAll = new ArrayList<EntryOfCDS>();

        String final_begdate = new String();
        String final_enddate = new String();
        String tmpdate[] = new String[3];

        tmpdate = mtDate.split("-");
        final_begdate = "20" + tmpdate[2].concat("-").concat(tmpdate[0]).concat("-").concat(tmpdate[1]);

        tmpdate = endMtdate.split("-");
        final_enddate = "20" + tmpdate[2].concat("-").concat(tmpdate[0]).concat("-").concat(tmpdate[1]);

        // 2016-02-25

        Log.e("begin date", final_begdate);
        Log.e("end date", final_enddate);

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where rdate BETWEEN ? AND  ?" , new String[] { final_begdate, final_enddate} );

        while (c.moveToNext()) {

            String rdate = c.getString(c.getColumnIndex("rdate"));

            Log.e("date sqllite :", rdate.toString());

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
/*            m.setDate(_date); annciennement setDateAPI */
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
            // Faire quelque chose

        }
        c.close();
        Log.e("status ok size:", String.valueOf(mAll.size()));
        return mAll;
    }

    //Statistiques method
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

    public EntryOfStats SelectStat(String beg_date, String end_date) {
        EntryOfStats m = null;

        if (beg_date == null)
            beg_date = "0-0-0";
        if (end_date == null)
            end_date = "0-0-0";
        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME_STAT + " where " + DATE_BEG + " = ?" + " and " + DATE_END + " = ?" , new String[] { beg_date, DATE_END});

        String[] i = c.getColumnNames();

        c.moveToNext();

        if (c == null || c.getCount() <= 0) {
            Log.e("Select statistiques", "Select echoué : beg_date" + beg_date);
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

        return m;
    }
}
