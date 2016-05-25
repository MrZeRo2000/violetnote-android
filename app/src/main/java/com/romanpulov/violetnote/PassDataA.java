package com.romanpulov.violetnote;

import android.os.Parcel;
import android.os.Parcelable;

import com.romanpulov.violetnotecore.Model.PassData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 26.04.2016.
 */
public class PassDataA implements Parcelable {
    private static final String PASSWORD_NAME="Password";
    private static final String CATEGORY_DATA_NAME="CategoryData";
    private static final String NOTE_DATA_NAME="NoteData";

    private String mPassword;

    public String getPassword() {
        return mPassword;
    }

    private List<PassCategoryA> mPassCategoryDataA;

    public List<PassCategoryA> getPassCategoryData() {
        return mPassCategoryDataA;
    }

    private List<PassNoteA> mPassNoteDataA;

    public List<PassNoteA> getPassNoteData() {
        return mPassNoteDataA;
    }

    public List<PassNoteA> getPassNoteData(PassCategoryA category) {
        List<PassNoteA> passNoteData = new ArrayList<>();
        for (PassNoteA p : mPassNoteDataA) {
            if (p.getCategory().equals(category)) {
                passNoteData.add(p);
            }
        }

        return passNoteData;
    }

    private PassDataA() {

    }

    public static PassDataA newInstance(String password, PassData passData) {
        PassDataReader reader = new PassDataReader(passData);
        reader.readCategoryData();
        reader.readNoteData();

        PassDataA newPassDataA = new PassDataA();
        newPassDataA.mPassword = password;
        newPassDataA.mPassCategoryDataA = reader.getPassCategoryDataA();
        newPassDataA.mPassNoteDataA = reader.getPassNoteDataA();

        return newPassDataA;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPassword);
        dest.writeTypedList(mPassCategoryDataA);
        dest.writeTypedList(mPassNoteDataA);
    }

    private PassDataA(Parcel in) {
        mPassword = in.readString();
        mPassCategoryDataA = new ArrayList<>();
        in.readTypedList(mPassCategoryDataA, PassCategoryA.CREATOR);
        mPassNoteDataA = new ArrayList<>();
        in.readTypedList(mPassNoteDataA, PassNoteA.CREATOR);
    }

    public static final Parcelable.Creator<PassDataA> CREATOR = new Parcelable.Creator<PassDataA>() {
        @Override
        public PassDataA createFromParcel(Parcel source) {
            return new PassDataA(source);
        }

        @Override
        public PassDataA[] newArray(int size) {
            return new PassDataA[size];
        }
    };
}
