package com.romanpulov.violetnote;

import android.util.Log;

import com.romanpulov.violetnote.db.DBDataUpgradeManager;
import com.romanpulov.violetnote.db.dao.BasicHEventDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteCOItemDAO;
import com.romanpulov.violetnote.db.dao.BasicHNoteItemDAO;
import com.romanpulov.violetnote.model.BasicHEventA;
import com.romanpulov.violetnote.model.BasicHNoteCOItemA;
import com.romanpulov.violetnote.model.BasicHNoteItemA;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import org.junit.Test;
import org.junit.Assert;

import java.util.List;

public class BasicHNotetemDAODBBaseTest extends DBBaseTest {
    private final static String TAG = BasicHNotetemDAODBBaseTest.class.getSimpleName();

    private static void log(String message) {
        Log.d(TAG, message);
    }

    @Override
    void prepareDatabase() {
        deleteDatabase();
        (new DataGenerator()).generateData();
    }

    @Test
    public void testMain() {
        synchronized (DBLock.instance) {
            initDB();

            log("Getting event groups");

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

            log("Getting animal notes");

            // get animals note
            List<BasicNoteA> notes = mDBNoteManager.mBasicNoteDAO.getByGroup(historyEventsGroup);
            Assert.assertNotEquals(0, notes.size());

            BasicNoteA animalsNote = null;
            BasicNoteA metricsNote = null;
            for (BasicNoteA note : notes) {
                if (note.getTitle().equals("Animals")) {
                    animalsNote = note;
                } else if (note.getTitle().equals("Metrics")) {
                    metricsNote = note;
                }

                if ((animalsNote != null) && (metricsNote != null)) {
                    break;
                }
            }
            Assert.assertNotNull(animalsNote);

            BasicHNoteCOItemDAO basicHNoteCOItemDAO = new BasicHNoteCOItemDAO(mContext);
            BasicHEventDAO basicHEventDAO = new BasicHEventDAO(mContext);


            List<BasicHNoteCOItemA> hNoteCOItems = basicHNoteCOItemDAO.getByNoteId(animalsNote.getId());
            Assert.assertEquals(4, hNoteCOItems.size());

            List<BasicHEventA> hEvents = basicHEventDAO.getByCOItemsNoteId(animalsNote.getId());
            Assert.assertEquals(3, hEvents.size());

            Assert.assertEquals(1, hEvents.get(0).getItemCount());
            Assert.assertEquals(1, hEvents.get(1).getItemCount());
            Assert.assertEquals(2, hEvents.get(2).getItemCount());

            (new DBDataUpgradeManager(mDB)).upgradeData(4);

            hEvents = basicHEventDAO.getByCOItemsNoteId(animalsNote.getId());
            Assert.assertEquals(2, hEvents.size());

            //HNoteItem

            Assert.assertNotNull(metricsNote);

            BasicNoteItemA colorNoteItem = null;
            for (BasicNoteItemA item : mDBNoteManager.mBasicNoteItemDAO.getByNote(metricsNote)) {
                if (item.getName().equals("Color")) {
                    colorNoteItem = item;
                    break;
                }
            }

            Assert.assertNotNull(colorNoteItem);

            BasicHNoteItemDAO basicHNoteItemDAO = new BasicHNoteItemDAO(mContext);

            List<BasicHNoteItemA> hNoteItems = basicHNoteItemDAO.getByNoteItemIdWithEvents(colorNoteItem.getId());
            Assert.assertEquals(3, hNoteItems.size());

            closeDB();
        }
    }
}
