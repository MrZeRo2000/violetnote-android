package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.List;

public class BasicNoteGroupDeleteAction extends BasicItemsCycleAction<List<BasicNoteGroupA>, BasicNoteGroupA> {

    public BasicNoteGroupDeleteAction(List<BasicNoteGroupA> data, List<BasicNoteGroupA> items) {
        super(data, items);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, BasicNoteGroupA item) {
        return noteManager.mBasicNoteGroupDAO.delete(item) != 0;
    }
}
