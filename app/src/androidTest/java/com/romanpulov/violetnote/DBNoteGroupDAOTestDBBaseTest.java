package com.romanpulov.violetnote;

import com.romanpulov.violetnote.db.dao.BasicNoteGroupDAO;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DBNoteGroupDAOTestDBBaseTest extends DBBaseTest {
    @Override
    void prepareDatabase() {
        deleteDatabase();
    }

    @Test
    public void testMain() {
        initDB();

        //all groups
        List<BasicNoteGroupA> noteGroups = mDBNoteManager.mBasicNoteGroupDAO.getAll();
        Assert.assertEquals(2, noteGroups.size());

        //note groups with default note group
        noteGroups = mDBNoteManager.mBasicNoteGroupDAO.getByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE);
        Assert.assertEquals(1, noteGroups.size());

        Assert.assertEquals(0, noteGroups.get(0).getNoteCount());

        //insert
        mDBNoteManager.mBasicNoteGroupDAO.insert(BasicNoteGroupA.newEditInstance(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, "New group name", 0));
        noteGroups = mDBNoteManager.mBasicNoteGroupDAO.getByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE);
        Assert.assertEquals(2, noteGroups.size());
        Assert.assertEquals(2, noteGroups.get(0).getOrderId());
        Assert.assertEquals(3, noteGroups.get(1).getOrderId());

        List<BasicNoteGroupA> relatedNoteGroups =  mDBNoteManager.mBasicNoteGroupDAO.getRelatedNoteGroupList(noteGroups.get(0));
        Assert.assertEquals(1, relatedNoteGroups.size());
        Assert.assertNotEquals(noteGroups.get(0).getId(), relatedNoteGroups.get(0).getId());
    }
}
