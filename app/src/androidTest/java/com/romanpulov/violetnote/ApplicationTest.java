package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.dropbox.DropBoxHelper;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.util.List;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    private final static String TAG = "ApplicationTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public ApplicationTest() {
        super(Application.class);
    }

    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    public void test2() {
        DropBoxHelper dh = DropBoxHelper.getInstance(getContext());
        log("Token: " + dh.getAccessToken());
    }

    public void testDBNote() {
        log("****************** testDBNote start");

        log("Delete database");
        getContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        log("Create note");
        DBNoteManager noteManager = new DBNoteManager(getContext());
        noteManager.insertNote(BasicNoteA.newEditInstance(1, "New Note", false, null));
        List<BasicNoteA> noteList = noteManager.queryNotes();
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

        noteManager.deleteNote(deleteNote);
        noteList = noteManager.queryNotes();
        assertEquals(noteList.size(), 0);

        log("****************** testDBNote finish");
    }
}