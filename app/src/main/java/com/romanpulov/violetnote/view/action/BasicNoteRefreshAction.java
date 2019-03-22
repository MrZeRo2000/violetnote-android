package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteA;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.List;

public class BasicNoteRefreshAction extends BasicAction<List<BasicNoteA>> {

    private final BasicNoteGroupA mNoteGroup;

    public BasicNoteRefreshAction(List<BasicNoteA> data, BasicNoteGroupA noteGroup) {
        super(data);
        this.mNoteGroup = noteGroup;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.mBasicNoteDAO.fillNotesByGroup(mNoteGroup, mData);
        return true;
    }
}
