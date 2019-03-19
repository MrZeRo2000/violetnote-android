package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.List;

public class BasicNoteGroupRefreshAction extends BasicAction<List<BasicNoteGroupA>> {

    public BasicNoteGroupRefreshAction(List<BasicNoteGroupA> data) {
        super(data);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.mBasicNoteGroupDAO.fillByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, mData);
        return true;
    }
}
