package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.List;

/**
 * HNoteCOItem data class
 */
public final class BasicHNoteCOItemA extends BasicEntityNoteA implements DisplayTitleProvider, Parcelable {
    private final long mEventId;
    public long getEventId() {
        return mEventId;
    }

    private final long mNoteId;
    public long getNoteId() {
        return mNoteId;
    }

    private final String mValue;
    public String getValue() {
        return mValue;
    }

    private final BasicNoteItemParams mNoteItemParams = BasicNoteItemParams.createEmpty();
    public BasicNoteItemParams getNoteItemParams() {
        return mNoteItemParams;
    }

    private BasicHNoteCOItemA(long id, long eventId, long noteId, String value) {
        setId(id);
        mEventId = eventId;
        mNoteId = noteId;
        mValue = value;
    }

    @NonNull
    public static BasicHNoteCOItemA newInstance(long id, long eventId, long noteId, String value) {
        return new BasicHNoteCOItemA(id, eventId, noteId, value);
    }

    @NonNull
    public static BasicHNoteCOItemA fromEventData(long eventId, @NonNull BasicNoteA note, @NonNull BasicNoteItemA noteItem) {
        return newInstance(
                0,
                eventId,
                note.getId(),
                noteItem.getValue()
        );
    }

    public static void fillArrayFromList(@NonNull LongSparseArray<List<BasicHNoteCOItemA>> hNoteCOItemArray, @NonNull List<BasicHNoteCOItemA> hNoteCOItemList) {
        hNoteCOItemArray.clear();
        for (BasicHNoteCOItemA hNoteCOItem: hNoteCOItemList) {
            List<BasicHNoteCOItemA> hItems = hNoteCOItemArray.get(hNoteCOItem.getEventId());
            if (hItems == null) {
                hItems = new ArrayList<>();
                hNoteCOItemArray.append(hNoteCOItem.getEventId(), hItems);
            }
            hItems.add(hNoteCOItem);
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "{" +
                "[id=" + getId() + "]," +
                "[eventId=" + mEventId + "]," +
                "[noteId=" + mNoteId + "]," +
                "[value=" + mValue + "]" +
                "}";
    }

    @Override
    public String getDisplayTitle() {
        return mValue;
    }

    private BasicHNoteCOItemA(@NonNull Parcel in) {
        setId(in.readLong());
        mEventId = in.readLong();
        mNoteId = in.readLong();
        mValue = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(mEventId);
        dest.writeLong(mNoteId);
        dest.writeString(mValue);
    }

    public static final Parcelable.Creator<BasicHNoteCOItemA> CREATOR = new Parcelable.Creator<BasicHNoteCOItemA>() {
        @Override
        public BasicHNoteCOItemA createFromParcel(Parcel source) {
            return new BasicHNoteCOItemA(source);
        }

        @Override
        public BasicHNoteCOItemA[] newArray(int size) {
            return new BasicHNoteCOItemA[size];
        }
    };

}
