package com.poli.actipuls;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertNotNull;

public class MainActivityTest {

    //test rule enables launching the activity
    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule=new ActivityTestRule<MainActivity>(MainActivity.class);
    private MainActivity mainActivity=null;
   Instrumentation.ActivityMonitor monitor=getInstrumentation().addMonitor(MainActivity.class.getName(),null,false);

    @Before
    public void setUp() throws Exception {
        mainActivity=mActivityTestRule.getActivity();
    }
    /**
     * test launch  Activity
     * **/
    @Test
    public void testLaunch(){
        View view  =mainActivity.findViewById(R.id.all_activities_list_view);
        View view2 =mainActivity.findViewById(R.id.fab);
       // View view3 =mainActivity.findViewById(R.id.activity_time);
      //  View view4 =mainActivity.findViewById(R.id.activity_name);
      //  View view5 =mainActivity.findViewById(R.id.activity_date);

        assertNotNull(view);
        assertNotNull(view2);
     //   assertNotNull(view3);
     //   assertNotNull(view4);
       // assertNotNull(view5);

    }

    /**
     * test view  Activity
     * **/
    @Test
    public void testView(){
        View view =mainActivity.findViewById(R.id.all_activities_list_view);
        org.junit.Assert.assertNotNull(view);

    }
    /**
     * test fab button  Activity
     * **/
    @Test
    public void testfab(){
        View view =mainActivity.findViewById(R.id.fab);
       org.junit.Assert.assertNotNull(view);

    }

    /**
     * test time  Activity
     * **/
    /*
    @Test
    public void testActTime(){
        View view =mainActivity.findViewById(R.id.activity_time);
        org.junit.Assert.assertNotNull(view);

    }
    /*
    @Test
    public void testActName(){
        View view =mainActivity.findViewById(R.id.activity_name);
       org.junit.Assert.assertNotNull(view);

    }

    @Test
    public void testAcTDate(){
        View view =mainActivity.findViewById(R.id.activity_date);
       org.junit.Assert.assertNotNull(view);

    }
    */


    /**
     * test functionality Floating Action Button
     * **/
    /*@Test
    public void testFloatActionButton(){
       assertNotNull(mainActivity.findViewById(R.id.fab));
        onView(withId(R.id.fab)).perform(click());
        Activity mainActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
       assertNotNull(mainActivity);
        mainActivity.finish();

    }*/


    @Test
    public void onCreate() {
    }

    @Test
    public void onCreateOptionsMenu() {
    }

    @Test
    public void onOptionsItemSelected() {
        //  onView(withId(R.id.calendar)).perform(click());
        // onView(withId(R.id.list)).perform(click());
    }


    @After
    public void tearDown() throws Exception {
        mainActivity=null;
    }

}
