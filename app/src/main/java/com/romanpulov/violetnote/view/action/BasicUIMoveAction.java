package com.romanpulov.violetnote.view.action;

import androidx.recyclerview.widget.RecyclerView;
import com.romanpulov.violetnote.model.BasicEntityNoteA;
import com.romanpulov.violetnote.model.BasicEntityNoteSelectionPosA;
import com.romanpulov.violetnote.view.core.ViewSelectorHelper;

import java.util.Collection;

public class BasicUIMoveAction<T extends Collection<? extends BasicEntityNoteA>> implements UIAction<T> {
    private final T mItems;
    private final int mDirection;
    private final ViewSelectorHelper.AbstractViewSelector<Integer> mRecyclerViewSelector;
    private final RecyclerView mRecyclerView;

    public BasicUIMoveAction(
            T items,
            int direction,
            ViewSelectorHelper.AbstractViewSelector<Integer> recyclerViewSelector,
            RecyclerView recyclerView) {
        this.mItems = items;
        this.mDirection = direction;
        this.mRecyclerViewSelector = recyclerViewSelector;
        this.mRecyclerView = recyclerView;
    }

    @Override
    public void execute(T data) {
        BasicEntityNoteSelectionPosA selectionPos = new BasicEntityNoteSelectionPosA(data, mItems);
        int selectionScrollPos = selectionPos.getDirectionPos(mDirection);
        if (selectionScrollPos != -1) {
            mRecyclerViewSelector.setSelectedItems(selectionPos.getSelectedItemsPositions());
            mRecyclerView.scrollToPosition(selectionScrollPos);
        }
    }
}
