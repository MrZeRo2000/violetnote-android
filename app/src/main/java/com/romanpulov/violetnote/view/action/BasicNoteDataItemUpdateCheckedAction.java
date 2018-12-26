package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * BasicNoteDataA update checked action
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataItemUpdateCheckedAction extends BasicNoteDataItemAction  {
    private final boolean mChecked;

    public BasicNoteDataItemUpdateCheckedAction(BasicNoteDataA basicNoteData, boolean checked) {
        super(basicNoteData, null);
        mChecked = checked;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.mBasicNoteItemDAO.updateCheckedList(mBasicNoteData.getNote().getItems(), mChecked);
        return true;
    }
}
