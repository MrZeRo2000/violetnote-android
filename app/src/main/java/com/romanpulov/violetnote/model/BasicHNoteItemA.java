package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import com.romanpulov.violetnote.model.core.BasicEntityNoteA;

/**
 * HNoteItem data class
 */
public final class BasicHNoteItemA extends BasicEntityNoteA implements NameValueData, Parcelable {
    private final long mEventId;
    public long getEventId() {
        return mEventId;
    }

    private final long mNoteItemId;
    public long getNoteItemId() {
        return mNoteItemId;
    }

    private String mName;

    @Override
    public String getName() {
        return mName;
    }

    @Override
    public void setName(String mName) {
        this.mName = mName;
    }

    private String mValue;

    @Override
    public String getValue() {
        return mValue;
    }

    @Override
    public void setValue(String mValue) {
        this.mValue = mValue;
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

    public static final Parcelable.Creator<BasicHNoteItemA> CREATOR = new Parcelable.Creator<>() {
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
