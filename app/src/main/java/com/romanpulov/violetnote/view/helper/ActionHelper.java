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
    public static void updateActionMenu(Menu menu, int selectedCount, int totalCount) {
        for (int menuIndex = 0; menuIndex < menu.size(); menuIndex ++) {
            MenuItem menuItem = menu.getItem(menuIndex);

            if (menuItem != null) {
                switch (menuItem.getItemId()) {
                    case R.id.select_all:
                        menuItem.setVisible(!((selectedCount == totalCount)));
                        break;
                    case R.id.edit_value:
                    case R.id.edit:
                        menuItem.setVisible(selectedCount == 1);
                        break;
                    case R.id.move_up:
                    case R.id.move_down:
                    case R.id.move_top:
                    case R.id.move_bottom:
                    case R.id.priority_up:
                    case R.id.priority_down:
                        menuItem.setVisible(selectedCount != totalCount);
                        break;
                }
            }
        }
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
