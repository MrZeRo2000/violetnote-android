package com.romanpulov.violetnote;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;

/**
 * Base class for DB tests
 */
abstract class DBBaseTest {
    DBBasicNoteHelper mDBHelper;
    SQLiteDatabase mDB;
    Context mContext;

    BasicNoteDAO mBasicNoteDAO;
    BasicNoteItemDAO mBasicNoteItemDAO;
    BasicNoteGroupDAO mBasicNoteGroupDAO;
    BasicCommonNoteDAO mBasicCommonNoteDAO;

    private Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

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

        mBasicNoteDAO = new BasicNoteDAO(mContext);
        mBasicNoteItemDAO  = new BasicNoteItemDAO(mContext);
        mBasicNoteGroupDAO = new BasicNoteGroupDAO(mContext);
        mBasicCommonNoteDAO = new BasicCommonNoteDAO(mContext);
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
