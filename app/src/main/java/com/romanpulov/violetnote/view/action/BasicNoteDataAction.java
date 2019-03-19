package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteDataA;

/**
 * Base class for BasicNoteDataA actions
 * Created by romanpulov on 13.09.2016.
 */
public abstract class BasicNoteDataAction extends BasicAction<BasicNoteDataA> {

    public BasicNoteDataAction(BasicNoteDataA basicNoteData) {
        super(basicNoteData);
    }
}
