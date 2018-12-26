package com.romanpulov.violetnote;

import android.support.test.filters.SmallTest;

import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHEventTypeA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.NoteGroupA;

import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

@SmallTest
public final class DBHManagementTest extends DBBaseTest {
    @Override
    void prepareDatabase() {
        deleteDatabase();
    }

    @Test
    public void testMain() {
        synchronized (DBLock.instance) {
            initDB();

            //test events only, clear afterwards
            testEventTypes();
            testEvents();

            testHNoteItems();
            testHNoteCOItems();
        }
    }

    private void testHNoteItems() {

        BasicNoteA note = BasicNoteA.newEditInstance(NoteGroupA.DEFAULT_NOTE_GROUP_ID, BasicNoteA.NOTE_TYPE_NAMED, "New note", false, null);
        long result = mDBNoteManager.mBasicNoteDAO.insert(note);
        assertNotEquals(-1, result);
        note.setId(result);

        BasicNoteItemA noteItem1 = BasicNoteItemA.newNamedEditInstance("New name", "New Value");
        result = mDBNoteManager.insertNoteItem(note, noteItem1);
        assertNotEquals(-1, result);
        noteItem1.setId(result);


        BasicNoteItemA noteItem2 = BasicNoteItemA.newNamedEditInstance("Another name", "Another Value");
        result = mDBNoteManager.insertNoteItem(note, noteItem2);
        assertNotEquals(-1, result);
        noteItem2.setId(result);

        //new events after create
        List<BasicHEventA> testEvents = mDBHManager.basicHEventDAO.getAll();
        assertEquals(2, testEvents.size());
        List<BasicHNoteItemA> testEventItems = mDBHManager.basicHNoteItemDAO.getAll();
        assertEquals(2, testEventItems.size());

        //new event after update
        noteItem2.setValue("Another value = new");
        mDBNoteManager.updateNoteItemNameValue(noteItem2);
        testEvents = mDBHManager.basicHEventDAO.getAll();
        assertEquals(3, testEvents.size());

        //delete note item and events should also be deleted
        result = mDBNoteManager.deleteNoteItem(noteItem2);
        assertNotEquals(0, result);

        testEvents = mDBHManager.basicHEventDAO.getAll();
        assertEquals(1, testEvents.size());

        noteItem1.setName("Changed name");
        noteItem1.setValue("Changed value");
        mDBNoteManager.updateNoteItemNameValue(noteItem1);

        testEvents = mDBHManager.basicHEventDAO.getAll();
        assertEquals(2, testEvents.size());

        //one time action to get backup for tests
        //backupDB();

        //delete the note
        result = mDBNoteManager.mBasicNoteDAO.delete(note);
        assertNotEquals(0, result);

        List<BasicNoteA> notes =  mDBNoteManager.mBasicNoteDAO.getTotals();
        assertEquals(0, notes.size());
    }

    private void testHNoteCOItems() {
        BasicNoteA note = BasicNoteA.newEditInstance(NoteGroupA.DEFAULT_NOTE_GROUP_ID, BasicNoteA.NOTE_TYPE_CHECKED, "Checked note", false, null);
        long result = mDBNoteManager.mBasicNoteDAO.insert(note);
        assertNotEquals(-1, result);
        note.setId(result);

        List<BasicNoteA> notes =  mDBNoteManager.mBasicNoteDAO.getTotals();
        assertEquals(1, notes.size());

        long priceNoteParamTypeId = mDBHelper.getDBDictionaryCache().getPriceNoteParamTypeId();

        BasicNoteItemA noteItem1 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Data without value");
        result = mDBNoteManager.insertNoteItem(note, noteItem1);
        assertNotEquals(-1, result);
        noteItem1.setId(result);

        mDBNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(1, note.getItemCount());

        BasicNoteItemA noteItem2 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Data with value 23.55");
        result = mDBNoteManager.insertNoteItem(note, noteItem2);
        assertNotEquals(-1, result);
        noteItem2.setId(result);

        assertEquals(2355L, noteItem2.getNoteItemParams().getLong(priceNoteParamTypeId));
        mDBNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(2, note.getItemCount());
        assertEquals(2355L, note.getItems().get(1).getNoteItemParams().getLong(priceNoteParamTypeId));

        BasicNoteItemA noteItem3 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Another value 34");
        result = mDBNoteManager.insertNoteItem(note, noteItem3);
        assertNotEquals(-1, result);
        noteItem3.setId(result);

        mDBNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(3, note.getItemCount());
        assertEquals(3400L, note.getItems().get(2).getNoteItemParams().getLong(priceNoteParamTypeId));
        assertEquals(5755L, note.getTotalPrice());

        //check items
        result = mDBNoteManager.checkNoteItem(noteItem1);
        assertNotEquals(-1, result);
        result = mDBNoteManager.checkNoteItem(noteItem2);
        assertNotEquals(-1, result);

        //checkout items
        mDBNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        mDBNoteManager.checkOut(note);

        //here we should have some history
        List<BasicHNoteCOItemA> hNoteCOItems =  mDBHManager.basicHNoteCOItemDAO.getByNoteId(note.getId());
        assertEquals(2, hNoteCOItems.size());

        //check remaining items after checkout
        mDBNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(1, note.getItemCount());

        //delete note with items
        mDBNoteManager.mBasicNoteDAO.delete(note);
        mDBNoteManager.mBasicNoteItemDAO.fillNoteDataItemsWithSummary(note);
        assertEquals(0, note.getItemCount());

    }

    private void testEventTypes() {
        List<BasicHEventTypeA> hEventTypes = mDBHManager.basicHEventTypeDAO.getAll();
        Assert.assertEquals(2, hEventTypes.size());

        Assert.assertEquals(1, mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId());
        Assert.assertEquals(2, mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId());
    }

    private void testEvents() {
        assertNotEquals(-1, mDBHManager.basicHEventDAO.insert(BasicHEventA.fromEventType(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId())));
        sleep(500);
        assertNotEquals(-1, mDBHManager.basicHEventDAO.insert(BasicHEventA.fromEventType(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId())));
        sleep(600);
        assertNotEquals(-1, mDBHManager.basicHEventDAO.insert(BasicHEventA.fromEventTypeWithSummary(mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId(), "Some summary")));

        List<BasicHEventA> testEvents = mDBHManager.basicHEventDAO.getAll();
        assertEquals(3, testEvents.size());

        testEvents = mDBHManager.basicHEventDAO.getByType(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId());
        assertEquals(2, testEvents.size());
        testEvents = mDBHManager.basicHEventDAO.getByType(mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId());
        assertEquals(1, testEvents.size());
        assertEquals("Some summary", testEvents.get(0).getEventSummary());

        testEvents = mDBHManager.basicHEventDAO.getAll();
        for (BasicHEventA hEvent : testEvents) {
            long result = mDBHManager.basicHEventDAO.delete(hEvent);
            assertNotEquals(0, result);
        }

        testEvents = mDBHManager.basicHEventDAO.getAll();
        assertEquals(0, testEvents.size());
    }

}
