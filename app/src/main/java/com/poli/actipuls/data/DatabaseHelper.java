package com.poli.actipuls.data;

import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.MobileServiceException;
import com.microsoft.windowsazure.mobileservices.table.MobileServiceTable;
import com.poli.actipuls.model.Activitati;
import com.poli.actipuls.model.HealthData;


import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseHelper {

    private MobileServiceTable<Activitati> actionsTable;

    private MobileServiceTable<HealthData> sensorTable;

    private MobileServiceClient client;
    // Tag for Logging
    private final static String TAG = DatabaseHelper.class.getSimpleName();

    public DatabaseHelper(){
        client = AzureServiceAdapter.getInstance().getClient();
    }

    /**
     *
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
     * @param item
     *            The item to Add
     */
    public void addItemInTable(HealthData item) throws ExecutionException, InterruptedException {
        sensorTable = client.getTable("CosbucIuliana", HealthData.class);
        HealthData data = sensorTable.insert(item).get();
        Log.v(TAG, data + "");
    }

    /**
     *  Get the Mobile Service Table instance to use
     * @return
     */
    public MobileServiceClient getClient(){
        client = AzureServiceAdapter.getInstance().getClient();
        return client;
    }

}
