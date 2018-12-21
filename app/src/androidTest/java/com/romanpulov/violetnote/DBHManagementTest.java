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

            testNotes();
        }
    }

    private void testNotes() {

        BasicNoteA note = BasicNoteA.newEditInstance(NoteGroupA.DEFAULT_NOTE_GROUP_ID, BasicNoteA.NOTE_TYPE_NAMED, "New note", false, null);
        long result = mDBNoteManager.insertNote(note);
        assertNotEquals(-1, result);
        note.setId(result);

        BasicNoteItemA noteItem = BasicNoteItemA.newNamedEditInstance("New name", "New Value");
        result = mDBNoteManager.insertNoteItem(note, noteItem);
        assertNotEquals(-1, result);


        noteItem = BasicNoteItemA.newNamedEditInstance("Another name", "Another Value");
        result = mDBNoteManager.insertNoteItem(note, noteItem);
        assertNotEquals(-1, result);
        noteItem.setId(result);

        List<BasicHEventA> testEvents = new ArrayList<>();
        mDBHManager.queryHEvents(testEvents);
        assertEquals(2, testEvents.size());

        result = mDBNoteManager.deleteNoteItem(noteItem);
        assertNotEquals(0, result);

        testEvents = new ArrayList<>();
        mDBHManager.queryHEvents(testEvents);
        assertEquals(1, testEvents.size());
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
