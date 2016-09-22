package com.romanpulov.violetnote.model;

/**
 * Created by rpulov on 22.09.2016.
 */

public class BasicModifiableEntityNoteA extends BasicEntityNoteA {
    protected long mLastModified;
    protected String mLastModifiedString;

    public long getLastModified() {
        return mLastModified;
    }

    public void setLastModified(long mLastModified) {
        this.mLastModified = mLastModified;
    }

    public String getLastModifiedString() {
        return mLastModifiedString;
    }

}
