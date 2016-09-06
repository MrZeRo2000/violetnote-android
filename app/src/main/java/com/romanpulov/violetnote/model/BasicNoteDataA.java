package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rpulov on 21.08.2016.
 */
public class BasicNoteDataA implements Parcelable, PasswordProvider {
    private final String mPassword;

    @Override
    public boolean isPasswordProtected() {
        return ((mNoteList != null) && (mNoteList.size() > 0) && (mNoteList.get(0).getIsEncrypted()));
    }

    @Override
    public String getPassword() {
        return mPassword;
    }

    private ArrayList<BasicNoteA> mNoteList;

    public ArrayList<BasicNoteA> getNoteList() {
        return mNoteList;
    }

    public BasicNoteA getNote() {
        return mNoteList.get(0);
    }

    private BasicNoteDataA(String password) {
        mPassword = password;
    }

    public static BasicNoteDataA newInstance(String password, ArrayList<BasicNoteA> noteList) {
        BasicNoteDataA newNoteData = new BasicNoteDataA(password);
        newNoteData.mNoteList = noteList;
        return newNoteData;
    }

    public int getCheckedCount() {
        int result = 0;

        for (BasicNoteA note : mNoteList) {
            for (BasicNoteItemA item : note.getItems()) {
                if (item.getChecked())
                    result ++;
            }
        }

        return result;
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
