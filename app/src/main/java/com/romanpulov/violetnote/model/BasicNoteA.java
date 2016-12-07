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
    public static final int NOTE_TYPE_CHECKED = 0;
    public static final int NOTE_TYPE_NAMED = 1;

    private int mNoteType;
    private String mTitle;
    private boolean mEncrypted;
    private String mEncryptedString;

    //calculated
    private int mItemCount;
    private int mCheckedItemCount;

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

    public boolean isEncrypted() {
        return mEncrypted;
    }

    public void setIsEncrypted(boolean encrypted) {
        this.mEncrypted = encrypted;
    }

    public String getEncryptedString() {
        return mEncryptedString;
    }

    public void setEncryptedString(String mEncryptedString) {
        this.mEncryptedString = mEncryptedString;
    }

    public int getItemCount() {
        return mItemCount;
    }

    public void setItemCount(int value) {
        mItemCount = value;
    }

    public int getCheckedItemCount() {
        return mCheckedItemCount;
    }

    public void setCheckedItemCount(int value) {
        mCheckedItemCount = value;
    }

    public void addCheckedItemCount(int value) {
        mCheckedItemCount += value;
    }

    private List<BasicNoteItemA> mItems = new ArrayList<>();

    public List<BasicNoteItemA> getItems() {
        return mItems;
    }

    private Set<String> mValues = new HashSet<>();

    public Set<String>  getValues() {
        return mValues;
    }

    private List<BasicNoteHistoryItemA> mHistoryItems = new ArrayList<>();

    public List<BasicNoteHistoryItemA> getHistoryItems() {
        return mHistoryItems;
    }

    private BasicNoteA() {

    }

    public static BasicNoteA newInstance(long id, long lastModified, String lastModifiedString, long orderId, int noteType, String title, boolean encrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.mId = id;
        instance.mLastModified = lastModified;
        instance.mLastModifiedString = lastModifiedString;
        instance.mOrderId = orderId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mEncrypted = encrypted;
        instance.mEncryptedString = encryptedString;

        return instance;
    }

    public static BasicNoteA newEditInstance(int noteType, String title, boolean encrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mEncrypted = encrypted;
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
                "[encrypted=" + mEncrypted + "]," +
                "[encryptedString=" + mEncryptedString + "]" +
                "[itemCount=" + mItemCount + "]" +
                "[checkedItemCount=]" + mCheckedItemCount + "]" +
                "}";
    }

    private BasicNoteA(Parcel in) {
        mId = in.readLong();
        mLastModified = in.readLong();
        mLastModifiedString = in.readString();
        mOrderId = in.readLong();
        mNoteType = in.readInt();
        mTitle = in.readString();
        mEncrypted = fromInt(in.readInt());
        mEncryptedString = in.readString();
        mItemCount = in.readInt();
        mCheckedItemCount = in.readInt();
        in.readTypedList(mItems, BasicNoteItemA.CREATOR);

        String[] values = in.createStringArray();
        Collections.addAll(mValues, values);

        in.readTypedList(mHistoryItems, BasicNoteHistoryItemA.CREATOR);
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
        dest.writeInt(toInt(mEncrypted));
        dest.writeString(mEncryptedString);
        dest.writeInt(mItemCount);
        dest.writeInt(mCheckedItemCount);
        dest.writeTypedList(mItems);
        dest.writeStringArray(mValues.toArray(new String[mValues.size()]));
        dest.writeTypedList(mHistoryItems);
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
