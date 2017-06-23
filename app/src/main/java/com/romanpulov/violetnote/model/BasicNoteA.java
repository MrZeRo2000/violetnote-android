package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by rpulov on 11.08.2016.
 */
public final class BasicNoteA extends BasicCommonNoteA implements Parcelable {
    public static final int NOTE_TYPE_CHECKED = 0;
    public static final int NOTE_TYPE_NAMED = 1;

    private static final String CHECKED_ITEM_COUNT_TITLE_FORMAT = "%d/%d";
    private static final String NAMED_ITEM_COUNT_TITLE_FORMAT = "%d";

    private int mNoteType;
    private String mTitle;
    private boolean mEncrypted;
    private String mEncryptedString;

    //calculated
    private int mItemCount;
    private int mCheckedItemCount;
    private String mItemCountTitle;

    public int getNoteType() {
        return mNoteType;
    }

    public void setNoteType(int mNoteType) {
        this.mNoteType = mNoteType;
    }

    @Override
    public String getDisplayTitle() {
        return mTitle;
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

    private void updateItemCountTitle() {
        if (mItemCount > 0)
            switch (mNoteType) {
                case NOTE_TYPE_CHECKED:
                    mItemCountTitle = String.format(Locale.getDefault(), CHECKED_ITEM_COUNT_TITLE_FORMAT, mCheckedItemCount, mItemCount);
                    break;
                case NOTE_TYPE_NAMED:
                    mItemCountTitle = String.format(Locale.getDefault(), NAMED_ITEM_COUNT_TITLE_FORMAT, mItemCount);
                    break;
                default:
                    mItemCountTitle = null;
            }
        else
            mItemCountTitle = null;

    }

    public String getItemCountTitle() {
        return mItemCountTitle;
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

    /**
     * Creates notes without totals
     * @param id
     * @param lastModified
     * @param lastModifiedString
     * @param orderId
     * @param noteType
     * @param title
     * @param encrypted
     * @param encryptedString
     * @return new instance
     */
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

    /**
     * Creates notes with calculated fields
     * @param id
     * @param lastModified
     * @param lastModifiedString
     * @param orderId
     * @param noteType
     * @param title
     * @param encrypted
     * @param encryptedString
     * @param itemCount
     * @param checkedItemCount
     * @return new instance
     */
    public static BasicNoteA newInstanceWithTotals(long id, long lastModified, String lastModifiedString, long orderId, int noteType, String title, boolean encrypted, String encryptedString, int itemCount, int checkedItemCount) {
        BasicNoteA instance = new BasicNoteA();

        instance.mId = id;
        instance.mLastModified = lastModified;
        instance.mLastModifiedString = lastModifiedString;
        instance.mOrderId = orderId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mEncrypted = encrypted;
        instance.mEncryptedString = encryptedString;
        instance.mItemCount = itemCount;
        instance.mCheckedItemCount = checkedItemCount;
        instance.updateItemCountTitle();

        return instance;
    }

    /**
     * Creates notes for editor
     * most fields are empty
     * @param noteType
     * @param title
     * @param encrypted
     * @param encryptedString
     * @return new instance
     */
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
                "[checkedItemCount=" + mCheckedItemCount + "]" +
                "}";
    }

    private BasicNoteA(Parcel in) {
        mId = in.readLong();
        mLastModified = in.readLong();
        mLastModifiedString = in.readString();
        mOrderId = in.readLong();
        mNoteType = in.readInt();
        mTitle = in.readString();
        mEncrypted = BooleanUtils.fromInt(in.readInt());
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
        dest.writeInt(BooleanUtils.toInt(mEncrypted));
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
