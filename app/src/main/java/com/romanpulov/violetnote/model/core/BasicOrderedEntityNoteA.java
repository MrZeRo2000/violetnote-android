package com.romanpulov.violetnote.model.core;

import java.util.Comparator;
import java.util.List;

/**
 * Entity class with ordering
 * Created by rpulov on 22.09.2016.
 */

public class BasicOrderedEntityNoteA extends BasicModifiableEntityNoteA {
    public static final long PRIORITY_HIGH = 1;
    public static final long PRIORITY_NORMAL = 0;
    public static final long PRIORITY_LOW = -1;

    private long mOrderId;

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long orderId) {
        this.mOrderId = orderId;
    }

    private long mPriority = PRIORITY_NORMAL;

    public long getPriority() {
        return mPriority;
    }

    public void setPriority(long priority) {
        if ((priority > PRIORITY_HIGH) || (priority < PRIORITY_LOW))
            throw new RuntimeException("invalid priority value : " + priority);
        mPriority = priority;
    }

    @Override
    public int hashCode() {
        return (int)mOrderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof BasicOrderedEntityNoteA object))
            return false;

        return (this.mOrderId == object.mOrderId) && (this.mPriority == object.mPriority);
    }

    /**
     * Ascending sorting by orderId
     * @param list sorted list
     */
    public static void sortAsc(List<? extends BasicOrderedEntityNoteA> list) {
        list.sort(new BasicOrderedEntityNoteAscComparator());
    }

    /**
     * Dedcending sorting by orderId
     * @param list sorted list
     */
    public static void sortDesc(List<? extends BasicOrderedEntityNoteA> list) {
        list.sort(new BasicOrderedEntityNoteDescComparator());
    }

    /**
     * Ascending comparator
     */
    private static class BasicOrderedEntityNoteAscComparator implements Comparator<BasicOrderedEntityNoteA> {
        @Override
        public int compare(BasicOrderedEntityNoteA lhs, BasicOrderedEntityNoteA rhs) {
            if (lhs == rhs)
                return 0;

            if (lhs.mPriority < rhs.mPriority)
                return 1;
            else if (lhs.mPriority > rhs.mPriority)
                return -1;
            else {
                return Long.compare(lhs.mOrderId, rhs.mOrderId);
            }
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

            if (lhs.mPriority < rhs.mPriority)
                return -1;
            else if (lhs.mPriority > rhs.mPriority)
                return 1;
            else {
                return Long.compare(rhs.mOrderId, lhs.mOrderId);
            }
        }
    }
}
