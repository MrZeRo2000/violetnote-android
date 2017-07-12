package com.romanpulov.violetnote.model;

import java.util.List;

/**
 * Selection utils class for multiple selection positions
 * Created by romanpulov on 11.07.2017.
 */

public final class BasicEntityNoteSelectionPosA {
    private final int mMinPos;
    private final int mMaxPos;

    public int getMinPos() {
        return mMinPos;
    }

    public int getMaxPos() {
        return mMaxPos;
    }

    private final Integer[] mSelectedItemsPositions;

    public Integer[] getSelectedItemsPositions() {
        return mSelectedItemsPositions;
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
}
