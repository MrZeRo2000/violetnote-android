package com.romanpulov.violetnote.view.action;

import android.support.v7.view.ActionMode;

import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.view.core.BasicCommonNoteFragment;

/**
 * Created by rpulov on 07.09.2016.
 */
public class BasicNoteMoveBottomAction <T extends BasicCommonNoteA> extends BasicNoteAction<T> {

    public BasicNoteMoveBottomAction(BasicCommonNoteFragment fragment) {
        super(fragment);
    }

    @Override
    public boolean execute(T item) {
        return mNoteManager.moveBottom(mFragment.getDBTableName(), item);
    }
}
