package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicNote value data
 * Created by rpulov on 22.09.2016.
 */

public class BasicNoteValueDataA implements Parcelable {
    protected BasicNoteA mNote;
    protected List<BasicNoteValueA> mValues;

    public BasicNoteA getNote() {
        return mNote;
    }

    public List<BasicNoteValueA> getValues() {
        return mValues;
    }
    public void setValues(List<BasicNoteValueA> value) {
        mValues = value;
    }
    public int indexOf(BasicNoteValueA value) {
        int result = -1;

        int index = 0;
        for (BasicNoteValueA item: mValues) {
            if (item.getValue().equals(value.getValue())) {
                result = index;
                break;
            }
            index++;
        }

        return result;
    }

    private BasicNoteValueDataA() {

    }

    public static BasicNoteValueDataA newInstance(BasicNoteA note, List<BasicNoteValueA> values) {
        BasicNoteValueDataA instance = new BasicNoteValueDataA();

        instance.mNote = note;
        instance.mValues = values;

        return instance;
    }

    private BasicNoteValueDataA(Parcel in) {
        //mNote = BasicNoteA.CREATOR.createFromParcel(in);
        mNote = in.readParcelable(BasicNoteA.class.getClassLoader());
        mValues = new ArrayList<>();
        in.readTypedList(mValues, BasicNoteValueA.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mNote, 0);
        dest.writeTypedList(mValues);
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
