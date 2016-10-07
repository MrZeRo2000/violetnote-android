package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBStorageManager;

import static junit.framework.Assert.assertEquals;

/**
 * Created by romanpulov on 07.10.2016.
 */

public class DBRestoreTest extends ApplicationTestCase<Application> {

    private final static String TAG = "DBRestoreTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public DBRestoreTest() {
        super(Application.class);
    }

    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    public void testLocalRestore() {
        DBBasicNoteHelper.getInstance(getContext()).closeDB();

        DBStorageManager storageManager = new DBStorageManager(getContext());

        String dbFileName = storageManager.restoreLocalBackup();
        assertNotNull(dbFileName);

        log(dbFileName);

        DBBasicNoteHelper.getInstance(getContext()).openDB();
    }

}
