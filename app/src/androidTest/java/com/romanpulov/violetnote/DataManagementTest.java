package com.romanpulov.violetnote;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.db.DateTimeFormatter;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 28.08.2016.
 */
public class DataManagementTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DataManagementTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public DataManagementTest() {
        super(Application.class);
    }

    public void disable_test1() {
        log("Data Management test message");
        assertEquals(1, 1);
    }

    public void disable_testMovePrev() {
        DBNoteManager noteManager = new DBNoteManager(getContext());
        List<BasicNoteA> noteList = noteManager.queryNotes();

        // generator should be ran first
        assertTrue(noteList.size() >= DataGeneratorTest.MAX_NOTES);

        log("Min id =" + DBBasicNoteHelper.getInstance(mContext).getMinId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0));
        log("Max id =" + DBBasicNoteHelper.getInstance(mContext).getMaxId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0));

        log("Get Max order id < 7 = " + DBBasicNoteHelper.getInstance(mContext).getAggregateColumn(
                DBBasicNoteOpenHelper.NOTES_TABLE_NAME,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                DBBasicNoteOpenHelper.ID_COLUMN_NAME + " < ?",
                new String[] {"7"}
        ));

        log ("Get prev order id(5) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 5));
        log ("Get prev order id(1) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 1));
        log ("Get prev order id(0) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 0));
        log ("Get prev order id(100) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 100));

        log ("Get next order id(5) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 5));
        log ("Get next order id(1) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 1));
        log ("Get next order id(0) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 0));
        log ("Get next order id(100) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, 100));

        //validate exchange
        BasicNoteA note1 = noteManager.queryById(5);
        BasicNoteA note2 = noteManager.queryById(4);

        DBBasicNoteHelper.getInstance(mContext).exchangeOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, note1.getOrderId(), note2.getOrderId());

        note1 = noteManager.queryById(5);
        note2 = noteManager.queryById(4);

        assertEquals(note1.getOrderId(), 4);
        assertEquals(note2.getOrderId(), 5);

        DBBasicNoteHelper.getInstance(mContext).exchangeOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0, note1.getOrderId(), note2.getOrderId());

        note1 = noteManager.queryById(5);
        note2 = noteManager.queryById(4);

        assertEquals(note1.getOrderId(), 5);
        assertEquals(note2.getOrderId(), 4);

        //move top
        note1 = noteManager.queryById(5);
        noteManager.moveTop(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note1);

        note1 = noteManager.queryById(5);
        assertEquals(note1.getOrderId(), 1);

        //move bottom
        noteManager.moveBottom(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note1);
        note1 = noteManager.queryById(5);
        assertEquals(note1.getOrderId(), 10);
    }

    public void testCheckCount() {
        DBBasicNoteHelper dbHelper = DBBasicNoteHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getDB();

        long startTime = System.nanoTime();

        Cursor c = null;
        try {
            c = db.query(DBBasicNoteOpenHelper.NOTE_ITEMS_TABLE_NAME, new String[]{"checked"},
                    DBBasicNoteOpenHelper.NOTE_ID_COLUMN_NAME + " = ?", new String[]{"2"}, null, null, null);

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

    public void testCheckCountQuery() {
        DBBasicNoteHelper dbHelper = DBBasicNoteHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getDB();

        long startTime = System.nanoTime();

        Cursor c = null;
        try {
            c = db.rawQuery("SELECT COUNT(_id) AS count_total, SUM(checked) AS count_checked FROM note_items WHERE note_id = ?", new String[]{"2"});
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



    public void testCheckQueryTotalsRaw() {

        long startTime = System.nanoTime();

        List<BasicNoteA> noteList = new ArrayList<>();

        String rawSQL =
        "SELECT " +
        "n._id, " +
                "n.last_modified," +
                "n.order_id," +
                "n.note_type," +
                "n.title," +
                "n.is_encrypted," +
                "n.encrypted_string," +
                "(SELECT COUNT(ni._id) FROM note_items ni WHERE ni.note_id = n._id) AS count_total, " +
                //"(SELECT COUNT(CASE WHEN ni.checked = 1 THEN 1 END) FROM note_items ni WHERE ni.note_id = n._id) AS count_checked " +
                "(SELECT SUM(ni.checked) FROM note_items ni WHERE ni.note_id = n._id) AS count_checked " +
        "FROM notes n " +
        "ORDER BY n.order_id";

        DBBasicNoteHelper dbHelper = DBBasicNoteHelper.getInstance(getContext());
        SQLiteDatabase db = dbHelper.getDB();

        Cursor c = null;
        DateTimeFormatter dtf = new DateTimeFormatter(getContext());
        try {
            c = db.rawQuery(rawSQL, null);

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

}
