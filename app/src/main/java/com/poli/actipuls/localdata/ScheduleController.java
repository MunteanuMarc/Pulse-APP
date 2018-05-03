package com.poli.actipuls.localdata;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * This class contains tha actions performed on the data received from the BLE
 */
public class ScheduleController {

    // Tag for Logging
    private final static String TAG = ScheduleController.class.getSimpleName();

    // Create a DB helper (this will create the DB if run for the first time)
    LocalDbHelper dbHelper;

    private Activity myActivity;
    private SQLiteDatabase db;
    private int pulse;
    private ScheduledExecutorService scheduler;

    /**
     * @param a
     */
    public ScheduleController(Activity a) {
        myActivity = a;
        dbHelper = new LocalDbHelper(myActivity);
        db = dbHelper.getWritableDatabase();
    }
    public void startScheduler() {
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
                                                   public void run() {
                                                       Log.i(TAG, "Calculating pulse ----------");
                                                       dbHelper.calculatePulse();
                                                   }
                                               },
                0,
                30,
                TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new Runnable() {
                                                    public void run() {
                                                        Log.i(TAG, "Cleaning in process ----------");
                                                        dbHelper.deleteProcessedData();
                                                    }
                                                },
                10,
                40,
                TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new Runnable() {
                                                    public void run() {
                                                        dbHelper.insertData();
                                                    }
                                                },
                0,
                1,
                TimeUnit.SECONDS);
    }

    public int getPulse() {
        return pulse;
    }

    public void shutDownExecutor() {
        scheduler.shutdown();
    }
}
