package com.romanpulov.violetnote;

import androidx.test.filters.SmallTest;

import android.content.Context;
import android.util.Log;

import com.romanpulov.violetnote.db.DBDictionaryCache;

import org.junit.*;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

@SmallTest
public class DBDictionaryCacheTest {
    private Context getTargetContext() {
        return getInstrumentation().getTargetContext();
    }

    private final static String TAG = "DBDictionaryCacheTest";

    @Test
    public void testMain() {
        DBDictionaryCache dc = new DBDictionaryCache();
        long id;

        try {
            id = dc.getPriceNoteParamTypeId();
            fail("Returned id from empty cache");
        } catch (Exception e) {
        }

        dc.load(getTargetContext());
        id = dc.getPriceNoteParamTypeId();
        Log.d(TAG, "id=" + id);
        assertNotEquals(0, id);

        dc.invalidate();

        try {
            id = dc.getPriceNoteParamTypeId();
            fail("Returned id from empty cache after invalidating");
        } catch (Exception e) {
        }

    }
}
