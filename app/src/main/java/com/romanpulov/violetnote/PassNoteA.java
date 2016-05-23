package com.romanpulov.violetnote;

import android.os.Parcel;
import android.os.Parcelable;

import com.romanpulov.violetnotecore.Model.PassNote;

import java.util.Map;

/**
 * Created by rpulov on 26.04.2016.
 */
public class PassNoteA implements Parcelable {
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

    private final String mSystem;

    public String getSystem() {
        return mSystem;
    }

    private final String mUser;

    public String getUser() {
        return mUser;
    }

    private final String mPassword;

    public String getPassword() {
        return mPassword;
    }

    private final String mComments;

    public String getComments() {
        return mComments;
    }

    private final String mCustom;

    public String getCustom() {
        return mCustom;
    }

    private final String mInfo;

    public String getInfo() {
        return mInfo;
    }

    private Map<String, String> mNoteAttr;

    public PassNoteA(PassCategoryA category, String system, String user, String password, String comments, String custom, String info) {
        mCategory = category;
        mSystem = system;
        mUser = user;
        mPassword = password;
        mComments = comments;
        mCustom = custom;
        mInfo = info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mCategoryName);
        dest.writeString(mSystem);
        dest.writeString(mUser);
        dest.writeString(mPassword);
        dest.writeString(mComments);
        dest.writeString(mCustom);
        dest.writeString(mInfo);
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
        mCategoryName = in.readString();
        mSystem = in.readString();
        mUser = in.readString();
        mPassword = in.readString();
        mComments = in.readString();
        mCustom = in.readString();
        mInfo = in.readString();
    }

    public void setNoteAttr(Map<String, String> value) {
        mNoteAttr = value;
    }

    public Map<String, String> getNoteAttr() {
        return mNoteAttr;
    }
}
