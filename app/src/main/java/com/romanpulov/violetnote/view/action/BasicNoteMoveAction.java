package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBDataProvider;
import com.romanpulov.violetnote.model.BasicEntityNoteA;

/**
 * Class for movement with direction
 * Created by romanpulov on 12.07.2017.
 */

public abstract class BasicNoteMoveAction <T extends BasicEntityNoteA> extends BasicNoteAction<T>  {
    public BasicNoteMoveAction(DBDataProvider dbDataProvider) {
        super(dbDataProvider);
    }

    public abstract int getDirection();
}
