package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

/**
 * Created by romanpulov on 25.08.2016.
 */
public class DateFormatTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DateFormatTest";
    private static void log(String message) {
        Log.d(TAG, message);
    }

    public DateFormatTest() {
        super(Application.class);
    }

    public void test1() {
        log("Test message");
        assertEquals(1, 2);
    }
}
