package com.poli.actipuls.bluetooth;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;


public class BluetoothHelper {

    // Strings for broadcasting
    public final static String ACTION_GATT_CONNECTED =
            "com.poli.actipulse.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED =
            "com.poli.actipulse.ACTION_GATT_DISCONNECTED";
    // Tag for Logging
    private final static String TAG = BluetoothHelper.class.getSimpleName();
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private Activity myActivity;
    private BluetoothGatt bluetoothGatt;
    // BluetoothGatt callback
    private final BluetoothGattCallback gattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            String intentAction;
            // verify the state log and update the UI accordingly
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                intentAction = ACTION_GATT_CONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery:");
                bluetoothGatt.discoverServices();
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                intentAction = ACTION_GATT_DISCONNECTED;
                broadcastUpdate(intentAction);
                Log.i(TAG, "Disconnected from GATT server.");
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            if (status == BluetoothGatt.GATT_SUCCESS) {
                // TODO - implement code for Arduino
                Log.i(TAG, "Services Discovered.");
            } else {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }

        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic,
                                         int status) {
            // TODO - Implement for Arduino
            if (status == BluetoothGatt.GATT_SUCCESS) {
                Log.i(TAG, "Characteristics read.");
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            // TODO - Implement for Arduino
            Log.v(TAG, "Characteristics changed");
        }
    };

    /**
     * Constructor
     * @param a
     */
    public BluetoothHelper(Activity a) {
        myActivity = a;
    }

    /**
     * Initializes a Bluetooth adapter.
     */
    public BluetoothAdapter getBluetoothAdapter(Context context) {
        final BluetoothManager bluetoothManager = (BluetoothManager) context.getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager == null) {
            return null;
        } else {
            return bluetoothManager.getAdapter();
        }
    }

    /**
     * Method to get the necessary permissions if needed
     *
     * @param bluetoothAdapter
     */
    public void getPermissions(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            blueToothEnable();
        } else if (!hasLocationPermissions()) {
            requestLocationPermission();
        }
    }

    /**
     * Verify if the app has location Permissions
     * @return
     */
    private boolean hasLocationPermissions() {
        return myActivity.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Ask permission to use Location
     */
    private void requestLocationPermission() {
        myActivity.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Ask permission to enable bluetooth
     */
    private void blueToothEnable() {
        Intent enableAdapter = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        myActivity.startActivityForResult(enableAdapter, 0);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Connect to Gatt server
     * @param myDevice
     * @param context
     */
    public void connectToGattServer(BluetoothDevice myDevice, Context context) {
        bluetoothGatt = myDevice.connectGatt(context, true, gattCallback);
    }

    /**
     *  Disconnect from Gatt server
     * @param bluetoothAdapter
     */
    public void disconnect(BluetoothAdapter bluetoothAdapter) {
        if (bluetoothAdapter == null || bluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        bluetoothGatt.disconnect();
    }

    /**
     * close client so the system can release resources
     */
    // TODO use close
    public void close() {
        if (bluetoothGatt == null) {
            return;
        }
        bluetoothGatt.close();
        bluetoothGatt = null;
    }

    /**
     * Method to broadcast the state for UI update
     *
     * @param action
     */
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        myActivity.sendBroadcast(intent);
    }
}