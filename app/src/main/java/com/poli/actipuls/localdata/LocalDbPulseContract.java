package com.poli.actipuls.localdata;

import android.provider.BaseColumns;

public class LocalDbPulseContract {
    public static final class LocalDbPulseEntry implements BaseColumns {

        public static final String TABLE_NAME = "localpulse";
        public static final String COLUMN_PULSE = "pulse";
        public static final String COLUMN_PROCESSED = "processed";
    }
}
