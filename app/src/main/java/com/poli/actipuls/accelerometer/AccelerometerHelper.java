package com.poli.actipuls.accelerometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class AccelerometerHelper  implements SensorEventListener {

    // TODO accelerometer not working properly anymore - check 

    private long lastUpdate;
    // initialize SensorManager
    private String accelerometerData = "";
    private float accelerationSquareRoot;
    // Tag for Logging
    private final static String TAG = AccelerometerHelper.class.getSimpleName();

    /**
     *  Method to get the accelerometer data
     * @param event
     */
    public void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        // Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];
        StringBuilder sb = new StringBuilder()
                .append(x)
                .append("#")
                .append(y)
                .append("#")
                .append(z);
        accelerationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        accelerometerData = String.valueOf(x + "#" + y +"#" + z);
        long actualTime = event.timestamp;
        if (accelerationSquareRoot >= 10)
        {
            Log.i(TAG, "is moving " + getAccelerometerData());
            if (actualTime - lastUpdate < 200) {
                return;
            }
            lastUpdate = actualTime;
            accelerometerData = String.valueOf(x + "#" + y +"#" + z);
        }
    }

    /**
     * Getter for the accelerometer data as string
     * @return
     */
    public String getAccelerometerData() {
        Log.i(TAG, "Acc data is" + accelerometerData);
        return accelerometerData;
    }

    /**
     * Getter for de accelerometer square root
     * @return
     */
    public float getAccelerationSquareRoot() {
        return accelerationSquareRoot;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not implemented
    }
    /**
     * Verifies if there is movement according to the accelerometer
     *
     * @return
     */
    public boolean isExercising() {
        if (getAccelerationSquareRoot() > 20) {
            Log.i(TAG, "Acc is " + getAccelerometerData());
            return true;
        }
        return false;
    }
}