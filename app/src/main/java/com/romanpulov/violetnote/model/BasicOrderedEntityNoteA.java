package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Entity class with ordering
 * Created by rpulov on 22.09.2016.
 */

public class BasicOrderedEntityNoteA extends BasicModifiableEntityNoteA{
    protected long mOrderId;

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long mOrderId) {
        this.mOrderId = mOrderId;
    }

    @Override
    public int hashCode() {
        return (int)mOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof BasicOrderedEntityNoteA))
            return false;

        BasicOrderedEntityNoteA object = (BasicOrderedEntityNoteA) o;
        return this.mOrderId == object.mOrderId;
    }

    public int compareTo(@NonNull BasicOrderedEntityNoteA another) {
        if (this == another)
            return 0;

        if (this.mOrderId > another.mOrderId)
            return 1;
        else if (this.mOrderId < another.mOrderId)
            return -1;
        else
            return 0;
    }

    /**
     * Ascending sorting by orderId
     * @param list sorted list
     */
    public static void sortAsc(List<? extends BasicOrderedEntityNoteA> list) {
        Collections.sort(list, new BasicOrderedEntityNoteA.BasicOrderedEntityNoteAscComparator());
    }

    /**
     * Dedcending sorting by orderId
     * @param list sorted list
     */
    public static void sortDesc(List<? extends BasicOrderedEntityNoteA> list) {
        Collections.sort(list, new BasicOrderedEntityNoteA.BasicOrderedEntityNoteDescComparator());
    }

    /**
     * Ascending comparator
     */
    private static class BasicOrderedEntityNoteAscComparator implements Comparator<BasicOrderedEntityNoteA> {
        @Override
        public int compare(BasicOrderedEntityNoteA lhs, BasicOrderedEntityNoteA rhs) {
            if (lhs == rhs)
                return 0;

            if (lhs.mOrderId > rhs.mOrderId)
                return 1;
            else if (lhs.mOrderId < rhs.mOrderId)
                return -1;
            else
                return 0;
        }
    }

    /**
     * Descending comparator
     */
    private static class BasicOrderedEntityNoteDescComparator implements Comparator<BasicOrderedEntityNoteA> {
        @Override
        public int compare(BasicOrderedEntityNoteA lhs, BasicOrderedEntityNoteA rhs) {
            if (lhs == rhs)
                return 0;

            if (lhs.mOrderId > rhs.mOrderId)
                return -1;
            else if (lhs.mOrderId < rhs.mOrderId)
                return 1;
            else
                return 0;
        }
    }
}
