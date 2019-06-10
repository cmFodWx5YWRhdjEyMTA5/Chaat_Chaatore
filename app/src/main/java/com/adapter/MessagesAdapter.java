package com.adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.IOException;

public class MessagesAdapter {
    @SuppressWarnings("unused")
    private Context context;
    private DataBaseHelper mDbHelper;
    private SQLiteDatabase sqLiteDatabase;

    public static final String sno = "sno";
    public static final String cat = "cat";
    public static final String Ans = "Ans";
    public static final String Que = "Que";
    public static final String Level = "Level";

    public static final String Opt_1 = "Opt_1";
    public static final String Opt_2 = "Opt_2";
    public static final String Opt_3 = "Opt_3";
    public static final String Ans_q = "Ans_q";

    public class SQLiteHelper extends SQLiteOpenHelper {
        public SQLiteHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("PRAGMA foreign_keys=ON;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public MessagesAdapter(Context context) {
        this.context = context;
        try {
            mDbHelper = new DataBaseHelper(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MessagesAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            Log.e("Error :", mIOException.toString()
                    + "  UnableToCreateDatabase");
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public MessagesAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            sqLiteDatabase = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            Log.e("Error :", "open >>" + mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }


    public Cursor getContent1(String data) {
        Cursor cursor = sqLiteDatabase.rawQuery("select * from GK WHERE Level = ?", new String[]{data});
        return cursor;
    }

    public Cursor getContent(String data) {
        Cursor cursor = sqLiteDatabase.rawQuery(
                "select * from GK_1  WHERE Level = ?", new String[]{data});
        return cursor;
    }


    ////////////////////////////////ORDER BY RANDOM() = ?
}
