package pl.com.weddingPlanner.util;

import android.util.Log;

import org.apache.commons.lang3.StringUtils;

import pl.com.weddingPlanner.BuildConfig;

public class Logger {

    private static final String EMPTY = "";

    public static void logToDevice(String tag, Throwable t) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, StringUtils.defaultIfEmpty(t.getMessage(), EMPTY));
        }
    }

    public static void logToDevice(String tag, Exception e) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, StringUtils.defaultIfEmpty(e.getMessage(), EMPTY));
        }
    }

    public static void logToDevice(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, StringUtils.defaultIfEmpty(msg, EMPTY));
        }
    }
}
