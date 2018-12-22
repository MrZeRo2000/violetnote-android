package com.romanpulov.violetnote;

import android.support.test.filters.SmallTest;

import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.NoteGroupA;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@SmallTest
public final class DBNoteItemPriceTest extends DBBaseTest {
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
        //create new note
        BasicNoteA note = BasicNoteA.newEditInstance(NoteGroupA.DEFAULT_NOTE_GROUP_ID, BasicNoteA.NOTE_TYPE_NAMED, "New note", false, null);
        long result = mDBNoteManager.insertNote(note);
        assertNotEquals(-1, result);
        note.setId(result);

        BasicNoteItemA noPriceNoteItem = BasicNoteItemA.newCheckedEditInstance("Value without param");
        result = mDBNoteManager.insertNoteItem(note, noPriceNoteItem);
        assertNotEquals(-1, result);
        noPriceNoteItem.setId(result);

        BasicNoteItemA hasPriceNoteItem = BasicNoteItemA.newCheckedEditInstance("Value with param 12.77");
        result = mDBNoteManager.insertNoteItem(note, hasPriceNoteItem);
        assertNotEquals(-1, result);
        hasPriceNoteItem.setId(result);

        //total price from value
        mDBNoteManager.queryNoteDataItems(note);
        assertEquals(1277L, note.getTotalPrice());

        //added price param to first item
        noPriceNoteItem.setParamPrice(200L);
        result = mDBNoteManager.updateNoteItemNameValue(noPriceNoteItem);
        assertNotEquals(0, result);

        //check total increased
        mDBNoteManager.queryNoteDataItems(note);
        assertEquals(1477L, note.getTotalPrice());

        //changed price param for first item
        noPriceNoteItem.setParamPrice(100L);
        result = mDBNoteManager.updateNoteItemNameValue(noPriceNoteItem);
        assertNotEquals(0, result);

        //check total item decreased
        mDBNoteManager.queryNoteDataItems(note);
        assertEquals(1377L, note.getTotalPrice());

        //removed price param
        noPriceNoteItem.setParamPrice(0L);
        result = mDBNoteManager.updateNoteItemNameValue(noPriceNoteItem);
        assertNotEquals(0, result);

        //total price back to initial value
        mDBNoteManager.queryNoteDataItems(note);
        assertEquals(1277L, note.getTotalPrice());
    }
}
