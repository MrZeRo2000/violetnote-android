package com.romanpulov.violetnote.model;

import java.util.Collection;
import java.util.List;

/**
 * Display title builder class
 * Created by romanpulov on 23.06.2017.
 */

public class DisplayTitleBuilder {
    private static String MULTIPLE_ITEMS_DISPLAY_TITLE = "...";

    public static String buildItemsDisplayTitle(List<? extends DisplayTitleProvider> items, Collection<Integer> selectedItems) {
        String title = "";

        for (Integer selectedItem : selectedItems) {
            DisplayTitleProvider item = items.get(selectedItem);
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
