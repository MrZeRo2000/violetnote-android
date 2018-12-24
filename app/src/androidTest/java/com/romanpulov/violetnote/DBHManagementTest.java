package com.romanpulov.violetnote;

import android.support.test.filters.SmallTest;

import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHEventTypeA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.NoteGroupA;

import org.junit.*;

import java.util.ArrayList;
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
        long result = mDBNoteManager.insertNote(note);
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
        List<BasicHEventA> testEvents = new ArrayList<>();
        mDBHManager.queryHEvents(testEvents);
        assertEquals(2, testEvents.size());

        //new event after update
        noteItem2.setValue("Another value = new");
        mDBNoteManager.updateNoteItemNameValue(noteItem2);
        mDBHManager.queryHEvents(testEvents);
        assertEquals(3, testEvents.size());

        //delete note item and events should also be deleted
        result = mDBNoteManager.deleteNoteItem(noteItem2);
        assertNotEquals(0, result);

        testEvents = new ArrayList<>();
        mDBHManager.queryHEvents(testEvents);
        assertEquals(1, testEvents.size());

        noteItem1.setName("Changed name");
        noteItem1.setValue("Changed value");
        mDBNoteManager.updateNoteItemNameValue(noteItem1);

        mDBHManager.queryHEvents(testEvents);
        assertEquals(2, testEvents.size());

        //one time action to get backup for tests
        //backupDB();

        //delete the note
        result = mDBNoteManager.deleteNote(note);
        assertNotEquals(0, result);

        ArrayList<BasicNoteA> notes =  mDBNoteManager.queryNotes();
        assertEquals(0, notes.size());
    }

    private void testHNoteCOItems() {
        BasicNoteA note = BasicNoteA.newEditInstance(NoteGroupA.DEFAULT_NOTE_GROUP_ID, BasicNoteA.NOTE_TYPE_CHECKED, "Checked note", false, null);
        long result = mDBNoteManager.insertNote(note);
        assertNotEquals(-1, result);
        note.setId(result);

    }

    private void testEventTypes() {
        List<BasicHEventTypeA> hEventTypes = mDBHManager.getHEventTypes();
        Assert.assertEquals(2, hEventTypes.size());

        Assert.assertEquals(1, mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId());
        Assert.assertEquals(2, mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId());
    }

    private void testEvents() {
        assertNotEquals(-1, mDBHManager.insertHEvent(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId(), null));
        sleep(500);
        assertNotEquals(-1, mDBHManager.insertHEvent(mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId(), null));
        sleep(600);
        assertNotEquals(-1, mDBHManager.insertHEvent(mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId(), "Some summary"));

        List<BasicHEventA> testEvents = new ArrayList<>();

        mDBHManager.queryHEvents(testEvents);
        assertEquals(3, testEvents.size());

        mDBHManager.queryHEventsByType(testEvents, mDBHelper.getDBDictionaryCache().getNoteItemsHEventParamId());
        assertEquals(2, testEvents.size());
        mDBHManager.queryHEventsByType(testEvents, mDBHelper.getDBDictionaryCache().getCheckoutHEventParamId());
        assertEquals(1, testEvents.size());
        assertEquals("Some summary", testEvents.get(0).getEventSummary());

        mDBHManager.queryHEvents(testEvents);
        for (BasicHEventA hEvent : testEvents) {
            long result = mDBHManager.deleteHEvent(hEvent);
            assertNotEquals(0, result);
        }

        mDBHManager.queryHEvents(testEvents);
        assertEquals(0, testEvents.size());
    }

}
