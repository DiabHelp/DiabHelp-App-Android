package fr.diabhelp.diabhelp.BDD;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import fr.diabhelp.diabhelp.BDD.Ressource.User;

/**
 * Created by sumbers on 22/06/16.
 */
public class UserDAO{

    public static final String TABLE_NAME = "User";
    public static final String USER_KEY = "id";
    public static final String USER_ID = "nom";
    public static final String USER_PWD = "password";


    public static final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" + USER_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_ID + " TEXT, " + USER_PWD + " TEXT);";
    public static final String TABLE_DROP =  "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public static void addUser(User m, SQLiteDatabase mDb) {
        ContentValues value = new ContentValues();
        value.put(UserDAO.USER_KEY, m.getId());
        value.put(UserDAO.USER_ID, m.getUser());
        value.put(UserDAO.USER_PWD, m.getPwd());
        mDb.insert(UserDAO.TABLE_NAME, null, value);
    }

    /**
     * @param id l'identifiant du métier à supprimer
     */
    public static void deleteUser(long id, SQLiteDatabase mDb) {
        mDb.delete(TABLE_NAME, USER_KEY + " = ?", new String[] {String.valueOf(id)});
    }

    public static void deleteAllUsers(SQLiteDatabase mDb)
    {
        mDb.delete(TABLE_NAME, null, null);
    }

    /**
     * @param m le métier modifié
     */
    public static void UpdateUser(User m, SQLiteDatabase mDb) {
        ContentValues value = new ContentValues();
        value.put(USER_PWD, m.getPwd());
        value.put(USER_ID, m.getUser());
        value.put(USER_KEY, 0);
        mDb.update(TABLE_NAME, value, USER_KEY  + " = ?", new String[] {"0"});
    }


    public static User selectUser(SQLiteDatabase mDb) {
        User m = null;
        Cursor c = mDb.rawQuery("SELECT " + USER_ID + ", " + USER_PWD + " from " + TABLE_NAME , null);

        //String[] i = c.getColumnNames();
        if (c.moveToNext()) {
            String user = c.getString(c.getColumnIndex("nom"));
            String pwd = c.getString(c.getColumnIndex("password"));

            m = new User(0, user, pwd);
        }
        return m;
    }

    public static Boolean isUserAlreadyFilled(String id, SQLiteDatabase mDb)
    {
        Cursor c = mDb.rawQuery("SELECT " + USER_ID + " FROM " + TABLE_NAME + " WHERE " + USER_KEY + " = ?", new String[] {id});
        if (c.moveToNext()) {
            c.close();
            return true;
        }
        c.close();
        return false;
    }

}