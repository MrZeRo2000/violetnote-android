package com.romanpulov.violetnote.view.action;

import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicNoteDataA;

import java.util.List;

/**
 * Class for movement with direction
 * Created by romanpulov on 12.07.2017.
 */

public abstract class BasicItemsMoveAction<T, I extends BasicEntityNoteA> extends BasicItemsAction<T, I>  {

    public BasicItemsMoveAction(T data, List<I> items) {
        super(data, items);
    }

    public abstract int getDirection();
}
