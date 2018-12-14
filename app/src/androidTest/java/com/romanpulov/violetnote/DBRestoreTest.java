package com.romanpulov.violetnote;

import android.util.Log;

import android.support.test.filters.SmallTest;
import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;

/**
 * Created by romanpulov on 07.10.2016.
 */
@Ignore
@SmallTest
public class DBRestoreTest {

    private final static String TAG = "DBRestoreTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void testLocalRestore() {
        DBBasicNoteHelper.getInstance(getTargetContext()).closeDB();

        DBStorageManager storageManager = new DBStorageManager(getTargetContext());

        String dbFileName = storageManager.restoreLocalBackup();
        assertNotNull(dbFileName);

        log(dbFileName);

        DBBasicNoteHelper.getInstance(getTargetContext()).openDB();
    }

}
