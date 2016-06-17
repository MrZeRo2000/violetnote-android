package com.romanpulov.violetnote.chooser;

import java.util.Comparator;

/**
 * Created by rpulov on 07.06.2016.
 */
public class ChooseItemComparator implements Comparator<ChooseItem> {
    @Override
    public int compare(ChooseItem o1, ChooseItem o2) {
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
