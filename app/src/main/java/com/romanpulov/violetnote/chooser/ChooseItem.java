package com.romanpulov.violetnote.chooser;

import java.util.List;

/**
 * Created by romanpulov on 27.05.2016.
 * General interface for Choose Item
 */
public interface ChooseItem {
    int ITEM_UNKNOWN = 10;
    int ITEM_PARENT = 1;
    int ITEM_DIRECTORY = 2;
    int ITEM_FILE = 3;

    String getItemPath();
    String getDisplayItemPath();
    String getItemName();
    int getItemType();
    void fillItems();
    String getFillItemsError();
    List<ChooseItem> getItems();
}
