package fr.diabhelp.diabhelp.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by naqued on 28/09/15.
 */
public class Bdd_manager extends SQLiteOpenHelper {

    public static final String ID = "id";
    public static final String USER = "user";
    public static final String PWD = "pwd";
    public static final String TABLE_NAME = "Diabhelp";

    public static final String TABLE_MODNAME = "Diabhelpmodule";

    public static final String MODULE_NAME = "module_name";
    public static final String LAST_UPDATE = "last_update";
    public static final String VERSION_MOD = "version";
    public static final String PACKAGE_NAME = "package_name";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    USER + " TEXT, " +
                    PWD + " TEXT);";

    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";
    public static final String TABLE_DROP_MOD = "DROP TABLE IF EXISTS " + TABLE_MODNAME + ";";

    public static final String TABLE_MOD_CREATE = "CREATE TABLE " + TABLE_MODNAME + " (" + MODULE_NAME + " TEXT,"
            + LAST_UPDATE + " datetime default (datetime(current_timestamp)),"
            + VERSION_MOD + " DOUBLE,"
            + PACKAGE_NAME + " TEXT);";

    public Bdd_manager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("db on_create", TABLE_MOD_CREATE);
        db.execSQL(TABLE_MOD_CREATE);
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        db.execSQL(TABLE_DROP_MOD);
        onCreate(db);
    }
}
// TODO Date > SQLITE et SQLITE > DATE