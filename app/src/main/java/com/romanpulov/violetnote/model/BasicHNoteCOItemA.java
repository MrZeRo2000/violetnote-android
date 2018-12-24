package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

/**
 * HNoteCOItem data class
 */
public final class BasicHNoteCOItemA extends BasicEntityNoteA {
    private final long mEventId;
    public long getEventId() {
        return mEventId;
    }

    private final long mNoteId;
    public long getNoteId() {
        return mNoteId;
    }

    private final String mValue;
    public String getName() {
        return mValue;
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
}
