package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.util.List;
import java.util.Locale;

/**
 * Created by rpulov on 20.08.2016.
 */
public class DataGeneratorTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DataGeneratorTest";

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public DataGeneratorTest() {
        super(Application.class);
    }

    public void test1() {
        log("Data Generator test message");
        assertEquals(1, 1);
    }

    public void testGenerateData() {
        final int MAX_NOTES = 10;

        log("************ Data Generator start");

        log("Delete database");
        getContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        log("Create notes");
        DBNoteManager noteManager = new DBNoteManager(getContext());
        for (int i = 1; i <= MAX_NOTES; i++)
            noteManager.insertNote(BasicNoteA.newEditInstance(1, String.format(Locale.getDefault(), "New Note %2d", i), false, null));
        List<BasicNoteA> noteList = noteManager.queryNotes();
        assertEquals(noteList.size(), MAX_NOTES);
        log("Created " + MAX_NOTES + " notes");
    }
}
