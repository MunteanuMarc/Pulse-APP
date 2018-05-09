package com.poli.actipuls;

import android.app.Instrumentation;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class CalendarActivityTest {

    @Rule
    public ActivityTestRule<CalendarActivity> mActivityTestRule=new ActivityTestRule<CalendarActivity>(CalendarActivity.class);
    private CalendarActivity calendarActivity=null;


    Instrumentation.ActivityMonitor monitor=getInstrumentation().addMonitor(CalendarActivity.class.getName(),null,false);



    @Before
    public void setUp() throws Exception {
        calendarActivity =mActivityTestRule.getActivity();
    }

    /**
     * Launch Activity test
     * **/
    @Test
    public void testLaunch(){

        View view =calendarActivity.findViewById(R.id.MyDate);
        View view2 =calendarActivity.findViewById(R.id.calendarView);


        Assert.assertNotNull(view);
        Assert.assertNotNull(view2);

    }
    /*
    @Test
    public void testMenu(){
        Assert.assertNotNull(calendarActivity.findViewById(R.id.calendarView));
        onView(withId(R.id.calendarView)).perform(click());
        Activity calendarActivity = getInstrumentation().waitForMonitorWithTimeout(monitor,5000);
        // getInstrumentation().invokeMenuActionSync(calendarActivity, R.id.calendarView, 0);
        //  getInstrumentation().waitForMonitorWithTimeout(monitor, 1000);
        //   Assert.assertEquals(true, getInstrumentation().checkMonitorHit(monitor, 1));

        Assert.assertNotNull(calendarActivity);
        calendarActivity.finish();

    }
    */


    @Test
    public void onCreate() {
    }

    @Test
    public void onCreateOptionsMenu() {
    }

    @Test
    public void onOptionsItemSelected() {
    }

    @After
    public void tearDown() throws Exception {
        calendarActivity=null;
    }

}
