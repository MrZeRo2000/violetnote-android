package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataItemUpdateCheckedAction extends BasicNoteDataItemAction  {
    private final boolean mChecked;

    public BasicNoteDataItemUpdateCheckedAction(BasicNoteDataA basicNoteData, BasicNoteItemA item, boolean checked) {
        super(basicNoteData, item);
        mChecked = checked;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.updateNoteDataChecked(mBasicNoteData, mChecked);
        return true;
    }
}
