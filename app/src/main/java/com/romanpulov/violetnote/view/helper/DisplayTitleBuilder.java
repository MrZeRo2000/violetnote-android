package com.romanpulov.violetnote.view.helper;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.DisplayTitleProvider;

import java.util.Collection;
import java.util.List;

/**
 * Display title builder class
 * Created by romanpulov on 23.06.2017.
 */

public class DisplayTitleBuilder {

    /**
     * Builds display title from collection of items
     * @param context context to retrieve resource strings
     * @param items full items list
     * @param selectedItems items to show title
     * @return string with title
     */
    public static String buildItemsDisplayTitle(Context context, List<? extends DisplayTitleProvider> items, Collection<Integer> selectedItems) {
        String title = "";

        for (Integer selectedItem : selectedItems) {
            DisplayTitleProvider item = items.get(selectedItem);
            if (item != null) {
                if (title.isEmpty())
                    title = item.getDisplayTitle();
                else {
                    title = context.getString(R.string.title_multiple_items_selected, selectedItems.size());
                    break;
                }
            }
        }
        return title;
    }

    public static String buildItemsTitle(Context context, Collection<? extends DisplayTitleProvider> selectedItems) {
        String title = "";

        if ((context != null) && (selectedItems != null)) {
            title = selectedItems.size() == 1 ?
                    selectedItems.iterator().next().getDisplayTitle() :
                    context.getString(R.string.title_multiple_items_selected, selectedItems.size());
        }

        return title;
    }
}
