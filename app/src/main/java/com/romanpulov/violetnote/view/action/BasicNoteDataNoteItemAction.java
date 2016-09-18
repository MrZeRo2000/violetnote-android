package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataNoteItemAction extends BasicNoteDataItemAction {
    private final BasicNoteAction<BasicNoteItemA> mNoteAction;

    public BasicNoteDataNoteItemAction(BasicNoteDataA basicNoteData, BasicNoteAction<BasicNoteItemA> noteAction, BasicNoteItemA item) {
        super(basicNoteData, item);
        mNoteAction = noteAction;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return mNoteAction.execute(null, mItem);
    }
}
