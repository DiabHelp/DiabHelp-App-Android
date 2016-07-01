package fr.diabhelp.diabhelp.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by naqued on 28/09/15.
 */
public class BddManager extends SQLiteOpenHelper {

    public BddManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("BddManage", "onCreate tables");
        db.execSQL(UserDAO.TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(UserDAO.TABLE_DROP);
        this.onCreate(db);
    }
}
// TODO Date > SQLITE et SQLITE > DATE