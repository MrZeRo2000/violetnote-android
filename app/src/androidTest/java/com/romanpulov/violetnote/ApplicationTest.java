package com.romanpulov.violetnote;

import android.content.Context;
import android.util.Log;
import java.util.List;

import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.*;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

@SmallTest
public class ApplicationTest {
    private final static String TAG = "ApplicationTest";

    private Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }


    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Test
    public void test1() {
        log("Test message");
        assertEquals(1, 1);
    }

    @Test
    public void testDBNote() {
        log("****************** testDBNote start");

        log("Delete database");
        getTargetContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        DBNoteManager noteManager = new DBNoteManager(getTargetContext());
        log("Get group");
        BasicNoteGroupA group = noteManager.mBasicNoteGroupDAO.getById(2);

        log("Create note");
        noteManager.mBasicNoteDAO.insert(BasicNoteA.newEditInstance(BasicNoteGroupA.DEFAULT_NOTE_GROUP_ID, 1,"New Note", false, null));
        List<BasicNoteA> noteList = noteManager.mBasicNoteDAO.getTotalsByGroup(group);
        assertEquals(noteList.size(), 1);

        log("Check note");
        BasicNoteA newNote = noteList.get(0);
        log("New note:" + newNote);
        assertEquals(newNote.getOrderId(), 1);

        log("Edit note");
        newNote.setTitle("Changed title");
        noteManager.mBasicNoteDAO.update(newNote);
        log("Edited note:" + newNote);

        BasicNoteA deleteNote = noteList.get(0);
        log("Delete note");

        noteManager.mBasicNoteDAO.delete(deleteNote);
        noteList = noteManager.mBasicNoteDAO.getTotalsByGroup(group);
        assertEquals(noteList.size(), 0);

        log("****************** testDBNote finish");
    }
}