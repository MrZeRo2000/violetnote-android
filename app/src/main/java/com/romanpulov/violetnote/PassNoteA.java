package com.romanpulov.violetnote;

import android.os.Parcel;
import android.os.Parcelable;

import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by rpulov on 26.04.2016.
 */
public class PassNoteA implements Parcelable {
    private final static int ATTR_COUNT = 7;
    private final static String ATTR_SYSTEM = "system";

    private PassCategoryA mCategory;
    private String mCategoryName;

    public PassCategoryA getCategory() {
        return mCategory;
    }

    public void setCategory(PassCategoryA category) {
        mCategory = category;
    }

    public String getCategoryName() {
        return mCategory == null ? mCategoryName : mCategory.getCategoryName();
    }

    public String getSystem() {
        return mNoteAttr.get(ATTR_SYSTEM);
    }

    private final Map<String, String> mNoteAttr;

    public PassNoteA(PassCategoryA category, Map<String, String> noteAttr) {
        mCategory = category;
        mNoteAttr = noteAttr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for (String s : mNoteAttr.keySet()) {
            dest.writeString(s);
            dest.writeString(mNoteAttr.get(s));
        }
    }

    public static final Parcelable.Creator<PassNoteA> CREATOR
            = new Parcelable.Creator<PassNoteA>() {
        public PassNoteA createFromParcel(Parcel in) {
            return new PassNoteA(in);
        }

        public PassNoteA[] newArray(int size) {
            return new PassNoteA[size];
        }
    };

    private PassNoteA(Parcel in) {
        mNoteAttr = new LinkedHashMap<>();
        for (int i = 0; i < ATTR_COUNT; i ++) {
            mNoteAttr.put(in.readString(), in.readString());
        }
    }

    public Map<String, String> getNoteAttr() {
        return mNoteAttr;
    }
}
