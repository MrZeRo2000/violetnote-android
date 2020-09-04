package com.romanpulov.violetnote;

import androidx.test.filters.SmallTest;
import android.util.Log;

import org.junit.*;

import static org.junit.Assert.assertEquals;

@Ignore
@SmallTest
public class DataGeneratorTest {
    private final static String TAG = "DataGeneratorTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Data Generator test message");
        assertEquals(1, 1);
    }

    @Test
    public void generateData() {
        DataGenerator d = new DataGenerator();
        d.generateData();
    }

}
