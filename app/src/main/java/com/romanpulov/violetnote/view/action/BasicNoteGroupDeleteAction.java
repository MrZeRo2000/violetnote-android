package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

public class BasicNoteGroupDeleteAction extends BasicNoteDeleteAction<BasicNoteGroupA> {
    @Override
    public boolean execute(DBNoteManager noteManager, BasicNoteGroupA item) {
        return noteManager.mBasicNoteGroupDAO.delete(item) != 0;
    }
}
