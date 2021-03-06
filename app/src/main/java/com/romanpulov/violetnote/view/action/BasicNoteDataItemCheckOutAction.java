package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * BasicNoteDataA checkout action
 * Created by rpulov on 18.09.2016.
 */
public class BasicNoteDataItemCheckOutAction extends BasicAction<BasicNoteDataA> {

    public BasicNoteDataItemCheckOutAction(BasicNoteDataA basicNoteData) {
        super(basicNoteData);
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        noteManager.mBasicNoteDAO.checkOut(mData.getNote());
        return true;
    }
}
