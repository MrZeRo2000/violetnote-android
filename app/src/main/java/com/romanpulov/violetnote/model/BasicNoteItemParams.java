package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Data class for param values accessible by typ3
 */
public final class BasicNoteItemParams implements Parcelable, Iterable<BasicParamValueA> {
    public final LongSparseArray<BasicParamValueA> paramValues;

    private BasicNoteItemParams(LongSparseArray<BasicParamValueA> noteItemParams) {
        this.paramValues = noteItemParams;
    }

    @NonNull
    public static BasicNoteItemParams createEmpty() {
        return new BasicNoteItemParams(new LongSparseArray<BasicParamValueA>());
    }

    private class BasicParamValueIterator implements Iterator<BasicParamValueA> {

        private int position = 0;

        @Override
        public boolean hasNext() {
            return position < paramValues.size() - 1;
        }

        @Override
        public BasicParamValueA next() {
            return paramValues.get(paramValues.keyAt(position++));
        }
    }

    @Override
    public Iterator<BasicParamValueA> iterator() {
        return new BasicParamValueIterator();
    }

    public final static class BasicNoteItemParamValueA implements Parcelable {
        private final long noteItemParamTypeId;
        private final BasicParamValueA paramValue;

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

    public static LongSparseArray<BasicParamValueA> paramValuesFromList(@NonNull List<BasicNoteItemParamValueA> noteItemParamValues) {
        LongSparseArray<BasicParamValueA> result = new LongSparseArray<>();
        for (BasicNoteItemParamValueA value : noteItemParamValues) {
            result.append(value.noteItemParamTypeId, value.paramValue);
        }

        return result;
    }

    @NonNull
    public static BasicNoteItemParams fromList(@NonNull List<BasicNoteItemParamValueA> noteItemParamValues) {
        return new BasicNoteItemParams(paramValuesFromList(noteItemParamValues));
    }

    @NonNull
    public static BasicNoteItemParams fromValue(long key, BasicParamValueA value) {
        LongSparseArray<BasicParamValueA> paramValues = new LongSparseArray<>();
        paramValues.append(key, value);
        return new BasicNoteItemParams(paramValues);
    }

    @NonNull
    private static List<BasicNoteItemParamValueA> paramValuesToList(@NonNull LongSparseArray<BasicParamValueA> paramValues) {
        List<BasicNoteItemParamValueA> result = new ArrayList<>();

        for (int i = 0; i < paramValues.size(); i++) {
            long key = paramValues.keyAt(i);
            result.add(new BasicNoteItemParamValueA(key, paramValues.get(key)));
        }

        return result;
    }

    public void append(long key, BasicParamValueA value) {
        paramValues.append(key, value);
    }

    public void put(long key, BasicParamValueA value) {
        paramValues.put(key, value);
    }

    public void putLong(long key, long value) {
        paramValues.put(key, BasicParamValueA.fromLong(value));
    }

    public void delete(long key) {
        paramValues.delete(key);
    }

    public long keyAt(int index) {
        return paramValues.keyAt(index);
    }

    public BasicParamValueA get(long key) {
        return paramValues.get(key);
    }

    public long getLong(long key) {
        return paramValues.get(key).vInt;
    }

    public long size() {
        return paramValues.size();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(paramValuesToList(paramValues));
    }

    private BasicNoteItemParams(Parcel in) {
        List<BasicNoteItemParamValueA> list = new ArrayList<>();
        in.readTypedList(list, BasicNoteItemParamValueA.CREATOR);
        paramValues = paramValuesFromList(list);
    }

    public static final Parcelable.Creator<BasicNoteItemParams> CREATOR = new Parcelable.Creator<BasicNoteItemParams>() {
        @Override
        public BasicNoteItemParams createFromParcel(Parcel source) {
            return new BasicNoteItemParams(source);
        }

        @Override
        public BasicNoteItemParams[] newArray(int size) {
            return new BasicNoteItemParams[size];
        }
    };

}
