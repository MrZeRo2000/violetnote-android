package com.romanpulov.violetnote;

import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.Log;
import java.util.Date;

import android.support.test.filters.SmallTest;
import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.InstrumentationRegistry.getContext;
import static org.junit.Assert.*;

/**
 * Created by romanpulov on 25.08.2016.
 */
@SmallTest
public class DateFormatTest {
    private final static String TAG = "DateFormatTest";
    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");

        Date dt = new Date();

        log(DateFormat.getDateFormat(getTargetContext()).format(dt));
        log(DateFormat.getTimeFormat(getTargetContext()).format(dt));
    }
}
