package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteItemA;

public class BasicNoteItemDeleteAction extends BasicNoteDeleteAction<BasicNoteItemA> {
    @Override
    public boolean execute(DBNoteManager noteManager, BasicNoteItemA item) {
        return noteManager.mBasicNoteItemDAO.delete(item) != 0;
    }
}
