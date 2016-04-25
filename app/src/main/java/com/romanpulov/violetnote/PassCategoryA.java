package com.romanpulov.violetnote;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by rpulov on 25.04.2016.
 */
public class PassCategoryA implements Parcelable {
    private String mCategoryName;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCategoryName);
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
    }

    public PassCategoryA(String categoryName) {
        this.mCategoryName = categoryName;
    }
}
