package com.poli.actipuls;

import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginTest {

    @Rule
    public ActivityTestRule<Login> mActivityTestRule=new ActivityTestRule<Login>(Login.class);
    private Login login=null;
    private static EditText username;
    private static EditText password;


    @Before
    public void setUp() throws Exception {
        login=mActivityTestRule.getActivity();
    }
    @Test
    public void testLoginAct(){
        View view   =login.findViewById(R.id.editText1);
        View view2  =login.findViewById(R.id.editText2);
        View view3  =login.findViewById(R.id.loginbutton);

        assertNotNull(view);
        assertNotNull(view2);
        assertNotNull(view3);

    }
    @Test
    public void setLogin() throws Exception {

        login = mActivityTestRule.getActivity();

        username= (EditText) login.findViewById( R.id.editText1 );
        password = (EditText) login.findViewById(R.id.editText2);
        String user = username.getText().toString();
        String pass = password.getText().toString();
        assertNotNull(username);
        assertNotNull(password);


    }
    @Test
    public void setLogin2() throws Exception {

        login = mActivityTestRule.getActivity();

        username = (EditText) login.findViewById( R.id.editText1 );
        password = (EditText) login.findViewById( R.id.editText2 );
        String user = username.getText().toString();
        String pass = password.getText().toString();

        assertEquals("", username.getText().toString());
        assertEquals("",password.getText().toString());
    }
    
    
    
    @Test
    public void testText1(){
        View view   =login.findViewById(R.id.editText1);
        assertNotNull(view);
    }
    @Test
    public void testText2(){
        View view   =login.findViewById(R.id.editText2);
        assertNotNull(view);
    }
    @Test
    public void testButtonLogin(){
        View view   =login.findViewById(R.id.loginbutton);
        assertNotNull(view);
    }

    @Test
    public void onCreate() {
    }
    @After
    public void tearDown() throws Exception {
        login=null;
    }
}
