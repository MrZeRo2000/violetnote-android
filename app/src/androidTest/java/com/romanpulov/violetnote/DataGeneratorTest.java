package com.romanpulov.violetnote;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.util.Log;

import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.List;
import java.util.Locale;

/**
 * Created by rpulov on 20.08.2016.
 */
public class DataGeneratorTest extends ApplicationTestCase<Application> {
    private final static String TAG = "DataGeneratorTest";
    public final static int MAX_NOTES = 30;
    public final static int MAX_NOTE_ITEMS = 20;

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

        log("************ Data Generator start");

        log("Delete database");
        getContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);

        log("Create notes");
        DBNoteManager noteManager = new DBNoteManager(getContext());
        for (int i = 1; i <= MAX_NOTES; i++) {
            String titleFormat = "Note %2d";
            if (i == 3)
                titleFormat = "This is a very very very very very very long note with number %2d";

            BasicNoteA newNote = BasicNoteA.newEditInstance(i % 2, String.format(Locale.getDefault(), titleFormat, i), (i % 5) == 0 , null);
            assertFalse(-1 == noteManager.insertNote(newNote));

            //items are for not encrypted only
            if (!newNote.isEncrypted()) {

                if (i % 2 == 0) {
                    //checked items
                    newNote.setId(i);
                    for (int j = 1; j <= MAX_NOTE_ITEMS; j++) {
                        String itemValueFormat = "Note item %2d for note %2d";
                        if (j == 4)
                            itemValueFormat = "This is a very very very very very very long note item %2d for note %2d";

                        BasicNoteItemA newNoteItem = BasicNoteItemA.newCheckedEditInstance(String.format(Locale.getDefault(), itemValueFormat, j, i));
                        assertFalse(-1 == noteManager.insertNoteItem(newNote, newNoteItem));
                    }
                } else {
                    //named items
                    newNote.setId(i);
                    for (int j = 1; j <= MAX_NOTE_ITEMS; j++) {
                        String itemNameFormat = "Name note item %2d for note %2d";
                        String itemValueFormat = "Value note item %2d for note %2d";
                        if (j == 3) {
                            itemNameFormat = "This is a very very very very very very long note item name %2d for note %2d";
                        }
                        if (j == 4) {
                            itemValueFormat = "This is a very very very very very very long note item value %2d for note %2d";
                        }

                        BasicNoteItemA newNoteItem = BasicNoteItemA.newNamedEditInstance(
                                String.format(Locale.getDefault(), itemNameFormat, j, i),
                                String.format(Locale.getDefault(), itemValueFormat, j, i)
                        );
                        assertFalse(-1 == noteManager.insertNoteItem(newNote, newNoteItem));
                    }
                }
            }

        }
        List<BasicNoteA> noteList = noteManager.queryNotes();
        assertEquals(noteList.size(), MAX_NOTES);
        log("Created " + MAX_NOTES + " notes");
    }
}
