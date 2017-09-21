package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BasicNote history item data
 * Created by rpulov on 22.09.2016.
 */

public class BasicNoteHistoryItemA extends BasicModifiableEntityNoteA implements Parcelable {
    protected String mValue;

    public String getValue() {
        return mValue;
    }

    private BasicNoteHistoryItemA() {

    }

    public static BasicNoteHistoryItemA newInstance(long id, long lastModified, String lastModifiedString, String value) {
        BasicNoteHistoryItemA instance = new BasicNoteHistoryItemA();

        instance.setId(id);
        instance.setLastModified(lastModified);
        instance.setLastModifiedString(lastModifiedString);
        instance.mValue = value;

        return instance;
    }

    public static BasicNoteHistoryItemA newEditInstance(String value) {
        BasicNoteHistoryItemA instance = new BasicNoteHistoryItemA();

        instance.mValue = value;

        return instance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(getLastModified());
        dest.writeString(getLastModifiedString());
        dest.writeString(mValue);
    }

    private BasicNoteHistoryItemA(Parcel in) {
        setId(in.readLong());
        setLastModified(in.readLong());
        setLastModifiedString(in.readString());
        mValue = in.readString();
    }

    public static final Parcelable.Creator<BasicNoteHistoryItemA> CREATOR = new Parcelable.Creator<BasicNoteHistoryItemA>() {
        @Override
        public BasicNoteHistoryItemA createFromParcel(Parcel source) {
            return new BasicNoteHistoryItemA(source);
        }

        @Override
        public BasicNoteHistoryItemA[] newArray(int size) {
            return new BasicNoteHistoryItemA[size];
        }
    };

}
