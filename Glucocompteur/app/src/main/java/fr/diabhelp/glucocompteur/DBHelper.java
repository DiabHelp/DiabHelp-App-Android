package fr.diabhelp.glucocompteur;

/**
 * Created by Kataleya on 15/03/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

public class DBHelper extends SQLiteOpenHelper
{
    private static String DB_PATH;
    private static String DB_NAME ="aliments.db";
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    public String tableName = "aliments_search";
    public String fieldObjectId = "_id";
    public String fieldObjectORIGGPCD = "ORIGGPCD";
    public String fieldObjectORIGGPFR = "ORIGGPFR";
    public String fieldObjectORIGFDCD = "ORIGFDCD";
    public String fieldObjectName = "alimentname";
    public String fieldObjectGlucides = "Glucides";
    public String fieldObjectSucres = "Sucres";
    public String fieldObjectCholes = "Cholestérol";

    public DBHelper(Context context)
    {
        super(context, DB_NAME, null, 1);// 1 is Database Version
        if(android.os.Build.VERSION.SDK_INT >= 17){
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        }
        else{
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
    }

    public void createDataBase() throws IOException
    {
        this.getWritableDatabase();
        this.close();
        try{
            copyDataBase();
            Log.e("createDatabase", "database created & copyied");
        }
        catch (IOException mIOException){
            throw new Error("ErrorCopyingDataBase");
        }
    }

    private boolean checkDataBase()
    {
        File dbFile = new File(DB_PATH + DB_NAME);
        Log.v("dbFile", dbFile + "   "+ dbFile.exists());
        return dbFile.exists();
    }

    private void copyDataBase() throws IOException
    {
        InputStream mInput = mContext.getAssets().open(DB_NAME);
        Log.d("CopyDB", mInput.toString());
        String outFileName = DB_PATH + DB_NAME;
        OutputStream mOutput = new FileOutputStream(outFileName);
        byte[] mBuffer = new byte[1024];
        int mLength;
        while ((mLength = mInput.read(mBuffer))>0){
            mOutput.write(mBuffer, 0, mLength);
        }
        mOutput.flush();
        mOutput.close();
        mInput.close();
    }

    public boolean openDataBase() throws SQLException
    {
        String mPath = DB_PATH + DB_NAME;
        Log.v("mPath", mPath);
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist)
            try {
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        mDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.CREATE_IF_NECESSARY);
        if (mDataBase != null)
            Log.i("DB info", "DB is not null" + mPath);
        else
            try {
                Log.i("openDatabase", "DB is null, we now create it" + mPath);
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return mDataBase != null;
    }

    @Override
    public synchronized void close()
    {
        if(mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "";
        sql += "CREATE TABLE if not exists " + tableName;
        sql += " (";
        sql += fieldObjectId + " INTEGER PRIMARY KEY AUTOINCREMENT, ";
        sql += fieldObjectORIGGPCD + " TEXT, ";
        sql += fieldObjectORIGGPFR + " TEXT, ";
        sql += fieldObjectORIGFDCD + " TEXT, ";
        sql += fieldObjectName + " TEXT, ";
        sql += fieldObjectGlucides + " TEXT, ";
        sql += fieldObjectSucres + " VARCHAR, ";
        sql += fieldObjectCholes + " VARCHAR";
        sql += ") ";
        Log.d("DB creation", "Using CREATE TABLE");
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        db.execSQL(sql);
        onCreate(db);
    }

    public Vector<MyObject> search(String text)
    {
        String sql = "";
        text = text.replace("'", "''").replace("\"", "\"\"");
        sql += "SELECT alimentname, Glucides FROM " + tableName;
        sql += " WHERE " + fieldObjectName + " MATCH '*" + text + "*'";
        sql += " ORDER BY " + fieldObjectName + "='" + text + "' DESC,";
        sql += fieldObjectName + " LIKE '" + text + "%' DESC,";
        sql += fieldObjectName + " LIKE '%" + text + "%' DESC";
        sql += " LIMIT 8";
        Log.d("SQL", sql);
        Vector<MyObject> recordsList = new Vector<MyObject>();
        try {
            Cursor cursor = this.mDataBase.rawQuery(sql, null);if (cursor == null || cursor.getCount() <= 0)
                Log.d("cursor ", "cursor is null or negative"); //<====== MESSAGE D'ERREUR POUR L'USER POSSIBLE (pas d'aliment correspondant)
            else
            if (cursor.moveToFirst()){
                do{
                    //// int productId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(fieldProductId)));
                    Log.d("cursor ", cursor.getString(cursor.getColumnIndex(fieldObjectName))); // A commenter pour virer le Log
                    String objectName = cursor.getString(cursor.getColumnIndex(fieldObjectName));
                    String objectGlucides;
                    if (cursor.getString(cursor.getColumnIndex(fieldObjectGlucides)).equals("traces")) {
                        objectGlucides = "0.001";
                    } else {
                        objectGlucides = cursor.getString(cursor.getColumnIndex(fieldObjectGlucides));
                    }
                    MyObject myObject = new MyObject(objectName, objectGlucides);
                    recordsList.add(myObject);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        catch(Exception ex){
            System.out.println("DatabaseHelper.search()- : ex " + ex.getClass() +", "+ ex.getMessage());
        }
        for (int it = 0 ; it < recordsList.size(); it++) Log.e("recordsize : ", recordsList.get(it).objectName);
        return recordsList;
    }
}