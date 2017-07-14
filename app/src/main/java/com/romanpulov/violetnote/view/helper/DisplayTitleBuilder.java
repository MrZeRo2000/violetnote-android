package com.romanpulov.violetnote.view.helper;

import android.content.Context;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.DisplayTitleProvider;

import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Display title builder class
 * Created by romanpulov on 23.06.2017.
 */

public class DisplayTitleBuilder {

    public static String buildItemsDisplayTitle(Context context, List<? extends DisplayTitleProvider> items, Collection<Integer> selectedItems) {
        String title = "";

        for (Integer selectedItem : selectedItems) {
            DisplayTitleProvider item = items.get(selectedItem);
            if (item != null) {
                if (title.isEmpty())
                    title = item.getDisplayTitle();
                else {
                    title = String.format(Locale.getDefault(), context.getString(R.string.title_multiple_items_selected), selectedItems.size());
                    break;
                }
            }
        }
        return title;
    }
}
