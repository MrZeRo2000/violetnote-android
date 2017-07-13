package com.romanpulov.violetnote.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by rpulov on 28.08.2016.
 */
public abstract class BasicCommonNoteA extends BasicOrderedEntityNoteA implements DisplayTitleProvider {
    /**
     * Returns list of items by positions
     * @param items items to search in
     * @param itemPositions positions
     * @return List of items
     */
    public static List<BasicCommonNoteA> getItemsByPositions(List<? extends BasicCommonNoteA> items, Collection<Integer> itemPositions) {
        List<BasicCommonNoteA> basicNoteItems = new ArrayList<>();
        for (Integer item : itemPositions ) {
            basicNoteItems.add(items.get(item));
        }

        return  basicNoteItems;
    }
}
