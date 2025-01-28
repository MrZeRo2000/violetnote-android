package com.romanpulov.violetnote;

import android.content.Context;
import android.util.Log;
import java.util.List;

import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;

import com.romanpulov.violetnote.db.dao.BasicNoteDAO;
import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import org.junit.*;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
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
    public void testDBNote() {
        log("****************** testDBNote start");

        log("Delete database");
        getTargetContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        BasicNoteGroupDAO basicNoteGroupDAO = new BasicNoteGroupDAO(getTargetContext());

        log("Get group");
        BasicNoteGroupA group = basicNoteGroupDAO.getById(2);

        BasicNoteDAO basicNoteDAO = new BasicNoteDAO(getTargetContext());

        log("Create note");
        basicNoteDAO.insert(BasicNoteA.newEditInstance(BasicNoteGroupA.DEFAULT_NOTE_GROUP_ID, 1,"New Note", false, null));
        List<BasicNoteA> noteList = basicNoteDAO.getTotalsByGroup(group);
        assertEquals(1, noteList.size());

        log("Check note");
        BasicNoteA newNote = noteList.get(0);
        log("New note:" + newNote);
        assertEquals(1, newNote.getOrderId());

        log("Edit note");
        newNote.setTitle("Changed title");
        basicNoteDAO.update(newNote);
        log("Edited note:" + newNote);

        BasicNoteA deleteNote = noteList.get(0);
        log("Delete note");

        basicNoteDAO.delete(deleteNote);
        noteList = basicNoteDAO.getTotalsByGroup(group);
        assertEquals(0, noteList.size());

        log("****************** testDBNote finish");
    }
}