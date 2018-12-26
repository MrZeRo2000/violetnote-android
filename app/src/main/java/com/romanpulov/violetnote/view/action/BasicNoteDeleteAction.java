package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;

import java.util.List;

/**
 * BasicCommonNoteA delete action
 * Created by romanpulov on 22.08.2017.
 */

public abstract class BasicNoteDeleteAction<T extends BasicCommonNoteA> extends BasicNoteAction<T> {

    @Override
    public boolean execute(DBNoteManager noteManager, List<T> items) {
        boolean result = false;

        for (T item : items) {
            if (execute(noteManager, item))
                result = true;
            else
                break;
        }

        return result;
    }
}
