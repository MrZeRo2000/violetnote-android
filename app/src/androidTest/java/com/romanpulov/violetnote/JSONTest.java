package com.romanpulov.violetnote;

import android.util.Log;

import android.support.test.filters.SmallTest;
import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rpulov on 17.10.2016.
 */
@SmallTest
public class JSONTest {
    private final static String TAG = "DBJSONTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void test2() {
        JSONObject jo = new JSONObject();
        try {

            jo.put("Name", "Name 1");
            jo.put("Value", "Value 1");

            log(jo.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jr;
        try {
            jr = new JSONObject(jo.toString());

            log("Name:" + jr.optString("Name"));
            log("Value:" + jr.optString("Value"));
            log("Value1:" + jr.optString("Value1"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
