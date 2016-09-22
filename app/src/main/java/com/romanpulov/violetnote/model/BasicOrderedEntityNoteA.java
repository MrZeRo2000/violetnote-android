package com.romanpulov.violetnote.model;

/**
 * Created by rpulov on 22.09.2016.
 */

public class BasicOrderedEntityNoteA extends BasicModifiableEntityNoteA {
    protected long mOrderId;

    public long getOrderId() {
        return mOrderId;
    }

    public void setOrderId(long mOrderId) {
        this.mOrderId = mOrderId;
    }
}
