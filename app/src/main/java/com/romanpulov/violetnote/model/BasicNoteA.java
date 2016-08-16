package com.romanpulov.violetnote.model;

import java.util.List;

/**
 * Created by rpulov on 11.08.2016.
 */
public class BasicNoteA {
    private long mId;
    private long mLastModified;
    private long mOrderId;
    private int mNoteType;
    private String mTitle;
    private boolean mIsEncrypted;
    private String mEncryptedString;

    private List<BasicNoteItemA> mItems;
    private List<String> mValues;
}
