package fr.diabhelp.diabhelp.BDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by naqued on 28/09/15.
 */
public class DAO_Module extends DAOBase {
    public static final String TABLE_NAME = "Diabhelpmodule";
    public static final String ID = "id";

    public static final String MODULE_NAME = "module_name";
    public static final String LAST_UPDATE = "last_update";
    public static final String VERSION_MOD = "version";
    public static final String PACKAGE_NAME = "package_name";

    public DAO_Module(Context context)
    {
        this.mHandler = new Bdd_manager(context, NOM, null, VERSION);
    }

    // TODO
    public Module_DB AddModule(Module_DB m) {
        Calendar rightnow = Calendar.getInstance();

        int day = rightnow.get(Calendar.DAY_OF_MONTH);
        int month = rightnow.get(Calendar.MONTH);
        int year = rightnow.get(Calendar.YEAR);
        String date = new String();
        if (day < 10)
            date = "0".concat(String.valueOf(day));
        else
            date = String.valueOf(day);
        if (month < 10)
            date = date.concat("-").concat("0").concat(String.valueOf(month + 1));
        else
            date = date.concat("-").concat(String.valueOf(month + 1));
        date = date.concat("-").concat(String.valueOf(year));

        Log.e("thedate is :", date);
//        String date =

        ContentValues value = new ContentValues();
        value.put(DAO_Module.MODULE_NAME, m.getAppname());
        value.put(DAO_Module.LAST_UPDATE, date);
        value.put(DAO_Module.PACKAGE_NAME, m.getPname());
        value.put(DAO_Module.VERSION_MOD, m.getVersion());


        mDb.insert(DAO_Module.TABLE_NAME, null, value);
        m.setLast_update(date);
        return m;
    }

    /**
     * @param _pname l'identifiant du métier à supprimer
     */
    public void deleteModule(String _pname) {
        mDb.delete(TABLE_NAME, PACKAGE_NAME + " = ?", new String[] { _pname});
    }

    /**
     * @param m le métier modifié
     */
    public Module_DB Update(Module_DB m) {
        ContentValues value = new ContentValues();
        Calendar rightnow = Calendar.getInstance();

        int day = rightnow.get(Calendar.DAY_OF_MONTH);
        int month = rightnow.get(Calendar.MONTH);
        int year = rightnow.get(Calendar.YEAR);
        String date = new String();
        if (day < 10)
            date = "0".concat(String.valueOf(day));
        else
            date = String.valueOf(day);
        if (month < 10)
            date = date.concat("-").concat("0").concat(String.valueOf(month + 1));
        else
            date = date.concat("-").concat(String.valueOf(month + 1));

        date = date.concat("-").concat(String.valueOf(year));

        Log.e("thedateupdate is :", date);
        value.put(DAO_Module.MODULE_NAME, m.getAppname());
        value.put(DAO_Module.LAST_UPDATE, date);
        value.put(DAO_Module.PACKAGE_NAME, m.getPname());
        value.put(DAO_Module.VERSION_MOD, m.getVersion());

        mDb.update(TABLE_NAME, value, PACKAGE_NAME  + " = ?", new String[] {String.valueOf(m.getPname())});
        m.setLast_update(date);
        return m;
    }

    //TODO créer la table dans bdd_manager et finir le select surcouché p-e le select ac un pname
    public ArrayList<Module_DB> SelectMod() {
        ArrayList<Module_DB> mlist = new ArrayList<Module_DB>();

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME , null);

        //String[] i = c.getColumnNames();
        while (c.moveToNext()) {

            Module_DB m = new Module_DB();

            m.setAppname(c.getString(c.getColumnIndex(MODULE_NAME)));
            m.setLast_update(c.getString(c.getColumnIndex(LAST_UPDATE)));
            m.setPname(c.getString(c.getColumnIndex(PACKAGE_NAME)));
            m.setVersion(c.getString(c.getColumnIndex(VERSION_MOD)));
            Log.e("date select", m.getLast_update());
            mlist.add(m);
        }
        return mlist;
    }

    public  Module_DB SelectthisMod(String _pname) {

        Cursor c = mDb.rawQuery("SELECT * from " + TABLE_NAME + " where " + PACKAGE_NAME + " = ?", new String[] { _pname} );
        Module_DB m = new Module_DB(_pname);
        if (c.moveToNext()) {

            m.setAppname(c.getString(c.getColumnIndex(MODULE_NAME)));
            m.setLast_update(c.getString(c.getColumnIndex(LAST_UPDATE)));
            m.setVersion(c.getString(c.getColumnIndex(VERSION_MOD)));
            Log.e("date select", m.getLast_update());

        }
        else
            return null;
        return m;
    }
}
