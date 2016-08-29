package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;

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

    public void test1() {
        log("Data Management test message");
        assertEquals(1, 1);
    }

    public void testMovePrev() {
        DBNoteManager noteManager = new DBNoteManager(getContext());
        List<BasicNoteA> noteList = noteManager.queryNotes();

        // generator should be ran first
        assertTrue(noteList.size() >= DataGeneratorTest.MAX_NOTES);

        log("Min id =" + DBBasicNoteHelper.getInstance(mContext).getMinId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME));
        log("Max id =" + DBBasicNoteHelper.getInstance(mContext).getMaxId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME));

        log("Get Max order id < 7 = " + DBBasicNoteHelper.getInstance(mContext).getAggregateColumn(
                DBBasicNoteOpenHelper.NOTES_TABLE_NAME,
                DBBasicNoteOpenHelper.ORDER_COLUMN_NAME,
                DBBasicNoteHelper.MAX_AGGREGATE_FUNCTION_NAME,
                DBBasicNoteOpenHelper.ID_COLUMN_NAME + " < ?",
                new String[] {"7"}
        ));

        log ("Get prev order id(5) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 5));
        log ("Get prev order id(1) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 1));
        log ("Get prev order id(0) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0));
        log ("Get prev order id(100) = " + DBBasicNoteHelper.getInstance(mContext).getPrevOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 100));

        log ("Get next order id(5) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 5));
        log ("Get next order id(1) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 1));
        log ("Get next order id(0) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 0));
        log ("Get next order id(100) = " + DBBasicNoteHelper.getInstance(mContext).getNextOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, 100));

        //validate exchange
        BasicNoteA note1 = noteManager.queryById(5);
        BasicNoteA note2 = noteManager.queryById(4);

        DBBasicNoteHelper.getInstance(mContext).exchangeOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note1.getOrderId(), note2.getOrderId());

        note1 = noteManager.queryById(5);
        note2 = noteManager.queryById(4);

        assertEquals(note1.getOrderId(), 4);
        assertEquals(note2.getOrderId(), 5);

        DBBasicNoteHelper.getInstance(mContext).exchangeOrderId(DBBasicNoteOpenHelper.NOTES_TABLE_NAME, note1.getOrderId(), note2.getOrderId());

        note1 = noteManager.queryById(5);
        note2 = noteManager.queryById(4);

        assertEquals(note1.getOrderId(), 5);
        assertEquals(note2.getOrderId(), 4);

        //move top

    }

}
