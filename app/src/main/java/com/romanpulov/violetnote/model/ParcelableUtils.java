package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Utility class for parcelable objects
 */
public final class ParcelableUtils {

    @NonNull
    public static <T extends Parcelable> T duplicateParcelableObject(@NonNull T source, @NonNull Parcelable.Creator<T> CREATOR) {
        Parcel parcel = Parcel.obtain();
        source.writeToParcel(parcel, 0);

        parcel.setDataPosition(0);

        return CREATOR.createFromParcel(parcel);
    }
}
