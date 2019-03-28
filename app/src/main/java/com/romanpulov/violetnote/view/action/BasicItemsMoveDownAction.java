package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicCommonNoteA;
import com.romanpulov.violetnote.model.BasicOrderedEntityNoteA;
import com.romanpulov.violetnote.model.MovementDirection;

import java.util.List;

/**
 * BasicCommonNoteA move down action
 * Created by rpulov on 07.09.2016.
 */
public class BasicItemsMoveDownAction<T, I extends BasicCommonNoteA> extends BasicItemsMoveAction<T, I> {

    public BasicItemsMoveDownAction(T data, List<I> items) {
        super(data, items);
    }

    @Override
    public int getDirection() {
        return MovementDirection.DIRECTION_DOWN;
    }

    @Override
    public boolean execute(DBNoteManager noteManager) {
        boolean result = false;
        BasicOrderedEntityNoteA.sortDesc(mItems);

        for (I item : mItems) {
            if (noteManager.mBasicCommonNoteDAO.moveDown(item)) {
                result = true;
            } else
                break;
        }

        return result;
    }
}
