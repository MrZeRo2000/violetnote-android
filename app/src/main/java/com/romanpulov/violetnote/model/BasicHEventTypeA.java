package com.romanpulov.violetnote.model;

import androidx.annotation.NonNull;

/**
 * HEventType data class
 */
public final class BasicHEventTypeA extends BasicEntityNoteA {
    public static final String EVENT_TYPE_CODE_NOTE_ITEMS = "NOTE_ITEMS";
    public static final String EVENT_TYPE_NAME_NOTE_ITEMS = "Note items";
    public static final String EVENT_TYPE_CODE_CHECKOUT = "CHECKOUT";
    public static final String EVENT_TYPE_NAME_CHECKOUT = "Checkout";


    private BasicHEventTypeA(long id) {
        setId(id);
    }

    @NonNull
    public static BasicHEventTypeA newInstance(long id) {
        return new BasicHEventTypeA(id);
    }
}
