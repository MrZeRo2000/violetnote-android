package com.romanpulov.violetnote.model.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public final class BasicNoteItemParamValueA implements Parcelable {
    public final long noteItemParamTypeId;
    public final BasicParamValueA paramValue;

    public BasicNoteItemParamValueA(long noteItemParamTypeId, BasicParamValueA paramValue) {
        this.noteItemParamTypeId = noteItemParamTypeId;
        this.paramValue = paramValue;
    }

    private BasicNoteItemParamValueA(@NonNull Parcel in) {
        noteItemParamTypeId = in.readLong();
        paramValue = in.readParcelable(BasicParamValueA.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(noteItemParamTypeId);
        dest.writeParcelable(paramValue, 0);
    }

    public static final Creator<BasicNoteItemParamValueA> CREATOR = new Creator<BasicNoteItemParamValueA>(){

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
