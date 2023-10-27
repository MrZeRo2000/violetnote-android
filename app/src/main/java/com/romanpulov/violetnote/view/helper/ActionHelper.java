package com.romanpulov.violetnote.view.helper;

import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;

/**
 * Helper class contains common action related procedures
 * Created by romanpulov on 29.08.2017.
 */

public class ActionHelper {

    /**
     * Updates action menu
     * @param menu Menu to update
     * @param selectedCount selected items count
     * @param totalCount total items count
     */
    public static int updateActionMenu(Menu menu, int selectedCount, int totalCount) {
        int visibleCount = 0;

        for (int menuIndex = 0; menuIndex < menu.size(); menuIndex ++) {
            MenuItem menuItem = menu.getItem(menuIndex);

            if (menuItem != null) {
                boolean isVisible;

                int itemId = menuItem.getItemId();
                if (itemId == R.id.select_all) {
                    isVisible = !((selectedCount == totalCount));
                } else if (itemId == R.id.edit_value || itemId == R.id.edit || itemId == R.id.history) {
                    isVisible = selectedCount == 1;
                } else if (itemId == R.id.move_up || itemId == R.id.move_down || itemId == R.id.move_top || itemId == R.id.move_bottom || itemId == R.id.priority_up || itemId == R.id.priority_down) {
                    isVisible = selectedCount != totalCount;
                } else {
                    isVisible = menuItem.isVisible();
                }

                menuItem.setVisible(isVisible);
                if (isVisible) {
                    visibleCount ++;
                }
            }
        }

        return visibleCount;
    }

    /**
     * Creates selected items for SelectAll functionality
     * @param itemCount items count
     * @return selected items array
     */
    public static Integer[] createSelectAllItems(int itemCount) {
        Integer[] items = new Integer[itemCount];
        for (int i = 0; i < itemCount; i++)
            items[i] = i;

        return items;
    }
}
