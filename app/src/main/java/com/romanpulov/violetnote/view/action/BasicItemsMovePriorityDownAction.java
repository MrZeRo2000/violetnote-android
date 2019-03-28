package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;

import java.util.List;

/**
 * BasicCommonNoteA move priority down action
 * Created by romanpulov on 18.08.2017.
 */

public class BasicItemsMovePriorityDownAction<T, I extends BasicCommonNoteA> extends BasicItemsMoveAction<T, I> {

    public BasicItemsMovePriorityDownAction(T data, List<I> items) {
        super(data, items);
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_DOWN;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        BasicOrderedEntityNoteA.sortDesc(mItems);

        for (I item : mItems) {
            noteManager.mBasicCommonNoteDAO.priorityDown(item);
        }

        return true;
    }
}
