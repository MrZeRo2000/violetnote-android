package com.romanpulov.violetnote;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBManagementProvider;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 09.08.2017.
 */

public class DBManagementTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DBManagementTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    private DBBasicNoteHelper mDBHelper;
    private SQLiteDatabase mDB;
    private DBNoteManager mDBNoteManager;

    private List<String> mTestNoteNames = new ArrayList<>();
    {
        mTestNoteNames.add("Instrumentation test note 1");
        mTestNoteNames.add("Instrumentation test note 2");
        mTestNoteNames.add("Instrumentation test note 3");
    }


    public DBManagementTest() {
        super(Application.class);
    }

    private void initDB() {
        if (mDBHelper == null) {
            getContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

            mDBHelper = DBBasicNoteHelper.getInstance(getContext());
            mDB = mDBHelper.getDB();
            mDBNoteManager = new DBNoteManager(getContext());
        }
    }

    public void createNotesTestData() {
        initDB();

        String insertNotesSql = "insert into " + DBBasicNoteOpenHelper.NOTES_TABLE_NAME + " (last_modified, order_id, note_type, title, is_encrypted) VALUES (?, ?, ?, ?, ?)";

        String[] insertNotesArgs = new String[] {"0", "1", "0", mTestNoteNames.get(0), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        insertNotesArgs = new String[] {"0", "2", "0", mTestNoteNames.get(1), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        insertNotesArgs = new String[] {"0", "4", "0", mTestNoteNames.get(2), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

    }

    public void clearNotesTestData() {
        initDB();

        String[] deleteNoteArgs = new String [1];
        String deleteNoteSQL = "DELETE FROM " + DBBasicNoteOpenHelper.NOTES_TABLE_NAME + " WHERE title = ?";

        for (String s: mTestNoteNames) {
            log("deleting note " + s);
            deleteNoteArgs[0] = s;
            mDB.execSQL(deleteNoteSQL, deleteNoteArgs);
        }
    }

    public void testMovePrev() {
        createNotesTestData();

        long note1id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(0)});
        long note2id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(1)});
        long note3id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(2)});

        BasicNoteA note1 = mDBNoteManager.get(note1id);
        BasicNoteA note2 = mDBNoteManager.get(note2id);
        BasicNoteA note3 = mDBNoteManager.get(note3id);

        //note 2 -> prev order = 1
        DBManagementProvider dbManagementProvider = note2.getDBManagementProvider();
        long prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(prevOrderId, 1);

        //note 1 -> prev order = 0
        dbManagementProvider = note1.getDBManagementProvider();
        prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(prevOrderId, 0);

        //note 3 -> prev order = 2
        dbManagementProvider = note3.getDBManagementProvider();
        prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(prevOrderId, 2);

        //note 1 move up - no action
        dbManagementProvider = note1.getDBManagementProvider();
        mDBNoteManager.moveUp(dbManagementProvider.getTableName(), note1);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 1);

        //note 3 move up - order exchange
        dbManagementProvider = note3.getDBManagementProvider();
        mDBNoteManager.moveUp(dbManagementProvider.getTableName(), note3);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 2);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 4);



        clearNotesTestData();
    }

}
