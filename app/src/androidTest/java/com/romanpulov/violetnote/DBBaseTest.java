package com.romanpulov.violetnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.manager.DBHManager;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.db.DBStorageManager;

import static android.support.test.InstrumentationRegistry.getTargetContext;

/**
 * Base class for DB tests
 */
abstract class DBBaseTest {
    DBBasicNoteHelper mDBHelper;
    SQLiteDatabase mDB;
    DBHManager mDBHManager;
    DBNoteManager mDBNoteManager;
    Context mContext;

    abstract void prepareDatabase();

    void deleteDatabase() {
        mContext.deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);
    }

    void initDB() {
        mContext = getTargetContext();
        mDBHelper = DBBasicNoteHelper.getInstance(mContext);
        mDBHelper.closeDB();

        prepareDatabase();

        mDBHelper.openDB();
        mDB = mDBHelper.getDB();
        mDBNoteManager = new DBNoteManager(mContext);
        mDBHManager = new DBHManager(mContext);
    }

    void backupDB() {
        mDBHelper.closeDB();
        DBStorageManager storageManager = new DBStorageManager(mContext);
        storageManager.createRollingLocalBackup();
        mDBHelper.openDB();
    }

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //do nothing
        }
    }

    void closeDB() {
        mDBHelper.closeDB();
    }
}
