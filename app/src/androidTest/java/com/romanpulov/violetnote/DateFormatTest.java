package com.romanpulov.violetnote;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import java.util.Date;

import androidx.test.filters.SmallTest;

import org.junit.*;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

/**
 * Created by romanpulov on 25.08.2016.
 */
@SmallTest
public class DateFormatTest {
    private final static String TAG = "DateFormatTest";
    private static void log(String message) {
        Log.d(TAG, message);
    }

    private Context getTargetContext() {
        return getInstrumentation().getTargetContext();
    }

    @Test
    public void test1() {
        log("Test message");

        Date dt = new Date();

        log(DateFormat.getDateFormat(getTargetContext()).format(dt));
        log(DateFormat.getTimeFormat(getTargetContext()).format(dt));
    }
}
