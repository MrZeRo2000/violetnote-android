package com.romanpulov.violetnote;

import android.support.annotation.NonNull;
import android.util.Log;
import java.util.List;
import java.util.Locale;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.*;

import com.romanpulov.violetnote.db.DBBasicNoteHelper;
import com.romanpulov.violetnote.db.DBBasicNoteOpenHelper;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

public class DataGenerator {
    private final static String TAG = "DataGenerator";
    public final static int MAX_NOTES = 30;
    public final static int MAX_NOTE_ITEMS = 20;
    public final static int MAX_VALUE_ITEMS = 10;
    public final static int MAX_HISTORY_ITEMS = 20;

    private static void log(String message) {
        Log.d(TAG, message);
    }

    public void deleteDatabase() {
        log("Delete database");
        getTargetContext().deleteDatabase(DBBasicNoteOpenHelper.DATABASE_NAME);
    }

    public void generateData() {

        log("************ Data Generator start");

        deleteDatabase();

        DBBasicNoteHelper.getInstance(getTargetContext()).openDB();
        final long priceNoteParamTypeId = DBBasicNoteHelper.getInstance(getTargetContext()).getDBDictionaryCache().getPriceNoteParamTypeId();

        log("Create notes");
        DBNoteManager noteManager = new DBNoteManager(getTargetContext());
        for (int i = 1; i <= MAX_NOTES; i++) {
            String titleFormat = "Note %2d";
            if (i == 3)
                titleFormat = "This is a very very very very very very long note with number %2d";

            BasicNoteA newNote = BasicNoteA.newEditInstance(BasicNoteGroupA.DEFAULT_NOTE_GROUP_ID, i % 2, String.format(Locale.getDefault(), titleFormat, i), (i % 5) == 0 , null);
            assertNotEquals(-1, noteManager.mBasicNoteDAO.insert(newNote));

            newNote.setId(i);
            //items are for not encrypted only
            if (!newNote.isEncrypted()) {

                if (i % 2 == 0) {
                    //checked items

                    for (int j = 1; j <= MAX_NOTE_ITEMS; j++) {
                        boolean isPrice = (i == 2) && (j == 5);
                        String itemValueFormat = "Note item %2d for note %2d name";
                        if (j == 4)
                            itemValueFormat = "This is a very very very very very very long note item %2d for note %2d name";

                        if (isPrice)
                            itemValueFormat = "Note item %2d for note %2d name with value 12.44";

                        BasicNoteItemA newNoteItem = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, String.format(Locale.getDefault(), itemValueFormat, j, i));
                        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem));

                        if (isPrice)
                            assertEquals(1244L, newNoteItem.getParamLong(priceNoteParamTypeId));
                    }


                    //values for checked items
                    for (int j = 1; j <= MAX_VALUE_ITEMS; j++) {
                        String valueFormat = "Value %2d";
                        if (j == 4)
                            valueFormat = "This is a very very very very very very long value %2d";

                        String newValue = String.format(Locale.getDefault(), valueFormat, j);
                        assertNotEquals(-1, noteManager.mBasicNoteValueDAO.insertWithNote(newNote, newValue));
                    }

                    //history for checked items
                    for (int j = 1; j <= MAX_HISTORY_ITEMS; j++) {
                        String historyValueFormat = "History Value %2d";
                        if (j == 4)
                            historyValueFormat = "This is a very very very very very very long history value %2d";

                        String newHistoryValue = String.format(Locale.getDefault(), historyValueFormat, j);
                        assertNotEquals(-1, noteManager.mBasicNoteHistoryDAO.insertNoteValue(newNote, newHistoryValue));

                        //assertFalse(-1 == noteManager.insertNoteValue(newNote, newHistoryValue));
                    }

                } else {
                    //named items
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
                        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem));
                    }
                }
            }

        }

        BasicNoteGroupA group = noteManager.mBasicNoteGroupDAO.getById(2);

        List<BasicNoteA> noteList = noteManager.mBasicNoteDAO.getTotalsByGroup(group);
        assertEquals(noteList.size(), MAX_NOTES);
        log("Created " + MAX_NOTES + " notes");

        createNoteGroup(noteManager);
        createHistoryEvents(noteManager);

        DBBasicNoteHelper.getInstance(getTargetContext()).closeDB();
    }

    private void createNoteGroup(@NonNull DBNoteManager noteManager) {
        final long priceNoteParamTypeId = DBBasicNoteHelper.getInstance(getTargetContext()).getDBDictionaryCache().getPriceNoteParamTypeId();

        //new note group
        BasicNoteGroupA newNoteGroup = BasicNoteGroupA.newEditInstance(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, "Group 2", 0);
        long newNoteGroupId = noteManager.mBasicNoteGroupDAO.insert(newNoteGroup);
        assertNotEquals(-1, newNoteGroupId);

        BasicNoteA newNote = BasicNoteA.newEditInstance(newNoteGroupId, BasicNoteA.NOTE_TYPE_CHECKED, "Checked 1", false, null);
        long newNoteId = noteManager.mBasicNoteDAO.insert(newNote);
        assertNotEquals(-1, newNoteId);
        newNote.setId(newNoteId);

        BasicNoteItemA newNoteItem = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Item 1");
        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem));

        BasicNoteItemA newNoteItem2 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Item 2");
        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem2));

        noteManager.mBasicNoteItemDAO.updateChecked(newNoteItem2, true);

        BasicNoteA newNote2 = BasicNoteA.newEditInstance(newNoteGroupId, BasicNoteA.NOTE_TYPE_CHECKED, "Checked 2", false, null);
        long newNoteId2 = noteManager.mBasicNoteDAO.insert(newNote2);
        assertNotEquals(-1, newNoteId2);
        newNote2.setId(newNoteId2);

        BasicNoteItemA newNoteItem21 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Item 21");
        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote2, newNoteItem21));

        //new empty note group
        BasicNoteGroupA emptyNoteGroup = BasicNoteGroupA.newEditInstance(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, "Empty group", 0);
        assertNotEquals(-1, noteManager.mBasicNoteGroupDAO.insert(emptyNoteGroup));
    }

    private void createHistoryEvents(@NonNull DBNoteManager noteManager) {
        final long priceNoteParamTypeId = DBBasicNoteHelper.getInstance(getTargetContext()).getDBDictionaryCache().getPriceNoteParamTypeId();

        //new note group
        BasicNoteGroupA newNoteGroup = BasicNoteGroupA.newEditInstance(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, "History Events", 0);
        long newNoteGroupId = noteManager.mBasicNoteGroupDAO.insert(newNoteGroup);
        assertNotEquals(-1, newNoteGroupId);

        //new checked note
        BasicNoteA newNote = BasicNoteA.newEditInstance(newNoteGroupId, BasicNoteA.NOTE_TYPE_CHECKED, "Animals", false, null);
        long newNoteId = noteManager.mBasicNoteDAO.insert(newNote);
        assertNotEquals(-1, newNoteGroupId);
        newNote.setId(newNoteId);



        //new checked note item 1
        BasicNoteItemA newNoteItem1 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Cat");
        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem1));

        BasicNoteItemA newNoteItem2 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Dog");
        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem2));

        BasicNoteItemA newNoteItem3 = BasicNoteItemA.newCheckedEditInstance(priceNoteParamTypeId, "Elephant");
        assertNotEquals(-1, noteManager.mBasicNoteItemDAO.insertWithNote(newNote, newNoteItem3));

    }
}
