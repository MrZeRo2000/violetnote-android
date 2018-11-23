package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * BasicNote data class
 * Created by rpulov on 21.08.2016.
 */
public class BasicNoteDataA implements Parcelable, PasswordProvider {
    private String mPassword;

    @Override
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    private ArrayList<BasicNoteA> mNoteList;

    public ArrayList<BasicNoteA> getNoteList() {
        return mNoteList;
    }

    private ArrayList<BasicNoteA> mRelatedNoteList;

    public ArrayList<BasicNoteA> getRelatedNoteList() {
        return mRelatedNoteList;
    }

    public BasicNoteA getNote() {
        if ((mNoteList != null) && (mNoteList.size() > 0))
            return mNoteList.get(0);
        else
            return null;
    }

    private BasicNoteDataA(String password) {
        mPassword = password;
    }

    private static BasicNoteDataA newInstance(String password, ArrayList<BasicNoteA> noteList) {
        BasicNoteDataA newNoteData = new BasicNoteDataA(password);
        newNoteData.mNoteList = noteList;
        return newNoteData;
    }

    public static BasicNoteDataA newInstance(String password, ArrayList<BasicNoteA> noteList, ArrayList<BasicNoteA> relatedNoteList) {
        BasicNoteDataA newNoteData = new BasicNoteDataA(password);
        newNoteData.mNoteList = noteList;
        newNoteData.mRelatedNoteList = relatedNoteList;
        return newNoteData;
    }

    public int getCheckedCount() {
        int result = 0;

        for (BasicNoteA note : mNoteList) {
            for (BasicNoteItemA item : note.getItems()) {
                if (item.isChecked())
                    result ++;
            }
        }

        return result;
    }

    public long getCheckedPrice() {
        long result = 0;

        for (BasicNoteA note : mNoteList) {
            for (BasicNoteItemA item : note.getItems()) {
                if (item.isChecked())
                    result += item.getParamPrice();
            }
        }

        return result;
    }

    public long getTotalPrice() {
        long result = 0;

        for (BasicNoteA note : mNoteList) {
            for (BasicNoteItemA item : note.getItems()) {
                result += item.getParamPrice();
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
        dest.writeTypedList(mRelatedNoteList);
    }

    private BasicNoteDataA(Parcel in) {
        mPassword = in.readString();
        mNoteList = new ArrayList<>();
        mRelatedNoteList = new ArrayList<>();
        in.readTypedList(mNoteList, BasicNoteA.CREATOR);
        in.readTypedList(mRelatedNoteList, BasicNoteA.CREATOR);
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
