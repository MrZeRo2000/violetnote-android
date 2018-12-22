package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Data class for param value
 */
public final class BasicNoteItemParamValueA implements Parcelable {
    public final long noteItemParamTypeId;
    public final long vInt;
    public final String vText;

    public BasicNoteItemParamValueA(long noteItemParamTypeId, long vInt, String vText) {
        this.noteItemParamTypeId = noteItemParamTypeId;
        this.vInt = vInt;
        this.vText = vText;
    }

    private BasicNoteItemParamValueA(@NonNull Parcel in) {
        noteItemParamTypeId = in.readLong();
        vInt = in.readLong();
        vText = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(noteItemParamTypeId);
        dest.writeLong(vInt);
        dest.writeString(vText);
    }

    public static final Parcelable.Creator<BasicNoteItemParamValueA> CREATOR = new Parcelable.Creator<BasicNoteItemParamValueA>(){

        @Override
        public BasicNoteItemParamValueA createFromParcel(Parcel source) {
            return new BasicNoteItemParamValueA(source);
        }

        @Override
        public BasicNoteItemParamValueA[] newArray(int size) {
            return new BasicNoteItemParamValueA[size];
        }
    };
}
