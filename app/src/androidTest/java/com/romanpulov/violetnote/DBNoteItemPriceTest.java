package com.romanpulov.violetnote;

import android.content.Context;

import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SmallTest
public final class DBNoteItemPriceTest extends DBBaseTest {
    private Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Override
    void prepareDatabase() {
        deleteDatabase();
    }

    @Test
    public void testMain() {
        synchronized (DBLock.instance) {
            initDB();

            testNoteItemPrice();
        }
    }

    private void testNoteItemPrice() {
        long priceNoteParamTypeId = DBBasicNoteHelper.getInstance(getTargetContext()).getDBDictionaryCache().getPriceNoteParamTypeId();

        //create new note
        BasicNoteA note = BasicNoteA.newEditInstance(BasicNoteGroupA.DEFAULT_NOTE_GROUP_ID, BasicNoteA.NOTE_TYPE_NAMED, "New note", false, null);
        long result = mBasicNoteDAO.insert(note);
        assertNotEquals(-1, result);
        note.setId(result);

        BasicNoteItemA noPriceNoteItem = BasicNoteItemA.newCheckedEditInstance(note.getId(), priceNoteParamTypeId, "Value without param");
        result = mBasicNoteItemDAO.insertWithNote(note, noPriceNoteItem);
        assertNotEquals(-1, result);
        noPriceNoteItem.setId(result);

        BasicNoteItemA hasPriceNoteItem = BasicNoteItemA.newCheckedEditInstance(note.getId(), priceNoteParamTypeId, "Value with param 12.77");
        result = mBasicNoteItemDAO.insertWithNote(note, hasPriceNoteItem);
        assertNotEquals(-1, result);
        hasPriceNoteItem.setId(result);

        //total price from value
        mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(1277L, note.getSummary().getParams().get(priceNoteParamTypeId).longValue());

        //added price param to first item
        noPriceNoteItem.setParamLong(priceNoteParamTypeId, 200L);
        result = mBasicNoteItemDAO.updateNameValue(noPriceNoteItem);
        assertNotEquals(0, result);

        //check total increased
        mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(1477L, note.getSummary().getParams().get(priceNoteParamTypeId).longValue());

        //changed price param for first item
        noPriceNoteItem.setParamLong(priceNoteParamTypeId, 100L);
        result = mBasicNoteItemDAO.updateNameValue(noPriceNoteItem);
        assertNotEquals(0, result);

        //check total item decreased
        mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(1377L, note.getSummary().getParams().get(priceNoteParamTypeId).longValue());

        //removed price param
        noPriceNoteItem.setParamLong(priceNoteParamTypeId, 0L);
        result = mBasicNoteItemDAO.updateNameValue(noPriceNoteItem);
        assertNotEquals(0, result);

        //total price back to initial value
        mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(1277L, note.getSummary().getParams().get(priceNoteParamTypeId).longValue());
    }
}
