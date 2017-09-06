package fr.diabhelp.diabhelp.Carnet_de_suivi.BDD;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by naqued on 21/11/15.
 */
public class DAO {

    // Version++ pour mettre a jour
    private final static int VERSION_HEAD = 25;
    // Le nom du fichier qui représente ma base
    private final static String NOM = "dh_db.db";
    private static DAO instance = null;
    private SQLiteDatabase mDb = null;
    private Bdd_manager mHandler = null;
    private DAO(Context pContext) {
        this.mHandler = new Bdd_manager(pContext, NOM, null, VERSION_HEAD);
    }

    public static DAO getInstance(Context context)
    {
        if (instance == null) {
                Log.i("DAO", "dao initialisation");
                instance = new DAO(context);

        }
        return (instance);
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
