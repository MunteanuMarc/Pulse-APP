package com.poli.actipuls.localdata;

import android.app.Activity;
import android.util.Log;

import com.poli.actipuls.data.RemoteDatabaseHelper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
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
    private ScheduledExecutorService scheduler;

    /**
     * Constructor
     * @param a represents the activity that will use the Scheduler
     */
    public ScheduleController(Activity a) {
        myActivity = a;
        dbHelper = new LocalDbHelper(myActivity);
    }

    public void startScheduler(String userId) {
        scheduler = Executors.newScheduledThreadPool(1);
        // Calculate the Pulse every 30 seconds
        scheduler.scheduleAtFixedRate(new Runnable() {
                                          public void run() {
                                              Log.i(TAG, "Calculating pulse 10 sec ----------");
                                              dbHelper.calculatePulse(userId);
                                          }
                                      },
                10,
                10,
                TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new Runnable() {
                                          public void run() {
                                              Log.i(TAG, "Calculating pulse 30 sec ----------");
                                              dbHelper.calculateFinalPulse(userId);
                                          }
                                      },
                30,
                30,
                TimeUnit.SECONDS);

        // Clean the local database every 3 minutes
        scheduler.scheduleAtFixedRate(new Runnable() {
                                          public void run() {
                                              Log.i(TAG, "Cleaning in process ----------");
                                              dbHelper.deleteProcessedData();
                                          }
                                      },
                180,
                180,
                TimeUnit.SECONDS);
        // insert pulse data in local database every second
        scheduler.scheduleAtFixedRate(new Runnable() {
                                          public void run() {
                                              dbHelper.insertData();
                                          }
                                      },
                0,
                1,
                TimeUnit.SECONDS);
    }

    /*
    Method to shut down the executor and close the local DB
     */
    public void shutDown() {
        scheduler.shutdown();
        dbHelper.closeLocalDb();
    }
}
