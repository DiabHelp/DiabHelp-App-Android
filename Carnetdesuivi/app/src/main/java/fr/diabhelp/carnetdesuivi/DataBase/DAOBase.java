package fr.diabhelp.carnetdesuivi.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by naqued on 21/11/15.
 */
public abstract class DAOBase {

    // Version++ pour mettre a jour
    // protected final static int VERSION = 19;
    protected final static int VERSION = 20;
    // Le nom du fichier qui représente ma base
    protected final static String NOM = "dh_db.db";

    protected SQLiteDatabase mDb = null;
    protected Bdd_manager mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new Bdd_manager(pContext, NOM, null, VERSION);
    }
    public DAOBase()
    {
        this.mHandler = new Bdd_manager(null, NOM, null, VERSION);
    }

    public SQLiteDatabase open() {
        // Pas besoin de fermer la dernière base puisque getWritableDatabase s'en charge
        mDb = mHandler.getWritableDatabase();
        return mDb;
    }

    public void close() {
        mDb.close();
    }

    public SQLiteDatabase getDb() {
        return mDb;
    }
}
