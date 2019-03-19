package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;

import java.util.List;

public class BasicNoteGroupAction extends BasicAction<List<BasicNoteGroupA>> {

    private final BasicNoteAction<BasicNoteGroupA> mNoteGroupAction;


    public BasicNoteGroupAction(List<BasicNoteGroupA> data, BasicNoteAction<BasicNoteGroupA> noteGroupAction) {
        super(data);
        mNoteGroupAction = noteGroupAction;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return mNoteGroupAction.execute(noteManager, mData);
    }
}
