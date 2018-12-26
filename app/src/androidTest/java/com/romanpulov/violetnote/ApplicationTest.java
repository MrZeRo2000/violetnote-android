package com.romanpulov.violetnote;

import android.util.Log;
import java.util.List;

import android.support.test.filters.SmallTest;
import org.junit.*;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.library.dropbox.DropboxHelper;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.NoteGroupA;

@SmallTest
public class ApplicationTest {
    private final static String TAG = "ApplicationTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void test2() {
        DropboxHelper dh = DropboxHelper.getInstance(getTargetContext());
        log("Token: " + dh.getAccessToken());
        assertEquals(1, 1);
    }

    @Test
    public void testDBNote() {
        log("****************** testDBNote start");

        log("Delete database");
        getTargetContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        log("Create note");
        DBNoteManager noteManager = new DBNoteManager(getTargetContext());
        noteManager.mBasicNoteDAO.insert(BasicNoteA.newEditInstance(NoteGroupA.DEFAULT_NOTE_GROUP_ID, 1,"New Note", false, null));
        List<BasicNoteA> noteList = noteManager.mBasicNoteDAO.getTotals();
        assertEquals(noteList.size(), 1);

        log("Check note");
        BasicNoteA newNote = noteList.get(0);
        log("New note:" + newNote);
        assertEquals(newNote.getOrderId(), 1);

        log("Edit note");
        newNote.setTitle("Changed title");
        noteManager.updateNote(newNote);
        log("Edited note:" + newNote);

        BasicNoteA deleteNote = noteList.get(0);
        log("Delete note");

        noteManager.mBasicNoteDAO.delete(deleteNote);
        noteList = noteManager.mBasicNoteDAO.getTotals();
        assertEquals(noteList.size(), 0);

        log("****************** testDBNote finish");
    }
}