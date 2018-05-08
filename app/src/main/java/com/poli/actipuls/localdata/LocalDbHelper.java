package com.poli.actipuls.localdata;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.nfc.Tag;
import android.util.Log;

import com.poli.actipuls.MockPulseGenerator;
import com.poli.actipuls.SensorActivity;
import com.poli.actipuls.data.RemoteDatabaseHelper;
import com.poli.actipuls.localdata.LocalDbPulseContract.LocalDbPulseEntry;

import java.util.ArrayList;
import java.util.List;

public class LocalDbHelper extends SQLiteOpenHelper {

    // the database name
    private static final String DATABASE_NAME = "actipuls.db";
    // Tag for Logging
    private final static String TAG = LocalDbHelper.class.getSimpleName();
    // Get the database
    private final SQLiteDatabase db = getWritableDatabase();
    // database version
    private static final int DATABASE_VERSION = 1;
    // the pulse that will be further transmitted
    private int pulse;
    // the array of pulse data
    private List<Integer> pulseList = new ArrayList<>(3);
    // initialize the helper for the remote db
    private RemoteDatabaseHelper remoteDbHelper = new RemoteDatabaseHelper();
    // query string to calculate the sum of all heartbeats in the last 30 seconds
    private final static String SQL_SUM = "SELECT SUM(" + LocalDbPulseEntry.COLUMN_PULSE + ") FROM " + LocalDbPulseEntry.TABLE_NAME + " WHERE " + LocalDbPulseEntry.COLUMN_PROCESSED + " = 0";

    public LocalDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * On create method
     *
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
     * This method calculates the final pulse that will be transmitted to the remote DB
     */
    public void calculateFinalPulse(String userId) {
        Log.i(TAG, "Calculating final pulse");
        int sum = 0;
        if (!pulseList.isEmpty()) {
            for (Integer pulse : pulseList) {
                // we make a sum of all Heartbeats in the list
                sum += pulse;
            }
        } else {
            Log.i(TAG, "List is empty");
        }
        // this is the avreage heartbeat in 10 seconds
        float avreage = sum / 3;
        Log.i(TAG, "Avreage per 10 seconds is" + avreage );
        // pulse is calculated at 60 seconds
        // so it is the avreage of ten seconds multiplied by 6
        pulse = Math.round(avreage * 6);
        Log.i(TAG, "Final pulse is" + pulse );
        // send the data to the remote DB
        remoteDbHelper.addItem(pulse, false, userId);
    }

    /**
     * This method adds the 10 second pulse measurement to a list
     *
     * @param pulse
     */
    private void addToPulseList(int pulse) {
        if (pulseList.size() >= 3) {
           // cleaning pulse list
            pulseList.clear();
        }
        // adding to the pulse list
        pulseList.add(pulse);
    }

    /**
     * Calculates a sum of all the beats received in the last 30 seconds
     */
    public void calculatePulse(String userId) {
        // execute the raw queries
        Cursor cursor = db.rawQuery(SQL_SUM, null);
        // setting the proccesed data entries to 1
        // return the data
        if (cursor.moveToFirst()) {
            int data = cursor.getInt(0);
            Log.i(TAG, "!------------ HeartsBeats per 10 sec : " + data);
            // we add to the list the number of heartbeats per 10 seconds
            addToPulseList(data);
            // calculate per 60 seconds
            int finalData = data * 6;
            // verify if the 10 second measurement is in the normal pulse spectrum
            if(!isPulseNormal(finalData)){
                remoteDbHelper.addAlertItem(finalData, userId);
            }
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
     * Method to delete the processed rows
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
    public void closeLocalDb() {
        db.close();
    }

    /**
     * This method calculates if the 10 sec measurement is a healthy pulse
     * @param newData represents the 10 sec measurement
     * @return
     */
    private boolean isPulseNormal(int newData) {
        // calculate the pulse for 60 seconds
        boolean normal = false;
        if (newData >= SensorActivity.getPulsMin() && newData <= SensorActivity.getPulsMax()) {
            normal = true;
        }
        return normal;
    }

}
