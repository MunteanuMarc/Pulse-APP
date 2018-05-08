package com.poli.actipuls.data;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.query.QueryOrder;
import com.poli.actipuls.SensorActivity;
import com.poli.actipuls.accelerometer.AccelerometerHelper;
import com.poli.actipuls.model.Activitati;
import com.poli.actipuls.model.HealthData;
import com.poli.actipuls.model.LoginModel;
import com.poli.actipuls.model.ValoriPuls;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * This class performs the remote database oprations
 */
public class RemoteDatabaseHelper {

    private AccelerometerHelper accHelper = new AccelerometerHelper();

    private MobileServiceClient client;
    // Tag for Logging
    private final static String TAG = RemoteDatabaseHelper.class.getSimpleName();

    /**
     * Constructor
     */
    public RemoteDatabaseHelper() {
        client = AzureServiceAdapter.getInstance().getClient();
    }

    /**
     * Method to get all the items from the table recomandari
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MobileServiceException
     */
    public List<Activitati> getItemsFromTable(String userId) throws ExecutionException, InterruptedException, MobileServiceException {
        return client.getTable("recomandari_tabel", Activitati.class)
                .where()
                .field("id_pacient").eq(userId)
                .orderBy("data_recomandare", QueryOrder.Ascending)
                .execute().get();
    }

    /**
     * Method to get all the items from the table valori_normale
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MobileServiceException
     */
    public List<ValoriPuls> getValuesFromTable(String userId) throws ExecutionException, InterruptedException, MobileServiceException {
        return client.getTable("valori_normale_tabel", ValoriPuls.class)
                .where()
                .field("id_pacient").eq(userId)
                .execute().get();
    }


    /**
     * Returns the login table
     *
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     * @throws MobileServiceException
     */
    public List<LoginModel> getLoginTable() throws ExecutionException, InterruptedException, MobileServiceException {
        return client.getTable("login_pacienti_tabel", LoginModel.class)
                .execute()
                .get();
    }


    /**
     * Add an item to the HealthData tracking Table
     *
     * @param item The item to Add
     */
    private void addItemInTable(HealthData item) throws ExecutionException, InterruptedException {
        client.getTable("senzori_tabel", HealthData.class)
                .insert(item).get();
    }

    /**
     * Get the Mobile Service Table instance to use
     *
     * @return
     */
    private MobileServiceClient getClient() {
        client = AzureServiceAdapter.getInstance().getClient();
        return client;
    }

    /**
     * Method to add alert item
     *
     * @param pulse
     */
    public void addAlertItem(int pulse, String userId) {
        boolean alert = false;
        // verify if pulse is under the normal limit
        if (pulse < SensorActivity.getPulsMin()) {
            alert = true;
            Log.i(TAG, "Pulse is too low");
        }
        // verify if pulse is over the normal limit and the person is not exercising
        else if (pulse > SensorActivity.getPulsMax() && !isExercising()) {
            alert = true;
            Log.i(TAG, "Pulse is too high");
        }
        // calls alertItem method
        addItem(pulse, alert, userId);
    }

    /**
     * Add a new item
     */
    public void addItem(int pulse, boolean alert, String userId) {
        if (getClient() == null) {
            return;
        }
        // Create a new item
        final HealthData item = new HealthData();

        // set the information to be sent to the remote db
        item.setAccelerometru(accHelper.getAccelerometerData());
        item.setPuls(String.valueOf(pulse));
        item.setAlert(alert);
        item.setUserId(userId);

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
