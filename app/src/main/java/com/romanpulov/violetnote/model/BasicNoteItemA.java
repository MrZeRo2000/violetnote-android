package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.provider.BasicNoteItemDBManagementProvider;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * BasicNote item data
 * Created by rpulov on 11.08.2016.
 */
public class BasicNoteItemA extends BasicCommonNoteA implements Parcelable {
    private long mNoteId;
    private String mName;
    private String mValue;
    private boolean mChecked;
    private BasicNoteItemParams mNoteItemParams = BasicNoteItemParams.createEmpty();

    public long getNoteId() {
        return mNoteId;
    }

    public void setNoteId(long value) {
        mNoteId = value;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getValue() {
        return mValue;
    }

    public String getValueWithFloatParams(long noteItemParamTypeId) {
        return InputParser.composeFloatParams(mValue, mNoteItemParams.getLong(noteItemParamTypeId));
    }

    public String getFloatParamDisplayValue(long noteItemParamTypeId) {
        return InputParser.getFloatDisplayValue(mNoteItemParams.getLong(noteItemParamTypeId));
    }

    public void setValue(String value) {
        mValue = value;
    }

    private void setFloatParams(long noteItemParamTypeId, @NonNull InputParser.FloatParamsResult floatParams) {
        mValue = floatParams.getText();
        if (floatParams.hasValue())
            mNoteItemParams.putLong(noteItemParamTypeId, floatParams.getLongValue());
        else
            mNoteItemParams.delete(noteItemParamTypeId);
    }

    public void setValueWithParams(long noteItemParamTypeId, String value) {
        setFloatParams(noteItemParamTypeId, InputParser.parseFloatParams(value));
    }

    public long getParamLong(long noteItemParamTypeId) {
        BasicParamValueA paramValue = mNoteItemParams.get(noteItemParamTypeId);
        return paramValue == null ? 0L : paramValue.vInt;
    }

    public void setParamLong(long noteItemParamTypeId, long paramValue) {
        if (paramValue == 0)
            mNoteItemParams.delete(noteItemParamTypeId);
        else
            mNoteItemParams.putLong(noteItemParamTypeId, paramValue);
    }

    public BasicNoteItemParams getNoteItemParams() {
        return mNoteItemParams;
    }

    public void setNoteItemParams(BasicNoteItemParams params) {
        if (params == null) {
            mNoteItemParams = BasicNoteItemParams.createEmpty();
        } else {
            mNoteItemParams = params;
        }
    }

    public static <T extends BasicNoteItemA> List<T> getCheckedBasicNoteItems(List<T> items) {
        List<T> result = new ArrayList<>();

        for (T item: items) {
            if (item.isChecked()) {
                result.add(item);
            }
        }

        return result;
    }

    public static <T extends BasicNoteItemA> List<String> getBasicNoteItemValues(Collection<T> items) {
        List<String> result = new ArrayList<>();

        for (T item: items) {
            result.add(item.getValue());
        }

        return result;
    }


    @Override
    public String getDisplayTitle() {
        if ((mName == null) || (mName.isEmpty()))
            return mValue;
        else
            return mName + "/" + mValue;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void updateChecked(BasicNoteItemA item) {
        this.mChecked = item.mChecked;
        this.setLastModified(item.getLastModified());
        this.setLastModifiedString(item.getLastModifiedString());
    }

    private BasicNoteItemA() {
        setDBManagementProvider(new BasicNoteItemDBManagementProvider(this));
    }

    public static @NonNull BasicNoteItemA newInstance(long id, long lastModified, String lastModifiedString, long noteId, long orderId, long priority, String name, String value, boolean checked) {
        BasicNoteItemA instance = new BasicNoteItemA();

        instance.setId(id);
        instance.setLastModified(lastModified);
        instance.setLastModifiedString(lastModifiedString);
        instance.setNoteId(noteId);
        instance.setOrderId(orderId);
        instance.setPriority(priority);
        instance.mName = name;
        instance.mValue = value;
        instance.mChecked = checked;

        return  instance;
    }

    private static @NonNull BasicNoteItemA fromFloatParams(long noteItemParamTypeId, @NonNull InputParser.FloatParamsResult floatParamsResult) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.setFloatParams(noteItemParamTypeId, floatParamsResult);
        return instance;
    }

    public static @NonNull BasicNoteItemA newCheckedEditValueInstance(String value) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.setValue(value);
        return instance;
    }

    public static @NonNull BasicNoteItemA newCheckedEditInstance(long noteItemParamTypeId, String value) {
        InputParser.FloatParamsResult floatParamsResult = InputParser.parseFloatParams(value);
        return fromFloatParams(noteItemParamTypeId, floatParamsResult);
    }

    public static @NonNull BasicNoteItemA newNamedEditInstance(String name, String value) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.mName = name;
        instance.mValue = value;
        return instance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeLong(getId());
        dest.writeLong(getLastModified());
        dest.writeString(getLastModifiedString());
        dest.writeLong(getOrderId());
        dest.writeLong(getPriority());
        dest.writeLong(mNoteId);
        dest.writeString(mName);
        dest.writeString(mValue);
        dest.writeInt(BooleanUtils.toInt(mChecked));
        dest.writeParcelable(mNoteItemParams, 0);
    }

    private BasicNoteItemA(Parcel in) {
        setId(in.readLong());
        setLastModified(in.readLong());
        setLastModifiedString(in.readString());
        setOrderId(in.readLong());
        setPriority(in.readLong());
        mNoteId = in.readLong();
        mName = in.readString();
        mValue = in.readString();
        mChecked = BooleanUtils.fromInt(in.readInt());
        mNoteItemParams = in.readParcelable(BasicNoteItemParams.class.getClassLoader());
    }

    public static final Parcelable.Creator<BasicNoteItemA> CREATOR = new Parcelable.Creator<BasicNoteItemA>() {
        @Override
        public BasicNoteItemA createFromParcel(Parcel source) {
            return new BasicNoteItemA(source);
        }

        @Override
        public BasicNoteItemA[] newArray(int size) {
            return new BasicNoteItemA[size];
        }
    };

}
