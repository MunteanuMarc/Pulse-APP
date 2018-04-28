package com.poli.actipuls.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.poli.actipuls.data.LocalDbPulseContract.LocalDbPulseEntry;

public class LocalDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "actipuls.db";

    private static final int DATABASE_VERSION = 1;

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        // Create a table to hold  data
        final String SQL_CREATE_TABLE = "CREATE TABLE " + LocalDbPulseEntry.TABLE_NAME + " (" +
                LocalDbPulseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LocalDbPulseEntry.COLUMN_PULSE + " INTEGER NOT NULL, " +
                LocalDbPulseEntry.COLUMN_PROCESSED + " INTEGER NOT NULL " +
                "); ";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocalDbPulseEntry.TABLE_NAME);
        onCreate(db);
    }
}
