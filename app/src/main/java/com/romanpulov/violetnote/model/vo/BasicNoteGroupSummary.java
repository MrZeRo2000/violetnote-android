package com.romanpulov.violetnote.model.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class BasicNoteGroupSummary implements Parcelable {

    private long mNoteCount;

    public long getNoteCount() {
        return mNoteCount;
    }

    private long mNoteItemCheckedCount;

    public long getNoteItemCheckedCount() {
        return mNoteItemCheckedCount;
    }

    private long mNoteItemUncheckedCount;

    public long getNoteItemUncheckedCount() {
        return mNoteItemUncheckedCount;
    }

    private BasicNoteGroupSummary() {

    }

    private BasicNoteGroupSummary(long noteCount, long noteItemCheckedCount, long noteItemUncheckedCount) {
        this.mNoteCount = noteCount;
        this.mNoteItemCheckedCount = noteItemCheckedCount;
        this.mNoteItemUncheckedCount = noteItemUncheckedCount;
    }

    @NonNull
    public static BasicNoteGroupSummary newInstance(long noteCount, long noteItemCheckedCount, long noteItemUncheckedCount) {
        return new BasicNoteGroupSummary(noteCount, noteItemCheckedCount, noteItemUncheckedCount);
    }

    public static BasicNoteGroupSummary createEmpty() {
        return new BasicNoteGroupSummary();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(mNoteCount);
        dest.writeLong(mNoteItemCheckedCount);
        dest.writeLong(mNoteItemUncheckedCount);
    }

    private BasicNoteGroupSummary(@NonNull Parcel in) {
        mNoteCount = in.readLong();
        mNoteItemCheckedCount = in.readLong();
        mNoteItemUncheckedCount = in.readLong();
    }

    public static final Parcelable.Creator<BasicNoteGroupSummary> CREATOR = new Parcelable.Creator<BasicNoteGroupSummary>() {
        @Override
        public BasicNoteGroupSummary createFromParcel(@NonNull Parcel source) {
            return new BasicNoteGroupSummary(source);
        }

        @Override
        public BasicNoteGroupSummary[] newArray(int size) {
            return new BasicNoteGroupSummary[size];
        }
    };

    @Override
    public @NonNull String toString() {
        return "{" +
                "[noteCount=" + mNoteCount + "]," +
                "[noteItemCheckedCount=" + mNoteItemCheckedCount + "]," +
                "[noteItemUncheckedCount=" + mNoteItemUncheckedCount + "]" +
                "}";
    }
}
