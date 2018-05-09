package com.poli.actipuls;


import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.poli.actipuls.data.AzureServiceAdapter;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;




public class SensorActivityTest {

    @Rule
    public ActivityTestRule<SensorActivity> mActivityTestRule=new ActivityTestRule<SensorActivity>(SensorActivity.class);
    public ActivityTestRule<AzureServiceAdapter> mActivityTestRule2=new ActivityTestRule<AzureServiceAdapter>(AzureServiceAdapter.class);
    private SensorActivity sensorActivity=null;
    Instrumentation.ActivityMonitor monitor=getInstrumentation().addMonitor(SensorActivity.class.getName(),null,false);
    private AzureServiceAdapter azureServiceAdapter =null;

    public SensorActivityTest( Context context ) {
   //     super(context);
    }


    @Before
    public void setUp() throws Exception {
        sensorActivity =mActivityTestRule.getActivity();
        azureServiceAdapter=mActivityTestRule2.getActivity();
    }
    /**
     * test to see if activities are launched
     * **/
    @Test
    public void testLaunch(){

        View view =sensorActivity.findViewById(R.id.btnStart);
        View view2 =sensorActivity.findViewById(R.id.btnStop);
        View view3 =sensorActivity.findViewById(R.id.device_name);
        View view4 =sensorActivity.findViewById(R.id.connectionState);
        View view5 =sensorActivity.findViewById(R.id.progressBar_cyclic);

        Assert.assertNotNull(view);
        Assert.assertNotNull(view2);
        Assert.assertNotNull(view3);
        Assert.assertNotNull(view4);
        Assert.assertNotNull(view5);


    }

    @Test
    public void testStartButton2() {

        View view = azureServiceAdapter.findViewById(R.id.btnStart);
        Assert.assertNotNull(view);
    }
    @Test
    public void testStopButton2() {

        View view = azureServiceAdapter.findViewById(R.id.btnStop);
        Assert.assertNotNull(view);
    }
    @Test
    public void testdDviceName() {

        View view = azureServiceAdapter.findViewById(R.id.device_name);
        Assert.assertNotNull(view);
    }
    @Test
    public void testConnectionState() {

        View view = azureServiceAdapter.findViewById(R.id.connectionState);
        Assert.assertNotNull(view);
    }
    @Test
    public void testProgressBar() {

        View view = azureServiceAdapter.findViewById(R.id.progressBar_cyclic);
        Assert.assertNotNull(view);
    }
    /**
     * test functionality Floating Action Button
     * **/
    @Test
    public void testStartButton() {
        Assert.assertNotNull(sensorActivity.findViewById(R.id.btnStart));
        onView(withId(R.id.btnStart)).perform(click());
        Activity sensorActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        Assert.assertNotNull(sensorActivity);
        sensorActivity.finish();
    }

    /**
     * test functionality Floating Action Button
     * **/
    @Test
    public void testStopButton() {
        Assert.assertNotNull(sensorActivity.findViewById(R.id.btnStop));
        onView(withId(R.id.btnStop)).perform(click());
        Activity sensorActivity = getInstrumentation().waitForMonitorWithTimeout(monitor, 5000);
        Assert.assertNotNull(sensorActivity);
        sensorActivity.finish();
    }
    @Test
    public void onCreate() {
    }

    @Test
    public void connectGattService() {
    }

    @Test
    public void onResume() {
    }

    @Test
    public void onPause() {
    }

    @Test
    public void onSensorChanged() {
    }

    @Test
    public void onAccuracyChanged() {
    }

    @Test
    public void getPulsMin() {
    }

    @Test
    public void getPulsMax() {
    }

    @Test
    public void setPulsMin() {
    }

    @Test
    public void setPulsMax() {
    }
    @After
    public void tearDown() throws Exception {
        sensorActivity=null;
        azureServiceAdapter=null;
    }
}
