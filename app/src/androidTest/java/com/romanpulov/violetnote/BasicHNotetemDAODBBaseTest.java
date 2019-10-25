package com.romanpulov.violetnote;

import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemDAO;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class BasicHNotetemDAODBBaseTest extends DBBaseTest {
    @Override
    void prepareDatabase() {
        deleteDatabase();
        (new DataGenerator()).generateData();
    }

    @Test
    public void testMain() {
        synchronized (DBLock.instance) {
            initDB();

            // get all groups
            List<BasicNoteGroupA> noteGroups = mDBNoteManager.mBasicNoteGroupDAO.getAll();
            Assert.assertNotEquals(0, noteGroups.size());

            // get history events group
            BasicNoteGroupA historyEventsGroup = null;
            // get history events group
            for (BasicNoteGroupA group : noteGroups) {
                if (group.getGroupName().equals("History Events")) {
                    historyEventsGroup = group;
                    break;
                }
            }
            Assert.assertNotNull(historyEventsGroup);

            // get animals note
            List<BasicNoteA> notes = mDBNoteManager.mBasicNoteDAO.getByGroup(historyEventsGroup);
            Assert.assertNotEquals(0, notes.size());

            BasicNoteA animalsNote = null;
            for (BasicNoteA note : notes) {
                if (note.getTitle().equals("Animals")) {
                    animalsNote = note;
                    break;
                }
            }
            Assert.assertNotNull(animalsNote);

            List<BasicHNoteCOItemA> hNoteCOItems = mDBHManager.mBasicHNoteCOItemDAO.getByNoteId(animalsNote.getId());
            Assert.assertEquals(3, hNoteCOItems.size());

            List<BasicHEventA> hEvents = mDBHManager.mBasicHEventDAO.getByCOItemsNoteId(animalsNote.getId());
            Assert.assertEquals(2, hEvents.size());

            Assert.assertEquals(1, hEvents.get(0).getItemCount());
            Assert.assertEquals(2, hEvents.get(1).getItemCount());
        }
    }
}
