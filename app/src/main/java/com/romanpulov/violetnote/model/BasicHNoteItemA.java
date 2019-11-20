package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * HNoteItem data class
 */
public final class BasicHNoteItemA extends BasicEntityNoteA implements Parcelable {
    private final long mEventId;
    public long getEventId() {
        return mEventId;
    }

    private final long mNoteItemId;
    public long getNoteItemId() {
        return mNoteItemId;
    }

    private final String mName;
    public String getName() {
        return mName;
    }

    private final String mValue;
    public String getValue() {
        return mValue;
    }

    // from event
    private final long mEventTime;

    public long getEventTime() {
        return mEventTime;
    }

    private BasicHNoteItemA(long id, long eventId, long eventTime, long noteItemId, String name, String value) {
        setId(id);
        mEventId = eventId;
        mEventTime = eventTime;
        mNoteItemId = noteItemId;
        mName = name;
        mValue = value;
    }

    @NonNull
    public static BasicHNoteItemA newInstance(long id, long eventId, long noteItemId, String name, String value) {
        return new BasicHNoteItemA(id, eventId, 0, noteItemId, name, value);
    }

    @NonNull
    public static BasicHNoteItemA newInstanceWithEventTime(long id, long eventId, long eventTime, long noteItemId, String name, String value) {
        return new BasicHNoteItemA(id, eventId, eventTime, noteItemId, name, value);
    }

    @NonNull
    public static BasicHNoteItemA fromEventData(long eventId, @NonNull BasicNoteItemA noteItem) {
        return newInstance(
                0,
                eventId,
                noteItem.getId(),
                noteItem.getName(),
                noteItem.getValue()
        );
    }

    private BasicHNoteItemA(@NonNull Parcel in) {
        setId(in.readLong());
        mEventId = in.readLong();
        mEventTime = in.readLong();
        mNoteItemId = in.readLong();
        mName = in.readString();
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
        dest.writeLong(mEventTime);
        dest.writeLong(mNoteItemId);
        dest.writeString(mName);
        dest.writeString(mValue);
    }

    public static final Parcelable.Creator<BasicHNoteItemA> CREATOR = new Parcelable.Creator<BasicHNoteItemA>() {
        @Override
        public BasicHNoteItemA createFromParcel(Parcel source) {
            return new BasicHNoteItemA(source);
        }

        @Override
        public BasicHNoteItemA[] newArray(int size) {
            return new BasicHNoteItemA[size];
        }
    };

}
