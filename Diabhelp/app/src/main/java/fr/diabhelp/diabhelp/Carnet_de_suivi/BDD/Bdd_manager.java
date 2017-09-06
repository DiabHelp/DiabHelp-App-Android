package fr.diabhelp.diabhelp.Carnet_de_suivi.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by naqued on 21/11/15.
 */
public class Bdd_manager extends SQLiteOpenHelper {

    public Bdd_manager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EntryOfCDSDAO.TABLE_CREATE);
        db.execSQL(EntryOfStatsDAO.TABLE_CREATE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(EntryOfCDSDAO.TABLE_DROP);
        db.execSQL(EntryOfStatsDAO.TABLE_DROP);
        onCreate(db);
    }
}

