package com.romanpulov.violetnote;

import android.util.Log;

import androidx.test.filters.SmallTest;
import org.junit.*;
import static androidx.test.platform.app.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;


/**
 * Created by romanpulov on 29.09.2016.
 */
@Ignore
@SmallTest
public class DBBackupTest {
    private final static String TAG = "DBBackupTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void testLocalBackup() {
        DBBasicNoteHelper.getInstance(getTargetContext()).closeDB();

        DBStorageManager storageManager = new DBStorageManager(getTargetContext());

        //assertNotNull(storageManager.createLocalBackup());
        assertNotNull(storageManager.createRollingLocalBackup());

        DBBasicNoteHelper.getInstance(getTargetContext()).openDB();
    }

}
