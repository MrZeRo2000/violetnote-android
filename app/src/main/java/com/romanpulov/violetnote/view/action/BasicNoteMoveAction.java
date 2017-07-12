package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.DBDataProvider;
import com.romanpulov.violetnote.model.BasicEntityNoteA;

/**
 * Class for movement with direction
 * Created by romanpulov on 12.07.2017.
 */

public abstract class BasicNoteMoveAction <T extends BasicEntityNoteA> extends BasicNoteAction<T>  {
    public static final int DIRECTION_UP = 1;
    public static final int DIRECTION_DOWN = -1;

    public BasicNoteMoveAction(DBDataProvider dbDataProvider) {
        super(dbDataProvider);
    }

    public abstract int getDirection();
}
