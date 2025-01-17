package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.DisplayTitleProvider;

import java.util.Collection;

public class MenuHelper {
    public final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 100;

    /**
     * Common logic for creation of related menu for movement to other
     * @param menu Menu to add sub-menu
     */
    public static void buildMoveToOtherSubMenu(
            Context context,
            Menu menu,
            Collection<? extends DisplayTitleProvider> displayTitleProviderList) {
        SubMenu subMenu = null;
        int order = 1;
        int relatedNoteIndex = 0;
        for (DisplayTitleProvider displayTitleProvider : displayTitleProviderList) {
            if (subMenu == null) {
                subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, order++, context.getString(R.string.action_move_other));
                subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                subMenu.clearHeader();
            }
            subMenu.add(MENU_GROUP_OTHER_ITEMS, relatedNoteIndex ++, Menu.NONE, displayTitleProvider.getDisplayTitle());
        }
    }
}
