package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * BasicNoteDataA update checked action
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataItemUpdateCheckedAction extends BasicAction<BasicNoteDataA>  {
    private final boolean mChecked;

    public BasicNoteDataItemUpdateCheckedAction(BasicNoteDataA basicNoteData, boolean checked) {
        super(basicNoteData);
        mChecked = checked;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.mBasicNoteItemDAO.updateChecked(mData.getNote().getItems(), mChecked);
        return true;
    }
}
