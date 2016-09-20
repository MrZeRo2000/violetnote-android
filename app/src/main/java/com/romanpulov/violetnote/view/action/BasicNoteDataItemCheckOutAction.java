package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataItemCheckOutAction extends BasicNoteDataItemAction {

    public BasicNoteDataItemCheckOutAction(BasicNoteDataA basicNoteData) {
        super(basicNoteData, null);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.checkOut(mBasicNoteData.getNote());
        return true;
    }
}
