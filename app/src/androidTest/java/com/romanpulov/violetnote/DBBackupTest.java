package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBStorageManager;

/**
 * Created by romanpulov on 29.09.2016.
 */

public class DBBackupTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DBBackupTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public DBBackupTest() {
        super(Application.class);
    }

    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    public void testLocalBackup() {
        DBBasicNoteHelper.getInstance(getContext()).closeDB();

        DBStorageManager storageManager = new DBStorageManager(getContext());
        assertTrue(storageManager.createLocalBackup() != null);

        DBBasicNoteHelper.getInstance(getContext()).openDB();
    }
}
