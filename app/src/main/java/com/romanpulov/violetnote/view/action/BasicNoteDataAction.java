package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * Base class for BasicNoteDataA actions
 * Created by romanpulov on 13.09.2016.
 */
public abstract class BasicNoteDataAction {
    protected final BasicNoteDataA mBasicNoteData;

    public BasicNoteDataAction(BasicNoteDataA basicNoteData) {
        mBasicNoteData = basicNoteData;
    }

    public abstract boolean execute(DBNoteManager noteManager);
}
