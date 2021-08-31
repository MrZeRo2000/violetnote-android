package com.romanpulov.violetnote;

import android.content.Context;
import android.util.Log;

import androidx.test.filters.SmallTest;

import org.junit.*;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
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

    private Context getTargetContext() {
        return getInstrumentation().getTargetContext();
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void testLocalBackup() {
        DBBasicNoteHelper.getInstance(getTargetContext()).closeDB();

        DBStorageManager storageManager = DBStorageManager.getInstance(getTargetContext());

        //assertNotNull(storageManager.createLocalBackup());
        assertNotNull(storageManager.getDBBackupManager().createLocalBackup());

        DBBasicNoteHelper.getInstance(getTargetContext()).openDB();
    }

}
