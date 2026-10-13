package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import java.util.List;

/**
 * PassNote data class
 * Created by rpulov on 26.04.2016.
 */
public class PassNoteA implements Parcelable {
    private PassCategoryA mCategory;

    public void setCategory(PassCategoryA category) {
        mCategory = category;
        if (mCategory != null)
            mCategoryName = category.getCategoryName();
        else
            mCategoryName = null;
    }

    private String mCategoryName;

    public String getCategoryName() {
        return mCategoryName;
    }

    public static class AttrItem implements Parcelable {
        public final String mName;
        public final String mValue;

        public AttrItem(String name, String value) {
            mName = name;
            mValue = value;
        }

        // Read constructor
        protected AttrItem(Parcel in) {
            mName = in.readString();
            mValue = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(@NonNull Parcel parcel, int flags) {
            parcel.writeString(mName);
            parcel.writeString(mValue);
        }

        public static final Creator<AttrItem> CREATOR = new Creator<>() {
            @Override
            public AttrItem createFromParcel(Parcel in) {
                return new AttrItem(in);
            }

            @Override
            public AttrItem[] newArray(int size) {
                return new AttrItem[size];
            }
        };
    }

    public PassCategoryA getCategory() {
        return mCategory;
    }

    private final List<AttrItem> mNoteAttrs;

    public List<AttrItem> getNoteAttrs() {
        return mNoteAttrs;
    }

    public PassNoteA(PassCategoryA category, List<AttrItem> noteAttrs) {
        setCategory(category);
        mNoteAttrs = noteAttrs;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCategoryName);
        dest.writeTypedList(mNoteAttrs);
    }

    public static final Parcelable.Creator<PassNoteA> CREATOR = new Parcelable.Creator<>() {
        public PassNoteA createFromParcel(Parcel in) {
            return new PassNoteA(in);
        }

        public PassNoteA[] newArray(int size) {
            return new PassNoteA[size];
        }
    };

    private PassNoteA(Parcel in) {
        mCategoryName = in.readString();
        mNoteAttrs = in.createTypedArrayList(AttrItem.CREATOR);
    }
}
