package com.poli.actipuls.bluetooth;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.poli.actipuls.MockPulseGenerator;
import com.poli.actipuls.data.LocalDbHelper;
import com.poli.actipuls.data.LocalDbPulseContract.LocalDbPulseEntry;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class contains tha actions performed on the data received from the BLE
 */
public class BluetoothController {

    // Tag for Logging
    private final static String TAG = BluetoothController.class.getSimpleName();
    // Create a DB helper (this will create the DB if run for the first time)
    LocalDbHelper dbHelper;

    private Activity myActivity;
    private SQLiteDatabase db;
    private int pulse;
    private ScheduledFuture<?> myTask;
    private ScheduledFuture<?> myTask2;
    private ScheduledFuture<?> myTask3;
    private ScheduledExecutorService scheduler;

    /**
     * @param a
     */
    public BluetoothController(Activity a) {
        myActivity = a;
        dbHelper = new LocalDbHelper(myActivity);
        db = dbHelper.getWritableDatabase();
    }

    // TODO fix scheduler - does not work correctly
    public void startScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);
        myTask = scheduler.scheduleAtFixedRate(new Runnable() {
                                                   public void run() {
                                                       db.delete(LocalDbPulseEntry.TABLE_NAME, LocalDbPulseEntry.COLUMN_PROCESSED, new String[]{"1"});
                                                       Log.i(TAG, "Cleaning in process ----------");
                                                   }
                                               },
                0,
                40,
                TimeUnit.SECONDS);
        myTask2 = scheduler.scheduleAtFixedRate(new Runnable() {
                                                    public void run() {
                                                        insertData();
                                                    }
                                                },
                0,
                1,
                TimeUnit.SECONDS);
        myTask3 = scheduler.scheduleAtFixedRate(new Runnable() {
                                                    public void run() {
                                                        pulse = calculatePulse();
                                                        Log.i(TAG, "!------------ Avreage Pulse is : " + pulse * 2);
                                                    }
                                                },
                0,
                30,
                TimeUnit.SECONDS);
    }

    /**
     * Calculates a sum of all the beats received in the last 30 seconds
     */
    private int calculatePulse() {
        // query string to calculate the sum of all heartbeats in the last 30 seconds
        String sql = "SELECT SUM(" + LocalDbPulseEntry.COLUMN_PULSE + ") FROM " + LocalDbPulseEntry.TABLE_NAME + " WHERE " + LocalDbPulseEntry.COLUMN_PROCESSED + "= 0";
        // query string to set table entried as processed in order for cleaning
        ContentValues cv = new ContentValues();
        cv.put(LocalDbPulseEntry.COLUMN_PROCESSED, 1);
        db.update(LocalDbPulseEntry.TABLE_NAME, cv, null, null);
        Log.i(TAG, sql);
        // execute the raw queries
        Cursor cursor = db.rawQuery(sql, new String[]{"0"});
        // return the data
        if (cursor.moveToFirst()) {
            int sum = cursor.getInt(0);
            Log.i(TAG, "!------------ HeartsBeats per 30 sec : " + sum);
            return sum;
        }
        cursor.close();
        return 0;
    }


    /**
     * Method to insert data in local DB
     */
    public void insertData() {
        if (db == null) {
            return;
        }
        Cursor cursor = getAllDataInTable();

        ContentValues cv = new ContentValues();
        // for testing purposes we are using a generated pulse
        cv.put(LocalDbPulseEntry.COLUMN_PULSE, MockPulseGenerator.generatePulse());
        cv.put(LocalDbPulseEntry.COLUMN_PROCESSED, 0);


        //insert in one transaction
        try {
            db.beginTransaction();
            db.insert(LocalDbPulseEntry.TABLE_NAME, null, cv);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
            Log.e(TAG, "Exception SQL" + e);
        } finally {
            db.endTransaction();
        }

    }

    /**
     * @return All the Data in the table
     */
    public Cursor getAllDataInTable() {
        return db.query(
                LocalDbPulseEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
    }


    public int getPulse() {
        return pulse;
    }

    public void shutDownExecutor() {
        scheduler.shutdown();
    }
}
