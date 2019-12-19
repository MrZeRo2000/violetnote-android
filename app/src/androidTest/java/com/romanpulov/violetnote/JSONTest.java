package com.romanpulov.violetnote;

import android.util.Log;

import androidx.test.filters.SmallTest;
import org.junit.*;
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

    @Test
    public void testDisplayOptions() {
        JSONObject jo = new JSONObject();

        JSONObject ind = new JSONObject();
        try {
            ind.put("display_total", true);
            ind.put("display_unchecked", false);
            ind.put("display_checked", true);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            jo.put("indicators", ind);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        log("testDisplayOptions");
        log(jo.toString());


        try {
            JSONObject jo1 = new JSONObject(jo.toString());

            JSONObject ind1 = jo1.getJSONObject("indicators");

            assertTrue(ind1.optBoolean("display_total"));
            assertFalse(ind1.optBoolean("display_unchecked"));
            assertTrue(ind1.optBoolean("display_checked"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmptyJSON() {
        JSONObject jo = new JSONObject();
        log("Empty json object:" + jo.toString());
    }
}
