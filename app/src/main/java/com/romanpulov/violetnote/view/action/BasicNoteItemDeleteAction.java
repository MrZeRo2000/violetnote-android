package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.List;

public class BasicNoteItemDeleteAction extends BasicItemsCycleAction<BasicNoteDataA, BasicNoteItemA> {

    public BasicNoteItemDeleteAction(BasicNoteDataA data, List<BasicNoteItemA> items) {
        super(data, items);
    }

    @Override
    public boolean execute(DBNoteManager noteManager, BasicNoteItemA item) {
        return noteManager.mBasicNoteItemDAO.delete(item) != 0;
    }
}
