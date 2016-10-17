package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by rpulov on 17.10.2016.
 */

public class JSONTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DBJSONTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public JSONTest() {
        super(Application.class);
    }

    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

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
