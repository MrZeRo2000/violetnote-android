package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * BasicNote value data
 * Created by romanpulov on 22.09.2016.
 */

public class BasicNoteValueA extends BasicEntityNoteA implements Parcelable, DisplayTitleProvider  {
    protected String mValue;

    public String getValue() {
        return mValue;
    }

    public void setValue(String value) {
        mValue = value;
    }

    private BasicNoteValueA() {

    }

    public static BasicNoteValueA newInstance(long id, String value) {
        BasicNoteValueA instance = new BasicNoteValueA();

        instance.setId(id);
        instance.mValue = value;

        return instance;
    }

    public static BasicNoteValueA newEditInstance(String value) {
        BasicNoteValueA instance = new BasicNoteValueA();

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
        dest.writeString(mValue);
    }

    private BasicNoteValueA(Parcel in) {
        setId(in.readLong());
        mValue = in.readString();
    }

    public static final Parcelable.Creator<BasicNoteValueA> CREATOR = new Parcelable.Creator<BasicNoteValueA>() {
        @Override
        public BasicNoteValueA createFromParcel(Parcel source) {
            return new BasicNoteValueA(source);
        }

        @Override
        public BasicNoteValueA[] newArray(int size) {
            return new BasicNoteValueA[size];
        }
    };

    @Override
    public String getDisplayTitle() {
        return mValue;
    }
}
