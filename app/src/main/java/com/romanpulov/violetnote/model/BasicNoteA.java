package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 11.08.2016.
 */
public final class BasicNoteA implements Parcelable {
    public static boolean fromInt(int value) {
        switch (value) {
            case 0: return false;
            case 1: return true;
            default: throw new IllegalArgumentException();
        }
    }
    public static int toInt(boolean value){
        return value ? 1 : 0;
    }

    private long mId;
    private long mLastModified;
    private String mLastModifiedString;
    private long mOrderId;
    private int mNoteType;
    private String mTitle;
    private boolean mIsEncrypted;
    private String mEncryptedString;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public long getLastModified() {
        return mLastModified;
    }

    public void setLastModified(long mLastModified) {
        this.mLastModified = mLastModified;
    }

    public String getLastModifiedString() {
        return mLastModifiedString;
    }

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long mOrderId) {
        this.mOrderId = mOrderId;
    }

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

    private List<String> mValues = new ArrayList<>();

    public List<String> getValues() {
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
