package fr.diabhelp.diabhelp.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sumbers on 22/06/16.
 */
public class DAO{

    private static DAO instance = null;

    public static DAO getInstance(Context context)
    {
        if (instance == null)
            instance = new DAO(context);
        return (instance);
    }

    //version de la BDD
    private final static int VERSION_HEAD = 20;

    // Le nom du fichier qui représente ma base
    private final static String NOM = "dh_db.db";

    private SQLiteDatabase mDb = null;
    private BddManager mHandler = null;

    private DAO(Context context) {
        this.mHandler = new BddManager(context, NOM, null, VERSION_HEAD);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }
}