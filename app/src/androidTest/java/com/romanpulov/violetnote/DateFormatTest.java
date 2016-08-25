package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.text.format.DateFormat;
import android.util.Log;

import java.util.Date;

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

        Date dt = new Date();

        log(DateFormat.getDateFormat(getContext()).format(dt));
        log(DateFormat.getTimeFormat(getContext()).format(dt));
    }
}
