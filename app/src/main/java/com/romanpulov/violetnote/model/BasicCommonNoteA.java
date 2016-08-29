package com.romanpulov.violetnote.model;

import java.util.List;

/**
 * Created by rpulov on 28.08.2016.
 */
public class BasicCommonNoteA {
    public static boolean fromInt(int value) {
        switch (value) {
            case 0: return false;
            case 1: return true;
            default: throw new IllegalArgumentException();
        }
    }
    public static int toInt(boolean value){
        return value ? 1 : 0;
    }

    public static int getNotePosWithId(List<? extends BasicCommonNoteA> noteList, long id) {
        int pos = 0;
        for (BasicCommonNoteA note : noteList) {
            if (note.getId() == id)
                return pos;
            pos++;
        }
        return -1;
    }

    protected long mId;
    protected long mLastModified;
    protected String mLastModifiedString;
    protected long mOrderId;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getLastModified() {
        return mLastModified;
    }

    public void setLastModified(long mLastModified) {
        this.mLastModified = mLastModified;
    }

    public String getLastModifiedString() {
        return mLastModifiedString;
    }

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long mOrderId) {
        this.mOrderId = mOrderId;
    }

}
