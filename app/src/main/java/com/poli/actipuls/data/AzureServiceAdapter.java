package com.poli.actipuls.data;

import android.content.Context;
import android.util.Log;

import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.http.OkHttpClientFactory;
import com.squareup.okhttp.OkHttpClient;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

/**
 * This class handles the Azure remote connection
 */
public class AzureServiceAdapter {
    // mobile resource link
    private String mMobileBackendUrl = "https://actipulse.azurewebsites.net";
    // the context
    private Context mContext;
    // mobile service client
    private MobileServiceClient mClient;
    private static AzureServiceAdapter mInstance = null;
    // Tag for Logging
    private final static String TAG = AzureServiceAdapter.class.getSimpleName();

    // Constructor
    private AzureServiceAdapter(Context context) {
        mContext = context;
        try {
            mClient = new MobileServiceClient(mMobileBackendUrl, mContext);
            // Extend timeout from default of 10s to 20s
            mClient.setAndroidHttpClientFactory(new OkHttpClientFactory() {
                @Override
                public OkHttpClient createOkHttpClient() {
                    // create a new httpclient
                    OkHttpClient client = new OkHttpClient();
                    // set the timeout
                    client.setReadTimeout(20, TimeUnit.SECONDS);
                    client.setWriteTimeout(20, TimeUnit.SECONDS);
                    return client;
                }
            });
            // catch the malformed exception
        } catch (MalformedURLException e) {
            Log.v("Exception", "Malformed Exception");
        }
    }

    /*
     *Initialization of the Azure Service Adapter
     */
    public static void Initialize(Context context) {
        if (mInstance == null) {
            mInstance = new AzureServiceAdapter(context);
        } else {
            Log.i(TAG, "Already initialized");
        }
    }

    /**
     * Returns a instance of the adapter
     * @return
     */
    public static AzureServiceAdapter getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("AzureServiceAdapter is not initialized");
        }
        return mInstance;
    }

    /**
     * Gets a mobile service client
     * @return
     */
    public MobileServiceClient getClient() {
        return mClient;
    }

}
