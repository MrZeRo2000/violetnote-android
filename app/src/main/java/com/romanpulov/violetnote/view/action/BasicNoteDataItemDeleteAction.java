package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteItemA;

import java.util.List;

/**
 * Created by romanpulov on 22.08.2017.
 */

public class BasicNoteDataItemDeleteAction<T extends BasicNoteItemA> extends BasicNoteAction<T> {

    @Override
    public boolean execute(DBNoteManager noteManager, T item) {
        return noteManager.delete(item);
    }

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
