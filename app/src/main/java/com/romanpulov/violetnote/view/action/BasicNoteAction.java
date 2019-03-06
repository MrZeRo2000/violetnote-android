package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicEntityNoteA;

import java.util.List;

/**
 * Base class for BasicEntityNoteA actions
 * Created by romanpulov on 07.09.2016.
 */
public abstract class BasicNoteAction <T extends BasicEntityNoteA>  {

    public abstract boolean execute(DBNoteManager noteManager, T item);
    public abstract boolean execute(DBNoteManager noteManager, List<T> items);
}
