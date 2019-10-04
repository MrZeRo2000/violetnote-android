package com.romanpulov.violetnote.model;

import android.support.annotation.NonNull;

/**
 * Data class for note item events
 */
public final class BasicHNoteItemEventA {
    public final long noteItemId;
    public final long eventTime;
    public final String name;
    public final String value;

    private BasicHNoteItemEventA(long noteItemId, long eventTime, String name, String value) {
        this.noteItemId = noteItemId;
        this.eventTime = eventTime;
        this.name = name;
        this.value = value;
    }

    @NonNull
    public static BasicHNoteItemEventA newInstance(long noteItemId, long eventTime, String name, String value) {
        return new BasicHNoteItemEventA(noteItemId, eventTime, name, value);
    }
}
