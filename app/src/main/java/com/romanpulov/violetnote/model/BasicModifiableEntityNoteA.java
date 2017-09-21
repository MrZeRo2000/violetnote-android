package com.romanpulov.violetnote.model;

/**
 * Base class for modifiable BasicEntityNote
 * Created by rpulov on 22.09.2016.
 */

public abstract class BasicModifiableEntityNoteA extends BasicEntityNoteA {
    private long mLastModified;
    private String mLastModifiedString;

    public long getLastModified() {
        return mLastModified;
    }

    public void setLastModified(long mLastModified) {
        this.mLastModified = mLastModified;
    }

    public String getLastModifiedString() {
        return mLastModifiedString;
    }

    public void setLastModifiedString(String value) {
        mLastModifiedString = value;
    }

}
