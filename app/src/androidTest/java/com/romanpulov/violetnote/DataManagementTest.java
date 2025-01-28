package com.romanpulov.violetnote;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.util.Log;

import java.util.List;

import androidx.test.filters.SmallTest;

import com.romanpulov.violetnote.db.dao.BasicCommonNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteItemDAO;
import org.junit.*;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.tabledef.DBCommonDef;
import com.romanpulov.violetnote.db.tabledef.NoteItemsTableDef;
import com.romanpulov.violetnote.db.tabledef.NotesTableDef;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by rpulov on 28.08.2016.
 */
@SmallTest
public class DataManagementTest extends DBBaseTest {
    private final static String TAG = "DataManagementTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    private Context getTargetContext() {
        return getInstrumentation().getTargetContext();
    }

    @Override
    void prepareDatabase() {
        (new DataGenerator()).generateData();
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

        BasicNoteGroupDAO basicNoteGroupDAO = new BasicNoteGroupDAO(getTargetContext());
        BasicNoteDAO basicNoteDAO = new BasicNoteDAO(getTargetContext());

        BasicNoteGroupA group = basicNoteGroupDAO.getById(2);

        List<BasicNoteA> noteList = basicNoteDAO.getTotalsByGroup(group);

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
        BasicNoteA note1 = basicNoteDAO.getById(5);
        BasicNoteA note2 = basicNoteDAO.getById(4);
        assert note1 != null;
        long order1 = note1.getOrderId();
        assert note2 != null;
        long order2 = note2.getOrderId();

        mDBHelper.exchangeOrderId(NotesTableDef.TABLE_NAME, 0, note1.getOrderId(), note2.getOrderId());

        note1 = basicNoteDAO.getById(5);
        note2 = basicNoteDAO.getById(4);

        assert note1 != null;
        assertEquals(note1.getOrderId(), order2);
        assert note2 != null;
        assertEquals(note2.getOrderId(), order1);

        mDBHelper.exchangeOrderId(NotesTableDef.TABLE_NAME, 0, note1.getOrderId(), note2.getOrderId());

        note1 = basicNoteDAO.getById(5);
        note2 = basicNoteDAO.getById(4);

        assert note1 != null;
        assertEquals(note1.getOrderId(), order1);
        assert note2 != null;
        assertEquals(note2.getOrderId(), order2);

        BasicCommonNoteDAO basicCommonNoteDAO = new BasicCommonNoteDAO(getTargetContext());

        //move top
        note1 = basicNoteDAO.getById(5);
        assert note1 != null;
        basicCommonNoteDAO.moveTop(note1);

        note1 = basicNoteDAO.getById(5);
        assert note1 != null;
        assertEquals(1, note1.getOrderId());

        //move bottom
        basicCommonNoteDAO.moveBottom(note1);
        note1 = basicNoteDAO.getById(5);
        assert note1 != null;
        assertEquals(noteList.size(), note1.getOrderId());

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

    @Test
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

        BasicNoteGroupDAO basicNoteGroupDAO = new BasicNoteGroupDAO(getTargetContext());
        BasicNoteDAO basicNoteDAO = new BasicNoteDAO(getTargetContext());

        long startTime = System.nanoTime();

        BasicNoteGroupA group = basicNoteGroupDAO.getById(2);

        List<BasicNoteA> noteList = basicNoteDAO.getTotalsByGroup(group);
        long endTime = System.nanoTime();

        log ("testCheckQueryTotalsRaw time: " + (endTime - startTime) + ", size = " + noteList.size());
        log ("testCheckQueryTotalsRaw List: " + noteList);
    }

    public void internalTestPriceParams() {
        log("testPriceParams");

        long priceNoteParamTypeId = DBBasicNoteHelper.getInstance(getTargetContext()).getDBDictionaryCache().getPriceNoteParamTypeId();

        BasicNoteDAO basicNoteDAO = new BasicNoteDAO(getTargetContext());
        BasicNoteItemDAO basicNoteItemDAO = new BasicNoteItemDAO(getTargetContext());

        BasicNoteA note = basicNoteDAO.getById(2);
        basicNoteItemDAO.fillNoteDataItemsWithSummary(note);

        assert note != null;
        BasicNoteItemA noteItem = note.getItems().get(4);
        assertNotEquals(0, noteItem.getParamLong(priceNoteParamTypeId));

        /*
        // not actual tests, to be deleted
        LongSparseArray<Long> longItemsParams = mDBNoteManager.queryNoteDataItemLongParams(noteItem);
        Long priceParam = longItemsParams.get(priceNoteParamTypeId);
        assertEquals((Long)noteItem.getParamLong(priceNoteParamTypeId), priceParam);

        noteItem.setValueWithParams(priceNoteParamTypeId, "new value");
        mDBNoteManager.updateNoteItemNameValue(noteItem);
        longItemsParams = mDBNoteManager.queryNoteDataItemLongParams(noteItem);
        priceParam = longItemsParams.get(priceNoteParamTypeId);
        assertNull(priceParam);
        */
    }

}
