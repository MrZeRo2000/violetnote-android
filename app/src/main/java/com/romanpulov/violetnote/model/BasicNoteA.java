package com.romanpulov.violetnote.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rpulov on 11.08.2016.
 */
public final class BasicNoteA {
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

    public BasicNoteA() {

    }

    public static BasicNoteA newInstance(long id, long lastModified, long orderId, int noteType, String title, boolean isEncrypted, String encryptedString) {
        BasicNoteA instance = new BasicNoteA();

        instance.mId = id;
        instance.mLastModified = lastModified;
        instance.mOrderId = orderId;
        instance.mNoteType = noteType;
        instance.mTitle = title;
        instance.mIsEncrypted = isEncrypted;
        instance.mEncryptedString = encryptedString;

        return instance;
    }
}
