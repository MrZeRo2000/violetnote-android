package com.romanpulov.violetnote;

import android.text.format.DateFormat;
import android.util.Log;
import java.util.Date;

import androidx.test.filters.SmallTest;
import org.junit.*;
import static androidx.test.platform.app.InstrumentationRegistry.getTargetContext;
import static androidx.test.platform.app.InstrumentationRegistry.getContext;

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
