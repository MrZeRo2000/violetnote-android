package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.List;

/**
 * BasicNoteItemA action
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataNoteItemAction extends BasicItemsAction<BasicNoteDataA, BasicNoteItemA> {
    private final BasicNoteAction<BasicNoteItemA> mNoteAction;

    public BasicNoteDataNoteItemAction(BasicNoteDataA basicNoteData, BasicNoteAction<BasicNoteItemA> noteAction, List<BasicNoteItemA> items) {
        super(basicNoteData, items);
        mNoteAction = noteAction;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return mNoteAction.execute(noteManager, mItems);
    }
}
