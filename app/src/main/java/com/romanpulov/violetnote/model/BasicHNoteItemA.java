package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

/**
 * HNoteItem data class
 */
public final class BasicHNoteItemA extends BasicEntityNoteA {
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

    private BasicHNoteItemA(long id, long eventId, long noteItemId, String name, String value) {
        setId(id);
        mEventId = eventId;
        mNoteItemId = noteItemId;
        mName = name;
        mValue = value;
    }

    @NonNull
    public static BasicHNoteItemA newInstance(long id, long eventId, long noteItemId, String name, String value) {
        return new BasicHNoteItemA(id, eventId, noteItemId, name, value);
    }
}
