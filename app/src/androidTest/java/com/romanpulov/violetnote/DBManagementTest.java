package com.romanpulov.violetnote;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import android.support.test.filters.SmallTest;
import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBManagementProvider;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by rpulov on 09.08.2017.
 */
@SmallTest
public class DBManagementTest {
    private final static String TAG = "DBManagementTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    private DBBasicNoteHelper mDBHelper;
    private SQLiteDatabase mDB;
    private DBNoteManager mDBNoteManager;

    private void initDB() {
        mDBHelper = DBBasicNoteHelper.getInstance(getTargetContext());
        mDBHelper.closeDB();

        getTargetContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        mDBHelper.openDB();
        mDB = mDBHelper.getDB();
        mDBNoteManager = new DBNoteManager(getTargetContext());
    }

    /**
     * Main test procedure
     */
    @Test
    public void testMain() {
        internalTestPriorityMove();
        internalTestNoteMove();
        internalTestNoteItemMove();
        internalTestRelatedNoteListAndMovement();
    }

    private final List<String> mTestNoteNames = new ArrayList<>();

    {
        for (int i = 1; i < 100; i++)
            mTestNoteNames.add("Instrumentation test note " + String.valueOf(i));
    }

    private void createNotesTestData() {
        initDB();

        int notePos = 0;

        String insertNotesSql = "insert into " + DBBasicNoteOpenHelper.NOTES_TABLE_NAME + " (last_modified, order_id, note_type, title, is_encrypted) VALUES (?, ?, ?, ?, ?)";

        // checked item 1
        String[] insertNotesArgs = new String[] {"0", "1", "0", mTestNoteNames.get(notePos++), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        // checked item 2
        insertNotesArgs = new String[] {"0", "2", "0", mTestNoteNames.get(notePos++), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        // checked item 3
        insertNotesArgs = new String[] {"0", "4", "0", mTestNoteNames.get(notePos++), "0"};
        mDB.execSQL(insertNotesSql, insertNotesArgs);

        // name value item 1
        insertNotesArgs = new String[] {"0", "5", "1", mTestNoteNames.get(notePos), "0"};
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

        //10
        insertArgs = new String[] {"0", "1", "3", "Note item 301", "Note item 301 value", "0", "0"};
        mDB.execSQL(insertSQL, insertArgs);
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

    private String getNotesOrder(long[] noteIdList) {
        String result = "";

        for (long noteId : noteIdList) {
            BasicNoteA note = mDBNoteManager.get(noteId);
            if (result.isEmpty())
                result = String.valueOf(note.getOrderId());
            else
                result += "," + String.valueOf(note.getOrderId());
        }

        return result;
    }

    private BasicNoteA[] getNotes(long[] noteIdList) {
        BasicNoteA[] result = new BasicNoteA[noteIdList.length];

        int idx = 0;
        for (long noteId : noteIdList) {
            result[idx ++] = mDBNoteManager.get(noteId);;
        }

        return result;
    }

    private void queryNoteDataItems(BasicNoteA[] notes) {
        for (BasicNoteA note : notes)
            mDBNoteManager.queryNoteDataItems(note);
    }


    private void internalTestNoteMove() {
        createNotesTestData();

        long note1id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(0)});
        long note2id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(1)});
        long note3id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(2)});
        long note4id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(3)});

        long[] noteIdList = {note1id, note2id, note3id, note4id};

        BasicNoteA[] notes = getNotes(noteIdList);

        //note 2 -> prev order = 1
        DBManagementProvider dbManagementProvider = notes[1].getDBManagementProvider();
        long prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(1, prevOrderId);

        //note 1 -> prev order = 0
        dbManagementProvider = notes[0].getDBManagementProvider();
        prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(0, prevOrderId);

        //note 3 -> prev order = 2
        dbManagementProvider = notes[2].getDBManagementProvider();
        prevOrderId = mDBHelper.getPrevOrderId(dbManagementProvider.getTableName(), dbManagementProvider.getPrevOrderSelection(), dbManagementProvider.getOrderSelectionArgs());
        Assert.assertEquals(2, prevOrderId);

        Assert.assertEquals("1,2,4,5", getNotesOrder(noteIdList)) ;

        //note 1 move up - no action : 1, 2, 4, 5
        mDBNoteManager.moveUp(notes[0]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("1,2,4,5", getNotesOrder(noteIdList)) ;

        //note 3 move up - order exchange : 1, 4, 2, 5
        mDBNoteManager.moveUp(notes[2]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("1,4,2,5", getNotesOrder(noteIdList)) ;

        //note 1 move down - order exchange
        mDBNoteManager.moveDown(notes[1]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("1,5,2,4", getNotesOrder(noteIdList)) ;

        //note 2 move down - no action
        mDBNoteManager.moveDown(notes[1]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("1,5,2,4", getNotesOrder(noteIdList)) ;

        //note 2 move top
        mDBNoteManager.moveTop(notes[1]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("2,1,3,5", getNotesOrder(noteIdList)) ;

        //note 2 move top - no action
        mDBNoteManager.moveTop(notes[1]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("2,1,3,5", getNotesOrder(noteIdList)) ;

        //note 2 move bottom
        mDBNoteManager.moveBottom(notes[1]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("1,5,2,4", getNotesOrder(noteIdList)) ;

        //note 2 move bottom - no action
        mDBNoteManager.moveBottom(notes[1]);
        notes = getNotes(noteIdList);
        Assert.assertEquals("1,5,2,4", getNotesOrder(noteIdList)) ;
    }

    public void internalTestRelatedNoteListAndMovement() {
        createNoteItemTestData();

        long note1id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(0)});
        long note2id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(1)});
        long note3id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(2)});
        long note4id = mDBHelper.getAggregateColumn(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, DBBasicNoteOpenHelper.ID_COLUMN_NAME, "MAX", "title = ?", new String[]{mTestNoteNames.get(3)});

        long[] noteIdList = {note1id, note2id, note3id, note4id};

        BasicNoteA[] notes = getNotes(noteIdList);

        //related notes
        BasicNoteDataA noteData2 = mDBNoteManager.fromNoteData(notes[1]);
        Assert.assertEquals(2, noteData2.getRelatedNoteList().size());

        BasicNoteDataA noteData4 = mDBNoteManager.fromNoteData(notes[3]);
        Assert.assertEquals(0, noteData4.getRelatedNoteList().size());

        //notes priority
        Assert.assertEquals(6, mDBHelper.getNoteMaxOrderId(1, 0));
        Assert.assertEquals(2, mDBHelper.getNoteMaxOrderId(1, 1));
        Assert.assertEquals(2, mDBHelper.getNoteMaxOrderId(1, -1));

        //notes movement
        queryNoteDataItems(notes);

        int note1ItemCount = notes[0].getItemCount();
        int note2ItemCount = notes[1].getItemCount();

        BasicNoteItemA itemToMove = notes[0].getItems().get(6);
        BasicNoteA noteToMoveTo = notes[1];

        mDBNoteManager.moveNoteItemOther(itemToMove, noteToMoveTo);

        queryNoteDataItems(notes);

        Assert.assertEquals(note1ItemCount - 1, notes[0].getItemCount());
        Assert.assertEquals(note2ItemCount + 1, notes[1].getItemCount());

        Assert.assertEquals(-1, notes[1].getItems().get(notes[1].getItemCount()-1).getPriority());
        Assert.assertEquals(1, notes[1].getItems().get(notes[1].getItemCount()-1).getOrderId());
    }
}
