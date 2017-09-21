package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.model.BasicNoteDataA;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.List;

/**
 * Base class for BasicNoteItemA action
 * Created by rpulov on 18.09.2016.
 */
public abstract class BasicNoteDataItemAction extends BasicNoteDataAction{
    protected final List<BasicNoteItemA> mItems;

    public BasicNoteDataItemAction(BasicNoteDataA basicNoteData, List<BasicNoteItemA> items) {
        super(basicNoteData);
        mItems = items;
    }
}
