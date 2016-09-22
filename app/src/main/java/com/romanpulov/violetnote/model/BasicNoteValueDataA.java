package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 22.09.2016.
 */

public class BasicNoteValueDataA implements Parcelable {
    protected BasicNoteA mNote;
    protected List<String> mValues;

    public BasicNoteA getNote() {
        return mNote;
    }

    public List<String> getValues() {
        return mValues;
    }

    private BasicNoteValueDataA() {

    }

    public BasicNoteValueDataA newInstance(BasicNoteA note, List<String> values) {
        BasicNoteValueDataA instance = new BasicNoteValueDataA();

        instance.mNote = note;
        instance.mValues = values;

        return instance;
    }

    private BasicNoteValueDataA(Parcel in) {
        mNote = BasicNoteA.CREATOR.createFromParcel(in);
        in.readStringList(mValues);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mNote, 0);
        dest.writeStringList(mValues);
    }

    public static final Parcelable.Creator<BasicNoteValueDataA> CREATOR = new Parcelable.Creator<BasicNoteValueDataA>() {
        @Override
        public BasicNoteValueDataA createFromParcel(Parcel source) {
            return new BasicNoteValueDataA(source);
        }

        @Override
        public BasicNoteValueDataA[] newArray(int size) {
            return new BasicNoteValueDataA[size];
        }
    };
}
