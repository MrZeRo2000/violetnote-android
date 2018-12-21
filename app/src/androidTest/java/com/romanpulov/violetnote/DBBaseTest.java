package com.romanpulov.violetnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBHManager;
import com.romanpulov.violetnote.db.DBNoteManager;

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

    void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            //do nothing
        }
    }
}
