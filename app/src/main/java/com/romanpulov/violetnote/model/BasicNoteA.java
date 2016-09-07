package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by rpulov on 11.08.2016.
 */
public final class BasicNoteA extends BasicCommonNoteA implements Parcelable {
    private int mNoteType;
    private String mTitle;
    private boolean mIsEncrypted;
    private String mEncryptedString;

    public int getNoteType() {
        return mNoteType;
    }

    public void setNoteType(int mNoteType) {
        this.mNoteType = mNoteType;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public boolean getIsEncrypted() {
        return mIsEncrypted;
    }

    public void setIsEncrypted(boolean mIsEncrypted) {
        this.mIsEncrypted = mIsEncrypted;
    }

    public String getEncryptedString() {
        return mEncryptedString;
    }

    public void setEncryptedString(String mEncryptedString) {
        this.mEncryptedString = mEncryptedString;
    }

    private List<BasicNoteItemA> mItems = new ArrayList<>();

    public List<BasicNoteItemA> getItems() {
        return mItems;
    }

    private Set<String> mValues = new HashSet<>();

    public Set<String>  getValues() {
        return mValues;
    }

    private BasicNoteA() {

    }

    public static BasicNoteA newInstance(long id, long lastModified, String lastModifiedString, long orderId, int noteType, String title, boolean isEncrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.mId = id;
        instance.mLastModified = lastModified;
        instance.mLastModifiedString = lastModifiedString;
        instance.mOrderId = orderId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mIsEncrypted = isEncrypted;
        instance.mEncryptedString = encryptedString;

        return instance;
    }

    public static BasicNoteA newEditInstance(int noteType, String title, boolean isEncrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mIsEncrypted = isEncrypted;
        instance.mEncryptedString = encryptedString;

        return instance;
    }

    @Override
    public String toString() {
        return "{" +
                "[id=" + mId + "]," +
                "[lastModified=" + mLastModified + "]," +
                "[orderId=" + mOrderId + "]," +
                "[noteType=" + mNoteType + "]," +
                "[title=" + mTitle + "]," +
                "[isEncrypted=" + mIsEncrypted + "]," +
                "[encryptedString=" + mEncryptedString + "]" +
                "}";
    }

    private BasicNoteA(Parcel in) {
        mId = in.readLong();
        mLastModified = in.readLong();
        mLastModifiedString = in.readString();
        mOrderId = in.readLong();
        mNoteType = in.readInt();
        mTitle = in.readString();
        mIsEncrypted = fromInt(in.readInt());
        mEncryptedString = in.readString();
        in.readTypedList(mItems, BasicNoteItemA.CREATOR);

        String[] values = in.createStringArray();
        Collections.addAll(mValues, values);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeLong(mLastModified);
        dest.writeString(mLastModifiedString);
        dest.writeLong(mOrderId);
        dest.writeInt(mNoteType);
        dest.writeString(mTitle);
        dest.writeInt(toInt(mIsEncrypted));
        dest.writeString(mEncryptedString);
        dest.writeTypedList(mItems);

        dest.writeStringArray(mValues.toArray(new String[mValues.size()]));
    }

    public static final Parcelable.Creator<BasicNoteA> CREATOR = new Parcelable.Creator<BasicNoteA>() {
        @Override
        public BasicNoteA createFromParcel(Parcel source) {
            return new BasicNoteA(source);
        }

        @Override
        public BasicNoteA[] newArray(int size) {
            return new BasicNoteA[size];
        }
    };

}
