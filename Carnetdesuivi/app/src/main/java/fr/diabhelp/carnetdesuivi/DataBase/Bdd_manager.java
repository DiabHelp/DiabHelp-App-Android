package fr.diabhelp.carnetdesuivi.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by naqued on 21/11/15.
 */
public class Bdd_manager extends SQLiteOpenHelper {
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

    public static final String TABLE_NAME = "Diabhelp_CDS";
    public static final String TABLE_NAME_STAT = "Diabhelp_CDS_Statistiques";

    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + tdate + " TEXT, " + Titre + " TEXT, " + Lieux + " TEXT, " + Date_hour + " TEXT, " + glucide + " DOUBLE, " + activity + " TEXT, " + activityType + " TEXT, " + notes + " TEXT, " + fast_insu + " DOUBLE, " + slow_insu + " DOUBLE, " + hba1c + " DOUBLE, " + Hour + " TEXT, " + glycemy + " DOUBLE, " + breakfast + " INTEGER, " + launch + " INTEGER, " + diner + " INTEGER, " + encas + " INTEGER, " + sleep + " INTEGER, " + wakeup + " INTEGER, " + night + " INTEGER, " + workout + " INTEGER, " + hypogly + " INTEGER, " + hypergly + " INTEGER, " + work + " INTEGER, " + athome + " INTEGER, " + alcohol + " INTEGER, " + period + " INTEGER, " + "rdate datetime default (datetime(current_timestamp)));";

    public static final String DATE_DEBUT = "beg_date";
    public static final String DATE_FIN = "end_date";

    // Table Statistiques
    public static final String TABLE_CREATE_STAT = "CREATE TABLE " + TABLE_NAME_STAT + " (" + breakfast + " INTEGER, " + launch + " INTEGER, " + diner + " INTEGER, " + encas + " INTEGER, " + sleep + " INTEGER, " + wakeup + " INTEGER, " + night + " INTEGER, " + workout + " INTEGER, " + hypogly + " INTEGER, " + hypergly + " INTEGER, " + work + " INTEGER, " + athome + " INTEGER, " + alcohol + " INTEGER, " + period + " INTEGER, " + DATE_DEBUT + " TEXT, " + DATE_FIN + " TEXT, " + "rdate datetime default (datetime(current_timestamp)));";


    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    public static final String TABLE_DROP_STAT = "DROP TABLE IF EXISTS " + TABLE_NAME_STAT + ";";

    public Bdd_manager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        db.execSQL(TABLE_CREATE_STAT);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        db.execSQL(TABLE_DROP_STAT);
        onCreate(db);
    }
}

