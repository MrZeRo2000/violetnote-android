package com.romanpulov.violetnote.view.action;

import android.content.Context;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by romanpulov on 13.09.2016.
 */
public abstract class BasicNoteDataAction {
    protected final BasicNoteDataA mBasicNoteData;
    protected BasicNoteItemA mItem;

    public BasicNoteDataAction(BasicNoteDataA basicNoteData) {
        mBasicNoteData = basicNoteData;
    }

    public abstract boolean execute(DBNoteManager noteManager);
}
