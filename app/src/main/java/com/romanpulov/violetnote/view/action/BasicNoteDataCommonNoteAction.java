package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.List;

/**
 * Created by romanpulov on 22.08.2017.
 */

public class BasicNoteDataCommonNoteAction extends BasicNoteDataAction {
    protected final BasicNoteAction<BasicCommonNoteA> mNoteAction;
    protected final List<? extends BasicCommonNoteA> mItems;

    public BasicNoteDataCommonNoteAction(BasicNoteDataA basicNoteData, BasicNoteAction<BasicCommonNoteA> noteAction, List<? extends BasicCommonNoteA> items) {
        super(basicNoteData);
        mNoteAction = noteAction;
        mItems = items;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        return mNoteAction.execute(noteManager, mItems);
        //return true;
    }
}
