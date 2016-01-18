package fr.diabhelp.suiviprochepatient.utils.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

/**
 * Created by naqued on 28/09/15.
 */
public class DAO extends DAOBase {
    public static final String TABLE_NAME = "Diabhelp";
    public static final String ID = "id";
    public static final String USER = "user";
    public static final String PWD = "pwd";


    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER + " TEXT, " + PWD + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    /**
     * @param m le métier à ajouter à la base
     */

    public DAO(Context context)
    {
        this.mHandler = new Bdd_manager(context, NOM, null, VERSION);
    }

    public void AddUser(User m) {
        ContentValues value = new ContentValues();
        value.put(DAO.ID, m.getId());
        value.put(DAO.USER, m.getUser());
        value.put(DAO.PWD, m.getPwd());
        mDb.insert(DAO.TABLE_NAME, null, value);
    }

    /**
     * @param id l'identifiant du métier à supprimer
     */
    public void deleteUser(long id) {
        mDb.delete(TABLE_NAME, ID + " = ?", new String[] {String.valueOf(id)});
    }

    /**
     * @param m le métier modifié
     */
    public void UpdateUser(User m) {
        ContentValues value = new ContentValues();
        value.put(PWD, m.getPwd());
        value.put(USER, m.getUser());
        value.put(ID, 0);
        mDb.update(TABLE_NAME, value, ID  + " = ?", new String[] {"0"});
    }


    public User SelectUser() {
        User m = null;
        Cursor c = mDb.rawQuery("SELECT " + USER + ", " + PWD + " from " + TABLE_NAME , null);

        //String[] i = c.getColumnNames();
        if (c.moveToNext()) {
            String user = c.getString(c.getColumnIndex("user"));
            String pwd = c.getString(c.getColumnIndex("pwd"));

            m = new User(0, user, pwd);
        }
        return m;
    }

    public boolean isUserAlreadyFilled(String id)
    {
        Cursor c = mDb.rawQuery("SELECT " + USER + " FROM " + TABLE_NAME + " WHERE " + ID + " = ?", new String[] {id});
        if (c.moveToNext()) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

}