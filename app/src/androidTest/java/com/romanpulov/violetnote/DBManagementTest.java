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
import com.romanpulov.violetnote.model.BasicNoteItemA;

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

    public void createNoteItemTestData() {
        createNotesTestData();

        //note items with zero priority

        //1
        String insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String[] insertArgs = new String[] {"0", "3", "1", "Note item 1", "Note item 1 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //2
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "5", "1", "Note item 2", "Note item 2 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //3
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "6", "1", "Note item 3", "Note item 3 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //note items with high priority
        //4
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "1", "1", "Note item 11", "Note item 11 value", "0", "1"};
        mDB.execSQL(insertSQL, insertArgs);

        //5
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "2", "1", "Note item 12", "Note item 12 value", "0", "1"};
        mDB.execSQL(insertSQL, insertArgs);

        //note items with low priority
        //6
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "1", "1", "Note item 21", "Note item 11 value", "0", "-1"};
        mDB.execSQL(insertSQL, insertArgs);

        //7
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "2", "1", "Note item 22", "Note item 12 value", "0", "-1"};
        mDB.execSQL(insertSQL, insertArgs);


        //note items with zero priority in another note item
        //8
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "1", "2", "Note item 201", "Note item 201 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //9
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "3", "2", "Note item 202", "Note item 202 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //10
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "2", "2", "Note item 202", "Note item 202 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);
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

    public void testNoteItemMove() {
        createNoteItemTestData();

        BasicNoteItemA[] items = new BasicNoteItemA[10];
        DBManagementProvider[] providers = new DBManagementProvider[10];

        for (int i = 0; i < items.length; i++) {
            items[i] = mDBNoteManager.getNoteItem(i + 1);
            providers[i] = items[i].getDBManagementProvider();
        }
    }

    public void disable_testNoteMove() {
        createNotesTestData();

        long note1id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(0)});
        long note2id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(1)});
        long note3id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(2)});

        BasicNoteA note1 = mDBNoteManager.get(note1id);
        BasicNoteA note2 = mDBNoteManager.get(note2id);
        BasicNoteA note3 = mDBNoteManager.get(note3id);

        //note 2 -> prev order = 1
        DBManagementProvider dbManagementProvider = note2.getDBManagementProvider();
        long prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(prevOrderId, 1);

        //note 1 -> prev order = 0
        dbManagementProvider = note1.getDBManagementProvider();
        prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(prevOrderId, 0);

        //note 3 -> prev order = 2
        dbManagementProvider = note3.getDBManagementProvider();
        prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(prevOrderId, 2);

        //note 1 move up - no action : 1, 2, 4
        mDBNoteManager.moveUp(note1);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 1);

        //note 3 move up - order exchange : 1, 4, 2
        mDBNoteManager.moveUp(note3);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 2);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 4);

        //note 1 move down - order exchange : 2, 4, 1
        mDBNoteManager.moveDown(note1);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 2);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 4);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 1);

        //note 2 move down - no action : 2, 4, 1
        mDBNoteManager.moveDown(note2);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 2);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 4);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 1);

        //note 2 move top
        mDBNoteManager.moveTop(note2);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 3);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 1);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 2);

        //note 2 move top - no action
        mDBNoteManager.moveTop(note2);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 3);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 1);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 2);

        //note 2 move bottom
        mDBNoteManager.moveBottom(note2);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 2);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 3);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 1);

        //note 2 move bottom - no action
        mDBNoteManager.moveBottom(note2);
        note1 = mDBNoteManager.get(note1id);
        Assert.assertEquals(note1.getOrderId(), 2);
        note2 = mDBNoteManager.get(note2id);
        Assert.assertEquals(note2.getOrderId(), 3);
        note3 = mDBNoteManager.get(note3id);
        Assert.assertEquals(note3.getOrderId(), 1);



        clearNotesTestData();
    }

}
