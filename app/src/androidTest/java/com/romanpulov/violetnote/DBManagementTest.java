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

    public DBManagementTest() {
        super(Application.class);
    }

    private void initDB() {
        mDBHelper = DBBasicNoteHelper.getInstance(getContext());
        mDBHelper.closeDB();

        getContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        mDBHelper.openDB();
        mDB = mDBHelper.getDB();
        mDBNoteManager = new DBNoteManager(getContext());
    }

    private List<String> mTestNoteNames = new ArrayList<>();
    {
        mTestNoteNames.add("Instrumentation test note 1");
        mTestNoteNames.add("Instrumentation test note 2");
        mTestNoteNames.add("Instrumentation test note 3");
    }

    private void createNotesTestData() {
        initDB();

        String insertNotesSql = "insert into " + DBBasicNoteOpenHelper.NOTES_TABLE_NAME + " (last_modified, order_id, note_type, title, is_encrypted) VALUES (?, ?, ?, ?, ?)";

        String[] insertNotesArgs = new String[] {"0", "1", "0", mTestNoteNames.get(0), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        insertNotesArgs = new String[] {"0", "2", "0", mTestNoteNames.get(1), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        insertNotesArgs = new String[] {"0", "4", "0", mTestNoteNames.get(2), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);
    }

    private void createNoteItemTestData() {
        createNotesTestData();

        //note items with zero priority

        //0
        String insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String[] insertArgs = new String[] {"0", "3", "1", "Note item 1", "Note item 1 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //1
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "5", "1", "Note item 2", "Note item 2 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //2
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "6", "1", "Note item 3", "Note item 3 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //note items with high priority
        //3
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "1", "1", "Note item 11", "Note item 11 value", "0", "1"};
        mDB.execSQL(insertSQL, insertArgs);

        //4
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "2", "1", "Note item 12", "Note item 12 value", "0", "1"};
        mDB.execSQL(insertSQL, insertArgs);

        //note items with low priority
        //5
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "1", "1", "Note item 21", "Note item 11 value", "0", "-1"};
        mDB.execSQL(insertSQL, insertArgs);

        //6
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "2", "1", "Note item 22", "Note item 12 value", "0", "-1"};
        mDB.execSQL(insertSQL, insertArgs);


        //note items with zero priority in another note item
        //7
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "1", "2", "Note item 201", "Note item 201 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //8
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "3", "2", "Note item 202", "Note item 202 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);

        //9
        insertSQL = "insert into " + DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME +
                "(last_modified, order_id, note_id, name, value, checked, priority) VALUES (?, ?, ?, ?, ?, ?, ?)";
        insertArgs = new String[] {"0", "2", "2", "Note item 202", "Note item 202 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);
    }

    public void testMove() {
        internalTestPriorityMove();
        internalTestNoteMove();
        internalTestNoteItemMove();
    }

    private BasicNoteItemA[] items = new BasicNoteItemA[10];
    private DBManagementProvider[] providers = new DBManagementProvider[10];

    private void loadNoteItems() {
        for (int i = 0; i < items.length; i++) {
            items[i] = mDBNoteManager.getNoteItem(i + 1);
            providers[i] = items[i].getDBManagementProvider();
        }
    }

    private void internalTestPriorityMove() {
        createNoteItemTestData();

        loadNoteItems();

        DBManagementProvider provider = providers[3];
        long maxOrderId = mDBHelper.getMaxOrderId(provider.getTableName(), provider.getOrderIdSelection(), provider.getOrderIdSelectionArgs());
        Assert.assertEquals(2, maxOrderId);

        provider = providers[0];
        maxOrderId = mDBHelper.getMaxOrderId(provider.getTableName(), provider.getOrderIdSelection(), provider.getOrderIdSelectionArgs());
        Assert.assertEquals(6, maxOrderId);

        provider = providers[2];
        Assert.assertEquals(6, items[2].getOrderId());

        mDBNoteManager.priorityUp(items[0]);
        loadNoteItems();
        Assert.assertEquals(1, items[0].getPriority());
        assertEquals(3, items[0].getOrderId());

        Assert.assertEquals(6, items[2].getOrderId());

        provider = providers[1];
        maxOrderId = mDBHelper.getMaxOrderId(provider.getTableName(), provider.getOrderIdSelection(), provider.getOrderIdSelectionArgs());
        Assert.assertEquals(6, maxOrderId);

        mDBNoteManager.priorityDown(items[0]);
        loadNoteItems();
        Assert.assertEquals(0, items[0].getPriority());

        Assert.assertEquals(6, items[2].getOrderId());

        assertEquals(7, items[0].getOrderId());
    }

    public void internalTestNoteItemMove() {
        createNoteItemTestData();

        loadNoteItems();

        //note 1 prev order
        DBManagementProvider provider = providers[1];
        long prevOrderId = mDBHelper.getPrevOrderId(provider.getTableName(), provider.getPrevOrderSelection(), provider.getOrderSelectionArgs());
        Assert.assertEquals(3, prevOrderId);

        //note 4 prev order (high priority)
        provider = providers[4];
        prevOrderId = mDBHelper.getPrevOrderId(provider.getTableName(), provider.getPrevOrderSelection(), provider.getOrderSelectionArgs());
        Assert.assertEquals(1, prevOrderId);

        //note 2 move top
        mDBNoteManager.moveTop(items[2]);
        loadNoteItems();
        Assert.assertEquals(3, items[2].getOrderId());

        //note 2 move top - remains the same
        mDBNoteManager.moveTop(items[2]);
        loadNoteItems();
        Assert.assertEquals(3, items[2].getOrderId());

        //note 2 move bottom
        mDBNoteManager.moveBottom(items[2]);
        loadNoteItems();
        Assert.assertEquals(6, items[2].getOrderId());

        //high prio item move bottom
        mDBNoteManager.moveBottom(items[3]);
        loadNoteItems();
        Assert.assertEquals(2, items[3].getOrderId());
        Assert.assertEquals(1, items[4].getOrderId());

        //other note items remain the same
        Assert.assertEquals(2, items[9].getOrderId());
        Assert.assertEquals(3, items[8].getOrderId());
    }

    private void internalTestNoteMove() {
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
    }

}
