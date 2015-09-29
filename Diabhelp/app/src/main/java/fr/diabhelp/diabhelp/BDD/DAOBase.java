package fr.diabhelp.diabhelp.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by naqued on 28/09/15.
 */
public abstract class DAOBase {
    // Nous sommes à la première version de la base
    // Si je décide de la mettre à jour, il faudra changer cet attribut

    protected final static int VERSION = 18;

    // Le nom du fichier qui représente ma base
    protected final static String NOM = "dh_db.db";

    protected SQLiteDatabase mDb = null;
    protected Bdd_manager mHandler = null;

    public DAOBase(Context pContext) {
        this.mHandler = new Bdd_manager(pContext, NOM, null, VERSION);
    }
    public DAOBase() // pas sur de ça
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