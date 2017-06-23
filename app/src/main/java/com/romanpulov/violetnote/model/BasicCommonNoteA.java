package com.romanpulov.violetnote.model;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rpulov on 28.08.2016.
 */
public abstract class BasicCommonNoteA extends BasicOrderedEntityNoteA implements DisplayTitleProvider {
    private static String MULTIPLE_ITEMS_DISPLAY_TITLE = "...";

    public static String getItemsDisplayTitle(List<? extends BasicCommonNoteA> items, Collection<Integer> selectedItems) {
        String title = "";

        for (Integer selectedItem : selectedItems) {
            BasicCommonNoteA item = items.get(selectedItem);
            if (item != null) {
                if (title.isEmpty())
                    title = item.getDisplayTitle();
                else {
                    title = MULTIPLE_ITEMS_DISPLAY_TITLE;
                    break;
                }
            }
        }
        return title;
    }
}
