package com.romanpulov.violetnote.chooser;

import java.util.Comparator;

/**
 * Created by rpulov on 07.06.2016.
 * Choose item comparator used for sorting choose items
 */
class ChooseItemComparator implements Comparator<AbstractChooseItem> {
    @Override
    public int compare(AbstractChooseItem o1, AbstractChooseItem o2) {
        if (o1 == o2)
            return 0;
        if (o1 == null)
            return -1;
        if (o2 == null)
            return 1;
        if (o1.getItemType() > o2.getItemType())
            return 1;
        if (o1.getItemType() < o2.getItemType())
            return -1;
        return o1.getItemName().compareTo(o2.getItemName());
    }
}
