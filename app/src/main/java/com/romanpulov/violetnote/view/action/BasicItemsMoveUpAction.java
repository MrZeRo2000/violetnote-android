package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;

import java.util.List;

/**
 * BasicCommonNoteA move up action
 * Created by rpulov on 07.09.2016.
 */
public class BasicItemsMoveUpAction<T, I extends BasicCommonNoteA> extends BasicItemsMoveAction<T, I> {

    public BasicItemsMoveUpAction(T data, List<I> items) {
        super(data, items);
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_UP;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortAsc(mItems);

        for (I item : mItems) {
            if (noteManager.mBasicCommonNoteDAO.moveUp(item)) {
                result = true;
            } else
                break;
        }

        return result;
    }
}
