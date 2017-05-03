package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.db.DBDataProvider;

import java.util.List;

/**
 * Created by romanpulov on 07.09.2016.
 */
public abstract class BasicNoteAction <T extends BasicEntityNoteA>  {
    protected final DBDataProvider mDBDataProvider;

    public BasicNoteAction(DBDataProvider dbDataProvider) {
        mDBDataProvider = dbDataProvider;
    }

    public abstract boolean execute(DBNoteManager noteManager, T item);
    public abstract boolean execute(DBNoteManager noteManager, List<T> items);
}
