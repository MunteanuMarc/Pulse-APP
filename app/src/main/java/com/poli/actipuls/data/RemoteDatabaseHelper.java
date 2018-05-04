package com.poli.actipuls.data;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.poli.actipuls.MockPulseGenerator;
import com.poli.actipuls.accelerometer.AccelerometerHelper;
import com.poli.actipuls.model.Activitati;
import com.poli.actipuls.model.HealthData;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class RemoteDatabaseHelper {

    private MobileServiceTable<Activitati> actionsTable;

    private MobileServiceTable<HealthData> sensorTable;

    private AccelerometerHelper accHelper = new AccelerometerHelper();

    private MobileServiceClient client;
    // Tag for Logging
    private final static String TAG = RemoteDatabaseHelper.class.getSimpleName();

    public RemoteDatabaseHelper() {
        client = AzureServiceAdapter.getInstance().getClient();
    }

    /**
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MobileServiceException
     */
    public List<Activitati> getItemsFromTable() throws ExecutionException, InterruptedException, MobileServiceException {
        actionsTable = client.getTable("Consultatii", Activitati.class);
        return actionsTable.execute().get();
    }

    /**
     * Add an item to the HealthData tracking Table
     *
     * @param item The item to Add
     */
    public void addItemInTable(HealthData item) throws ExecutionException, InterruptedException {
        sensorTable = client.getTable("CosbucIuliana", HealthData.class);
        HealthData data = sensorTable.insert(item).get();
        Log.v(TAG, data + "");
    }

    /**
     * Get the Mobile Service Table instance to use
     *
     * @return
     */
    public MobileServiceClient getClient() {
        client = AzureServiceAdapter.getInstance().getClient();
        return client;
    }

    public void addAlertItem(int pulse){
        boolean alert = false;
        // verify if pulse is under the normal limit
        if (pulse < 60) {
            alert = true;
            Log.i(TAG, "Pulse is too low");
        }
        // verify if pulse is over the normal limit and the person is not exercising
        else if (pulse > 100 && !isExercising()) {
            alert = true;
            Log.i(TAG, "Pulse is too high");
        }
        addItem(pulse, alert);
    }
    /**
     * Add a new item
     */
    public void addItem(int pulse, boolean alert) {
        if (getClient() == null) {
            return;
        }
        if(alert){
            // TODO implement code to ask and send message
        }
        // Create a new item
        final HealthData item = new HealthData();

        item.setAccelerometru(accHelper.getAccelerometerData());
        item.setPuls(String.valueOf(pulse));
        item.setAlert(alert);

        // Insert the new item
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    addItemInTable(item);

                } catch (final Exception e) {
                    Log.e(TAG, "insert Error" + e);
                }
                return null;
            }
        };

        runAsyncTask(task);
    }

    /**
     * Run an ASync task on the corresponding executor
     *
     * @param task
     * @return
     */
    private AsyncTask<Void, Void, Void> runAsyncTask(AsyncTask<Void, Void, Void> task) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            return task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            return task.execute();
        }
    }
    /**
     * Verifies if there is movement according to the accelerometer
     *
     * @return
     */
    public boolean isExercising() {
        if (accHelper.getAccelerationSquareRoot() > 5) {
            return true;
        }
        return false;
    }
}
