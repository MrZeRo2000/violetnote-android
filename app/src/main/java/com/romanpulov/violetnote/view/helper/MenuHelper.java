package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.model.BasicNoteA;

import java.util.Collection;

public class MenuHelper {
    public final static int MENU_GROUP_OTHER_ITEMS = Menu.FIRST + 100;

    /**
     * Common logic for creation of related menu for movement to other note
     * @param menu Menu to add sub-menu
     */
    public static void buildMoveToOtherNotesSubMenu(Context context, Menu menu, Collection<BasicNoteA> relatedNotes) {
        SubMenu subMenu = null;
        int order = 1;
        int relatedNoteIndex = 0;
        for (BasicNoteA relatedNote : relatedNotes) {
            if (subMenu == null) {
                subMenu = menu.addSubMenu(Menu.NONE, Menu.NONE, order++, context.getString(R.string.action_move_other));
                subMenu.getItem().setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
                subMenu.clearHeader();
            }
            subMenu.add(MENU_GROUP_OTHER_ITEMS, relatedNoteIndex ++, Menu.NONE, relatedNote.getTitle());
        }
    }
}
