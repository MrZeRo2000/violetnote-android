package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Data class for pure param value
 */
public final class BasicParamValueA implements Parcelable {
    public final long vInt;
    public final String vText;

    private BasicParamValueA(long vInt, String vText) {
        this.vInt = vInt;
        this.vText = vText;
    }

    @NonNull
    public static BasicParamValueA newInstance(long vInt, String vText) {
        return new BasicParamValueA(vInt, vText);
    }

    @NonNull
    public static BasicParamValueA fromLong(long vInt) {
        return newInstance(vInt, null);
    }

    @NonNull
    public static BasicParamValueA fromString(String vText) {
        return newInstance(0, vText);
    }

    private BasicParamValueA(@NonNull Parcel in) {
        vInt = in.readLong();
        vText = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(vInt);
        dest.writeString(vText);
    }

    public static final Parcelable.Creator<BasicParamValueA> CREATOR = new Parcelable.Creator<BasicParamValueA>(){

        @Override
        public BasicParamValueA createFromParcel(Parcel source) {
            return new BasicParamValueA(source);
        }

        @Override
        public BasicParamValueA[] newArray(int size) {
            return new BasicParamValueA[size];
        }
    };

}
