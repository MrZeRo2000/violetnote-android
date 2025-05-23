package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.romanpulov.violetnote.model.core.BasicEntityNoteA;

/**
 * BasicNote value data
 * Created by romanpulov on 22.09.2016.
 */

public class BasicNoteValueA extends BasicEntityNoteA implements Parcelable, DisplayTitleProvider  {
    private long mNoteId;
    protected String mValue;

    public long getNoteId() {
        return mNoteId;
    }

    public void setNoteId(long mNoteId) {
        this.mNoteId = mNoteId;
    }

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

    public static BasicNoteValueA newEditInstance(long noteId, String value) {
        BasicNoteValueA instance = new BasicNoteValueA();

        instance.mNoteId = noteId;
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

    public static final Parcelable.Creator<BasicNoteValueA> CREATOR = new Parcelable.Creator<>() {
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
