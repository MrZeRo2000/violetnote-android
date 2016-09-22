package com.romanpulov.violetnote.model;

/**
 * Created by rpulov on 22.09.2016.
 */

public class BasicNoteHistoryItemA extends BasicModifiableEntityNoteA {
    protected String mValue;

    public String getValue() {
        return mValue;
    }

    private BasicNoteHistoryItemA() {

    }

    public static BasicNoteHistoryItemA newInstance(long id, long lastModified, String lastModifiedString, String value) {
        BasicNoteHistoryItemA instance = new BasicNoteHistoryItemA();

        instance.mId = id;
        instance.mLastModified = lastModified;
        instance.mLastModifiedString = lastModifiedString;
        instance.mValue = value;

        return instance;
    }

    public static BasicNoteHistoryItemA newEditInstance(String value) {
        BasicNoteHistoryItemA instance = new BasicNoteHistoryItemA();

        instance.mValue = value;

        return instance;
    }
}
