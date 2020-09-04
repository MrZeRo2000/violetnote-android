package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

/**
 * PassCategory data
 * Created by rpulov on 25.04.2016.
 */
public class PassCategoryA implements Parcelable {
    private final String mCategoryName;

    public String getCategoryName() {
        return mCategoryName;
    }

    private int mNotesCount = 0;

    public int getNotesCount() {
        return mNotesCount;
    }

    public void setNotesCount(int notesCount) {
        mNotesCount = notesCount;
    }

    @Override
    @NonNull
    public String toString() {
        return "{[CategoryName=" + mCategoryName + "], [NotesCount=" + mNotesCount + "]}";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCategoryName);
        dest.writeInt(mNotesCount);
    }

    public static final Parcelable.Creator<PassCategoryA> CREATOR
            = new Parcelable.Creator<PassCategoryA>() {
        public PassCategoryA createFromParcel(Parcel in) {
            return new PassCategoryA(in);
        }

        public PassCategoryA[] newArray(int size) {
            return new PassCategoryA[size];
        }
    };

    private PassCategoryA(Parcel in) {
        mCategoryName = in.readString();
        mNotesCount = in.readInt();
    }

    public PassCategoryA(String categoryName) {
        mCategoryName = categoryName;
    }
}
