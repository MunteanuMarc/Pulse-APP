package com.poli.actipuls.localdata;

import android.provider.BaseColumns;

/**
 * This class represents the contract eoth the local database
 */
public class LocalDbPulseContract {

    public static final class LocalDbPulseEntry implements BaseColumns {
        // local database table name
        public static final String TABLE_NAME = "localpulse";
        // local database columns
        public static final String COLUMN_PULSE = "pulse";
        public static final String COLUMN_PROCESSED = "processed";
    }
}
