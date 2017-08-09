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

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    @Override
    public String getDisplayTitle() {
        if ((mName == null) || (mName.isEmpty()))
            return mValue;
        else
            return mName + "/" + mValue;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean value) {
        mChecked = value;
    }

    public void updateChecked(BasicNoteItemA item) {
        this.mChecked = item.mChecked;
        this.setLastModified(item.getLastModified());
        this.setLastModifiedString(item.getLastModifiedString());
    }

    private BasicNoteItemA() {

    }

    public static BasicNoteItemA newInstance(long id, long lastModified, String lastModifiedString, long orderId, long priority, String name, String value, boolean checked) {
        BasicNoteItemA instance = new BasicNoteItemA();

        instance.setId(id);
        instance.setLastModified(lastModified);
        instance.setLastModifiedString(lastModifiedString);
        instance.setOrderId(orderId);
        instance.setPriority(priority);
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
        dest.writeLong(getId());
        dest.writeLong(getLastModified());
        dest.writeString(getLastModifiedString());
        dest.writeLong(getOrderId());
        dest.writeLong(getPriority());
        dest.writeString(mName);
        dest.writeString(mValue);
        dest.writeInt(BooleanUtils.toInt(mChecked));
    }

    private BasicNoteItemA(Parcel in) {
        setId(in.readLong());
        setLastModified(in.readLong());
        setLastModifiedString(in.readString());
        setOrderId(in.readLong());
        setPriority(in.readLong());
        mName = in.readString();
        mValue = in.readString();
        mChecked = BooleanUtils.fromInt(in.readInt());
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
