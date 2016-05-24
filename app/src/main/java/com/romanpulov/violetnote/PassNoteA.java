package com.romanpulov.violetnote;

import android.os.Parcel;
import android.os.Parcelable;

import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by rpulov on 26.04.2016.
 */
public class PassNoteA implements Parcelable {
    private final static int ATTR_COUNT = 6;

    private PassCategoryA mCategory;
    private String mCategoryName;

    public static class AttrItem {
        final String mName;
        final String mValue;

        public AttrItem(String name, String value) {
            mName = name;
            mValue = value;
        }
    }

    public PassCategoryA getCategory() {
        return mCategory;
    }

    public void setCategory(PassCategoryA category) {
        mCategory = category;
    }

    public String getCategoryName() {
        return mCategory == null ? mCategoryName : mCategory.getCategoryName();
    }

    public String getAttrId(int id) {
        int i = 1;
        for (String s : mNoteAttr.keySet()) {
            if (i++ == id) {
                return mNoteAttr.get(s);
            }
        }
        return null;
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
            String name = in.readString();
            String value = in.readString();
            mNoteAttr.put(name, value);
        }
    }

    public Map<String, String> getNoteAttr() {
        return mNoteAttr;
    }

    public List<AttrItem> getNoteAttrList() {
        List<AttrItem> result = new ArrayList<>(mNoteAttr.size());
        for (String s : mNoteAttr.keySet()) {
            result.add(new AttrItem(s, mNoteAttr.get(s)));
        }
        return result;
    }
}
