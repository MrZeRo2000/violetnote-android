package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

/**
 * Created by rpulov on 18.09.2016.
 */
public abstract class BasicNoteDataItemAction extends BasicNoteDataAction{
    protected BasicNoteItemA mItem;

    public BasicNoteDataItemAction(BasicNoteDataA basicNoteData, BasicNoteItemA item) {
        super(basicNoteData);
        mItem = item;
    }
}
