package com.romanpulov.violetnote.model;

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
