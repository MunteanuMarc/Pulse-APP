package com.poli.actipuls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.poli.actipuls.data.AzureServiceAdapter;
import com.poli.actipuls.data.RemoteDatabaseHelper;
import com.poli.actipuls.model.Activitati;
import com.poli.actipuls.model.LoginModel;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {

    private static EditText username;
    private static EditText password;
    private static Button login_button;
    private RemoteDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AzureServiceAdapter.Initialize(this);
        dbHelper = new RemoteDatabaseHelper();
        username = (EditText) findViewById(R.id.editText1);
        password = (EditText) findViewById(R.id.editText2);
        login_button = (Button) findViewById(R.id.loginbutton);
        login_button.setOnClickListener((View v) -> {
            String user = username.getText().toString();
            String pass = password.getText().toString();
            checkCredentials(user, pass);
               /* Toast.makeText(Login.this, "Username and password is correct",
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);

            } else {
                Toast.makeText(Login.this, "Username and password is NOT correct",
                        Toast.LENGTH_SHORT).show();

            }*/
        });
    }

    private void checkCredentials(String user, String pass) {
        boolean status = false;
        // Get the items that weren't marked as completed and add them in the
        // adapter
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    List<LoginModel> results = dbHelper.getLoginTable();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            boolean found = false;
                            for (LoginModel result : results) {
                                if (result.getUsername().equals(user) && result.getPassword().equals(pass)) {
                                    found = true;
                                    Toast.makeText(Login.this, "Username and password is correct",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            }
                            if(!found){
                                Toast.makeText(Login.this,"Username and password is NOT correct",
                                        Toast.LENGTH_SHORT).show();
                                username.setText("");
                                password.setText("");
                            }
                        }
                    });

                } catch (final Exception e) {
                    Log.v("Error", "Error");
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
}
