package com.poli.actipuls;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.poli.actipuls.data.AzureServiceAdapter;
import com.poli.actipuls.data.DatabaseHelper;
import com.poli.actipuls.data.ListActionAdapter;
import com.poli.actipuls.model.Activitati;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // TODO optimisation on the application : "The application may be doing too much work on its main thread".

    private List<Activitati> results;
    private RecyclerView activitiesRecyclerView;
    private ListActionAdapter mAdapter;
    private MobileServiceClient client;
    private DatabaseHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener((View v) -> {
            Intent intent = new Intent(MainActivity.this, SensorActivity.class);
            startActivity(intent);
        });

        try {
            AzureServiceAdapter.Initialize(this);
            dbHelper = new DatabaseHelper();

            // Set local attributes to corresponding views
            activitiesRecyclerView = this.findViewById(R.id.all_activities_list_view);

            // Set layout for the RecyclerView, because it's a list we are using the linear layout
            activitiesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            mAdapter = new ListActionAdapter(this);

            refreshItemsFromTable();



        } catch (Exception e) {
            Log.v("Exception", "Interuuuuuuuuuuuuuuuuuuupteeeeeeeeeeeeeeed-----------------------");
        }
    }

    /**
     * Refresh the list with the items in the Table
     */
    private void refreshItemsFromTable() {

        // Get the items that weren't marked as completed and add them in the
        // adapter
        AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    final List<Activitati> results = dbHelper.getItemsFromTable();

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.getResultList().clear();

                            mAdapter.setResultList(results);

                            // Link the adapter to the RecyclerView
                            activitiesRecyclerView.setAdapter(mAdapter);
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
