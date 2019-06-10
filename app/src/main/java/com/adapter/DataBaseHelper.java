package com.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

@SuppressLint("SdCardPath")
public class DataBaseHelper extends SQLiteOpenHelper {
    // window
    private static String DB_PATH = "";
    private static String DB_NAME = "gkqueans.sqlite";// Database name
    private SQLiteDatabase mDataBase;
    private final Context mContext;
    private static final int DATABASE_VERSION = 5;
    private static final String SP_KEY_DB_VER = "db_ver";

    public DataBaseHelper(Context context) throws IOException {
        super(context, DB_NAME, null, 1);// 1? its Database Version
        if (android.os.Build.VERSION.SDK_INT >= 4.2) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        this.mContext = context;
        initialize();
    }

    /**
     * Initializes database. Creates database if doesn't exist.
     */
    private void initialize() {
        if (checkDataBase()) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
            int dbVersion = prefs.getInt(SP_KEY_DB_VER, 1);
            if (DATABASE_VERSION > dbVersion) {
                File dbFile = new File(DB_PATH + DB_NAME);
                if (!dbFile.delete()) {
                    Log.w(TAG, "Unable to update database");
                }
            }
        }
        if (!checkDataBase()) {
            try {
                createDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void createDataBase() throws IOException {
        // If database not exists copy it from the assets
        boolean dbexist = checkDataBase();
        if (dbexist) {
            // System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
                throw new Error("Error copying database");
            }
        }
    }

    // Check that the database exists here: /data/data/your package/databases/Da
    // Name
    private boolean checkDataBase() {
        File dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    // Copy the database from assets
    private void copyDataBase() throws IOException {


        InputStream myinput;
        try {
            myinput = mContext.getAssets().open(DB_NAME);

            Log.i("DB", "InputStream created");

            // Path to the just created empty db
            String outfilename = DB_PATH + DB_NAME;
            Log.i("DB", "outfilename : " + outfilename);

            // Open the empty db as the output stream
            OutputStream myoutput = new FileOutputStream("/data/data/" + mContext.getApplicationContext().getPackageName() + "/databases/" + DB_NAME);
            Log.i("DB", "myoutput : " + "/data/data/" + mContext.getApplicationContext().getPackageName() + "/databases/" + DB_NAME);

            // transfer byte to inputfile to outputfile
            byte[] buffer = new byte[1024];
            int length = 0;
            Log.i("DB", "Transfer Start");
            int i = 0;
            while ((length = myinput.read(buffer)) > 0) {
                Log.i("DB", "Transfer Read : " + length);
                myoutput.write(buffer, 0, length);
                Log.i("DB", "Transfer Progress : " + i);
                i++;
            }

            // Close the streams
            myoutput.flush();
            myoutput.close();
            myinput.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            Log.e("DB", "Error : " + e.getMessage());
            e.printStackTrace();
        }
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        prefs.edit().putInt(SP_KEY_DB_VER, DATABASE_VERSION).commit();
    }

    // Open the database, so we can query it
    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DB_NAME;
        // Log.v("mPath", mPath);
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.OPEN_READWRITE);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub

    }
}
