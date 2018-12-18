package com.romanpulov.violetnote;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;
import java.util.ArrayList;
import java.util.List;

import android.support.test.filters.SmallTest;
import android.util.LongSparseArray;

import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsTableDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by rpulov on 28.08.2016.
 */
@SmallTest
public class DataManagementTest {
    private final static String TAG = "DataManagementTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    private final DataGenerator mDataGenerator = new DataGenerator();

    private DBBasicNoteHelper mDBHelper;
    private SQLiteDatabase mDB;
    private DBNoteManager mDBNoteManager;

    private void initDB() {
        mDBHelper = DBBasicNoteHelper.getInstance(getTargetContext());
        mDBHelper.closeDB();

        mDataGenerator.generateData();

        mDBHelper.openDB();
        mDB = mDBHelper.getDB();
        mDBNoteManager = new DBNoteManager(getTargetContext());
    }


    @Test
    public void testMain() {
        synchronized (DBLock.instance) {
            initDB();
            internalTestCheckCountQuery();
            internalTestMovePrev();
            internalTestCheckQueryTotalsRaw();
            internalTestPriceParams();
        }
    }

    public void internalTestMovePrev() {
        log("testMovePrev");

        List<BasicNoteA> noteList = mDBNoteManager.queryNotes();

        // generator should be ran first
        assertTrue(noteList.size() >= DataGenerator.MAX_NOTES);

        log("Min id =" + mDBHelper.getMinId(NotesTableDef.TABLE_NAME, 0));
        log("Max id =" + mDBHelper.getMaxId(NotesTableDef.TABLE_NAME, 0));

        log("Get Max order id < 7 = " + mDBHelper.getAggregateColumn(
                NotesTableDef.TABLE_NAME,
                DBCommonDef.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                DBCommonDef.ID_COLUMN_NAME + " < ?",
                new String[] {"7"}
        ));

        log ("Get prev order id(5) = " + mDBHelper.getPrevOrderId(NotesTableDef.TABLE_NAME, 0, 5));
        log ("Get prev order id(1) = " + mDBHelper.getPrevOrderId(NotesTableDef.TABLE_NAME, 0, 1));
        log ("Get prev order id(0) = " + mDBHelper.getPrevOrderId(NotesTableDef.TABLE_NAME, 0, 0));
        log ("Get prev order id(100) = " + mDBHelper.getPrevOrderId(NotesTableDef.TABLE_NAME, 0, 100));

        log ("Get next order id(5) = " + mDBHelper.getNextOrderId(NotesTableDef.TABLE_NAME, 0, 5));
        log ("Get next order id(1) = " + mDBHelper.getNextOrderId(NotesTableDef.TABLE_NAME, 0, 1));
        log ("Get next order id(0) = " + mDBHelper.getNextOrderId(NotesTableDef.TABLE_NAME, 0, 0));
        log ("Get next order id(100) = " + mDBHelper.getNextOrderId(NotesTableDef.TABLE_NAME, 0, 100));

        //validate exchange
        BasicNoteA note1 = mDBNoteManager.queryById(5);
        BasicNoteA note2 = mDBNoteManager.queryById(4);
        long order1 = note1.getOrderId();
        long order2 = note2.getOrderId();

        mDBHelper.exchangeOrderId(NotesTableDef.TABLE_NAME, 0, note1.getOrderId(), note2.getOrderId());

        note1 = mDBNoteManager.queryById(5);
        note2 = mDBNoteManager.queryById(4);

        assertEquals(note1.getOrderId(), order2);
        assertEquals(note2.getOrderId(), order1);

        mDBHelper.exchangeOrderId(NotesTableDef.TABLE_NAME, 0, note1.getOrderId(), note2.getOrderId());

        note1 = mDBNoteManager.queryById(5);
        note2 = mDBNoteManager.queryById(4);

        assertEquals(note1.getOrderId(), order1);
        assertEquals(note2.getOrderId(), order2);

        //move top
        note1 = mDBNoteManager.queryById(5);
        mDBNoteManager.moveTop(note1);

        note1 = mDBNoteManager.queryById(5);
        assertEquals(note1.getOrderId(), 1);

        //move bottom
        mDBNoteManager.moveBottom(note1);
        note1 = mDBNoteManager.queryById(5);
        assertEquals(note1.getOrderId(), noteList.size());

        //order id
        long orderId = mDBHelper.getOrderId(NotesTableDef.TABLE_NAME, note1.getId());
        assertEquals(orderId, note1.getOrderId());
    }


    public void internalTestCheckCountQuery() {
        log("testCheckCountQuery");

        long startTime = System.nanoTime();

        Cursor c = null;
        try {
            c = mDB.rawQuery("SELECT COUNT(_id) AS count_total, SUM(checked) AS count_checked FROM note_items WHERE note_id = ?", new String[]{"2"});
            c.moveToFirst();
            if (!c.isAfterLast()) {
                int itemCount = c.getInt(0);
                int checkedItemCount = c.getInt(1);
                log("itemCount = " + itemCount + ", checkedItemCount = " + checkedItemCount);
            }
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        long endTime = System.nanoTime();

        log ("testCheckCountQuery time: " + (endTime - startTime));
    }

    public void testCheckCount() {
        DBBasicNoteHelper dbHelper = DBBasicNoteHelper.getInstance(getTargetContext());
        SQLiteDatabase db = dbHelper.getDB();

        long startTime = System.nanoTime();

        Cursor c = null;
        try {
            c = db.query(NoteItemsTableDef.TABLE_NAME, new String[]{"checked"},
                    DBCommonDef.NOTE_ID_COLUMN_NAME + " = ?", new String[]{"2"}, null, null, null);

            int itemCount = 0;
            int checkedItemCount = 0;

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                itemCount ++;
                checkedItemCount += c.getInt(0);
            }

            log("itemCount = " + itemCount + ", checkedItemCount = " + checkedItemCount);

        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        long endTime = System.nanoTime();

        log ("testCheckCount time: " + (endTime - startTime));

    }

    public void internalTestCheckQueryTotalsRaw() {
        log("testCheckQueryTotalsRaw");

        long startTime = System.nanoTime();

        List<BasicNoteA> noteList = new ArrayList<>();

        String rawSQL =
        "SELECT " +
        "n._id, " +
                "n.last_modified," +
                "n.order_id," +
                "n.group_id," +
                "n.note_type," +
                "n.title," +
                "n.is_encrypted," +
                "n.encrypted_string," +
                "(SELECT COUNT(ni._id) FROM note_items ni WHERE ni.note_id = n._id) AS count_total, " +
                "(SELECT SUM(ni.checked) FROM note_items ni WHERE ni.note_id = n._id) AS count_checked " +
        "FROM notes n " +
        "ORDER BY n.order_id";

        Cursor c = null;
        DateTimeFormatter dtf = new DateTimeFormatter(getTargetContext());
        try {
            c = mDB.rawQuery(rawSQL, null);

            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                BasicNoteA newItem = DBNoteManager.noteFromCursorWithTotals(c, dtf);

                noteList.add(newItem);
            }
        } finally {
            if ((c !=null) && !c.isClosed())
                c.close();
        }

        long endTime = System.nanoTime();

        log ("testCheckQueryTotalsRaw time: " + (endTime - startTime) + ", size = " + noteList.size());
        log ("testCheckQueryTotalsRaw List: " + noteList.toString());
    }

    public void internalTestPriceParams() {
        log("testPriceParams");
        BasicNoteA note = mDBNoteManager.queryById(2);
        mDBNoteManager.queryNoteDataItems(note);

        BasicNoteItemA noteItem = note.getItems().get(4);
        assertNotEquals(0, noteItem.getParamPrice());

        LongSparseArray<Long> longItemsParams = mDBNoteManager.queryNoteDataItemLongParams(noteItem);
        long priceNoteParamTypeId = DBBasicNoteHelper.getInstance(getTargetContext()).getDBDictionaryCache().getPriceNoteParamTypeId();
        Long priceParam = longItemsParams.get(priceNoteParamTypeId);
        assertEquals((Long)noteItem.getParamPrice(), priceParam);

        noteItem.setValueWithParams("new value");
        mDBNoteManager.updateNoteItemNameValue(noteItem);
        longItemsParams = mDBNoteManager.queryNoteDataItemLongParams(noteItem);
        priceParam = longItemsParams.get(priceNoteParamTypeId);
        assertNull(priceParam);

    }

}
