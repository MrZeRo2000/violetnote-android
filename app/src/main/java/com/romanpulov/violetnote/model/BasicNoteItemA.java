package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rpulov on 11.08.2016.
 */
public class BasicNoteItemA extends BasicCommonNoteA implements Parcelable {
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

    public void setChecked(boolean value) {
        mChecked = value;
    }

    public void updateChecked(BasicNoteItemA item) {
        this.mChecked = item.mChecked;
        this.mLastModified = item.mLastModified;
        this.mLastModifiedString = item.mLastModifiedString;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(mId);
        dest.writeLong(mLastModified);
        dest.writeString(mLastModifiedString);
        dest.writeLong(mOrderId);
        dest.writeString(mName);
        dest.writeString(mValue);
        dest.writeInt(toInt(mChecked));
    }

    private BasicNoteItemA(Parcel in) {
        mId = in.readLong();
        mLastModified = in.readLong();
        mLastModifiedString = in.readString();
        mOrderId = in.readLong();
        mName = in.readString();
        mValue = in.readString();
        mChecked = fromInt(in.readInt());
    }

    public static final Parcelable.Creator<BasicNoteItemA> CREATOR = new Parcelable.Creator<BasicNoteItemA>() {
        @Override
        public BasicNoteItemA createFromParcel(Parcel source) {
            return new BasicNoteItemA(source);
        }

        @Override
        public BasicNoteItemA[] newArray(int size) {
            return new BasicNoteItemA[size];
        }
    };

}
