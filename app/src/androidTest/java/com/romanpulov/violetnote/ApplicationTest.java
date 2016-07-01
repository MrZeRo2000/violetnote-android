package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.dropbox.DropBoxHelper;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private final static String TAG = "ApplicationTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public ApplicationTest() {
        super(Application.class);
    }

    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    public void test2() {
        DropBoxHelper dh = DropBoxHelper.getInstance(getContext());
        log("Token: " + dh.getAccessToken());
    }

}