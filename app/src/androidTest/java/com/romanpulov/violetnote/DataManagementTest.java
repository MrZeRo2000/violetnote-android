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
    }

}
