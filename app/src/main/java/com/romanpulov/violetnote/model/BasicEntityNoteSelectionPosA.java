package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Selection utils class for multiple selection positions
 * Created by romanpulov on 11.07.2017.
 */

public final class BasicEntityNoteSelectionPosA {
    private final int mMinPos;
    private final int mMaxPos;

    private final Integer[] mSelectedItemsPositions;

    public Integer[] getSelectedItemsPositions() {
        return mSelectedItemsPositions;
    }

    /**
     * Generic method which returns list of items by positions
     * @param items items to search in
     * @param itemPositions positions
     * @return List of items
     */
    @NonNull
    public static <T> List<T> getItemsByPositions(@NonNull List<? extends T> items, @NonNull Collection<Integer> itemPositions) {
        List<T> itemsPos = new ArrayList<>();

        for (Integer item : itemPositions ) {
            itemsPos.add(items.get(item));
        }

        return  itemsPos;
    }

    public BasicEntityNoteSelectionPosA(List<? extends BasicEntityNoteA> items, List<? extends BasicEntityNoteA> selectedItems) {
        mSelectedItemsPositions = new Integer[selectedItems.size()];
        int itemPosIndex = 0;
        int calcMinPos = -1;
        int calcMaxPos = -1;

        for (BasicEntityNoteA item : selectedItems) {
            int notePos =  BasicEntityNoteA.getNotePosWithId(items, item.getId());

            // calc min max note pos
            if (itemPosIndex == 0) {
                calcMinPos = notePos;
                calcMaxPos = notePos;
            }
            else {
                if (notePos < calcMinPos) {
                    calcMinPos = notePos;
                }
                if (notePos > calcMaxPos) {
                    calcMaxPos = notePos;
                }
            }

            mSelectedItemsPositions[itemPosIndex ++] = notePos;
        }

        mMinPos = calcMinPos;
        mMaxPos = calcMaxPos;
    }

    /**
     * Returns max or min pos depending on direction
     * @param direction up or down
     * @return selection position
     */
    public int getDirectionPos(int direction) {
        switch (direction) {
            case MovementDirection.DIRECTION_UP:
                return mMinPos;
            case MovementDirection.DIRECTION_DOWN:
                return mMaxPos;
            default:
                return -1;
        }
    }
}
