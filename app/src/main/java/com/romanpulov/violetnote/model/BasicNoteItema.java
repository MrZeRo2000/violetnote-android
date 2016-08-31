package com.romanpulov.violetnote.model;

/**
 * Created by rpulov on 11.08.2016.
 */
public class BasicNoteItemA extends BasicCommonNoteA {
    private String mName;
    private String mValue;
    private boolean mChecked;

    public String getName() {
        return mName;
    }

    public String getValue() {
        return mValue;
    }

    public boolean getChecked() {
        return mChecked;
    }

    private BasicNoteItemA() {

    }

    public static BasicNoteItemA newInstance(long id, long lastModified, String lastModifiedString, long orderId, String name, String value, boolean checked) {
        BasicNoteItemA instance = new BasicNoteItemA();

        instance.mId = id;
        instance.mLastModified = lastModified;
        instance.mLastModifiedString = lastModifiedString;
        instance.mOrderId = orderId;
        instance.mName = name;
        instance.mValue = value;
        instance.mChecked = checked;

        return  instance;
    }

    public static BasicNoteItemA newCheckedEditInstance(String value) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.mValue = value;
        return instance;
    }

    public static BasicNoteItemA newNamedEditInstance(String name, String value) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.mName = name;
        instance.mValue = value;
        return instance;
    }

}
