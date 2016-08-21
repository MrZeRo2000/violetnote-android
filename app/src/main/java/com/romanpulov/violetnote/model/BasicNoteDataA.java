package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rpulov on 21.08.2016.
 */
public class BasicNoteDataA implements Parcelable {
    private final String mPassword;

    public String getPassword() {
        return mPassword;
    }

    private ArrayList<BasicNoteA> mNoteList;

    public ArrayList<BasicNoteA> getNoteList() {
        return mNoteList;
    }

    private BasicNoteDataA(String password) {
        mPassword = password;
    }

    public static BasicNoteDataA newInstance(String password, ArrayList<BasicNoteA> noteList) {
        BasicNoteDataA newNoteData = new BasicNoteDataA(password);
        newNoteData.mNoteList = noteList;
        return newNoteData;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPassword);
        dest.writeTypedList(mNoteList);
    }

    private BasicNoteDataA(Parcel in) {
        mPassword = in.readString();
        mNoteList = new ArrayList<>();
        in.readTypedList(mNoteList, BasicNoteA.CREATOR);
    }

    public static final Parcelable.Creator<BasicNoteDataA> CREATOR = new Parcelable.Creator<BasicNoteDataA>() {
        @Override
        public BasicNoteDataA createFromParcel(Parcel source) {
            return new BasicNoteDataA(source);
        }

        @Override
        public BasicNoteDataA[] newArray(int size) {
            return new BasicNoteDataA[size];
        }
    };

}
