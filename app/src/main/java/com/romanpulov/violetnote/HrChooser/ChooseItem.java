package com.romanpulov.violetnote.HrChooser;

import java.util.List;

/**
 * Created by romanpulov on 27.05.2016.
 */
public interface ChooseItem {
    int ITEM_UNKNOWN = 10;
    int ITEM_PARENT = 1;
    int ITEM_DIRECTORY = 2;
    int ITEM_FILE = 3;

    String ITEM_PARENT_NAME = "..";

    String getItemPath();
    String getItemName();
    int getItemType();
    List<ChooseItem> getItems();
    ChooseItem getParentItem();
}
