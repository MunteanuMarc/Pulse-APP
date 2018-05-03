package com.poli.actipuls.localdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.poli.actipuls.MockPulseGenerator;
import com.poli.actipuls.localdata.LocalDbPulseContract.LocalDbPulseEntry;

public class LocalDbHelper extends SQLiteOpenHelper {

    // the database name
    private static final String DATABASE_NAME = "actipuls.db";
    // Tag for Logging
    private final static String TAG = LocalDbHelper.class.getSimpleName();
    // Get the database
    private final SQLiteDatabase db = getWritableDatabase();
    // database version
    private static final int DATABASE_VERSION = 1;

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * On create method
     * @param sqLiteDatabase
     */
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

    /**
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocalDbPulseEntry.TABLE_NAME);
        onCreate(db);
    }

    /**
     * Method to insert data in local DB
     */
    public void insertData() {
        if (db == null) {
            return;
        }
        ContentValues cv = new ContentValues();
        // for testing purposes we are using a generated pulse
        cv.put(LocalDbPulseContract.LocalDbPulseEntry.COLUMN_PULSE, MockPulseGenerator.generatePulse());
        cv.put(LocalDbPulseContract.LocalDbPulseEntry.COLUMN_PROCESSED, 0);
        //insert in one transaction
        try {
            db.beginTransaction();
            db.insert(LocalDbPulseContract.LocalDbPulseEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Exception SQL" + e);
        } finally {
            db.endTransaction();
        }

    }

    /**
     * Calculates a sum of all the beats received in the last 30 seconds
     */
    public void calculatePulse() {
        // query string to calculate the sum of all heartbeats in the last 30 seconds
        String sql = "SELECT SUM(" + LocalDbPulseEntry.COLUMN_PULSE + ") FROM " + LocalDbPulseEntry.TABLE_NAME + " WHERE " + LocalDbPulseEntry.COLUMN_PROCESSED + " = 0";
        // query string to set table entried as processed in order for cleaning
        Log.i(TAG, sql);
        // execute the raw queries
        Cursor cursor = db.rawQuery(sql, null);
        // setting the proccesed data entries to 1
        // return the data
        if (cursor.moveToFirst()) {
            int sum = cursor.getInt(0);
            Log.i(TAG, "!------------ HeartsBeats per 30 sec : " + sum);
        }
        cursor.close();
        setProcessed();
    }

    /**
     * Method to set the processed rows to 1
     */
    private void setProcessed() {
        // New value
        int value = 1;
        ContentValues values = new ContentValues();
        values.put(LocalDbPulseEntry.COLUMN_PROCESSED, value);
        db.update(
                LocalDbPulseEntry.TABLE_NAME,
                values,
                null,
                null);
    }

    /**
     *Method to delete the processed rows
     */
    public void deleteProcessedData() {
        // Define 'where' part of query.
        String selection = LocalDbPulseEntry.COLUMN_PROCESSED + " LIKE ?";
        // Arguments in placeholder order.
        String[] selectionArgs = {"1"};
        db.delete(LocalDbPulseEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Unused method
     * Maybe useful later otherwise will be deleted in final version
     *
     * @return All the Data in the table
     */
    public Cursor getAllDataInTable() {
        return db.query(
                LocalDbPulseContract.LocalDbPulseEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Method to close the db
     */
    public void closeLocalDb(){
        db.close();
    }
}
