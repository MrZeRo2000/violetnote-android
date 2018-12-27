package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import java.util.Locale;

import static com.romanpulov.violetnote.model.BasicNoteA.NOTE_TYPE_CHECKED;
import static com.romanpulov.violetnote.model.BasicNoteA.NOTE_TYPE_NAMED;

public final class BasicNoteSummary implements Parcelable {
    private static final String CHECKED_ITEM_COUNT_TITLE_FORMAT = "%d/%d";
    private static final String NAMED_ITEM_COUNT_TITLE_FORMAT = "%d";

    private int mItemCount = 0;

    public void addItemCount() {
        mItemCount++;
    }

    public int getItemCount() {
        return mItemCount;
    }

    private int mCheckedItemCount = 0;

    public void addCheckedItemCount() {
        addCheckedItemCount(1);
    }

    public void addCheckedItemCount(int increment) {
        mCheckedItemCount += increment;
    }

    public int getCheckedItemCount(){
        return mCheckedItemCount;
    }

    private String mItemCountTitle;

    public String getItemCountTitle() {
        return mItemCountTitle;
    }

    private LongSparseArray<Long> mParams = new LongSparseArray<>();

    public LongSparseArray<Long> getParams() {
        return mParams;
    }

    public BasicNoteSummary() {

    }

    @NonNull
    public static BasicNoteSummary createEmpty() {
        return new BasicNoteSummary();
    }

    @NonNull
    public static BasicNoteSummary fromItemCounts(int itemCount, int checkedItemCount) {
        BasicNoteSummary result = new BasicNoteSummary();
        result.mItemCount = itemCount;
        result.mCheckedItemCount = checkedItemCount;

        return result;
    }

    public void appendParams(LongSparseArray<Long> paramValues) {
        for (int i = 0; i < paramValues.size(); i++) {
            long key = paramValues.keyAt(i);

            Long value = paramValues.get(key);

            if ((value != null) && (value > 0)) {
                Long param = mParams.get(key);
                if (param == null) {
                    mParams.append(key, value);
                } else {
                    mParams.put(key, mParams.get(key) + value);
                }
            }
        }
    }

    public void updateItemCountTitle(int noteType) {
        if (mItemCount > 0)
            switch (noteType) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mItemCount);
        dest.writeInt(mCheckedItemCount);
        dest.writeString(mItemCountTitle);

        dest.writeInt(mParams.size());
        long[] keys = new long[mParams.size()];
        long[] values = new long[mParams.size()];
        for (int i = 0; i < mParams.size(); i++) {
            keys[i] = mParams.keyAt(i);
            values[i] = mParams.get(keys[i]);
        }
        dest.writeLongArray(keys);
        dest.writeLongArray(values);
    }

    private BasicNoteSummary(Parcel in) {
        mItemCount = in.readInt();
        mCheckedItemCount = in.readInt();
        mItemCountTitle = in.readString();

        int paramsSize = in.readInt();
        long[] keys = new long[paramsSize];
        long[] values = new long[paramsSize];
        in.readLongArray(keys);
        in.readLongArray(values);
        for (int i = 0; i < paramsSize; i++) {
            mParams.put(keys[i], values[i]);
        }
    }

    public static final Parcelable.Creator<BasicNoteSummary> CREATOR = new Parcelable.Creator<BasicNoteSummary>() {
        @Override
        public BasicNoteSummary createFromParcel(Parcel source) {
            return new BasicNoteSummary(source);
        }

        @Override
        public BasicNoteSummary[] newArray(int size) {
            return new BasicNoteSummary[size];
        }
    };

    @Override
    public String toString() {
        return "{" +
                "[itemCount=" + mItemCount + "]," +
                "[checkedItemCount=" + mCheckedItemCount + "]," +
                "[params=" + mParams.toString() + "]" +
                "}";
    }
}
