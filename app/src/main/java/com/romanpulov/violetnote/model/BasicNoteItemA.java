package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

import com.romanpulov.violetnote.db.provider.BasicNoteItemDBManagementProvider;
import com.romanpulov.violetnote.model.vo.BasicParamValueA;

import java.util.Objects;

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
        mNoteItemParams = Objects.requireNonNullElseGet(params, BasicNoteItemParams::createEmpty);
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

    public static @NonNull BasicNoteItemA newCheckedEditInstance(long noteId, long noteItemParamTypeId, String value) {
        InputParser.FloatParamsResult floatParamsResult = InputParser.parseFloatParams(value);
        return fromFloatParams(noteItemParamTypeId, floatParamsResult).withNoteId(noteId);
    }

    public static @NonNull BasicNoteItemA newNamedEditInstance(String name, String value) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.mName = name;
        instance.mValue = value;
        return instance;
    }

    public @NonNull BasicNoteItemA withNoteId(long noteId) {
        setNoteId(noteId);
        return this;
    }

    public @NonNull BasicNoteItemA withName(String name) {
        setName(name);
        return this;
    }

    public @NonNull BasicNoteItemA withValue(String value) {
        setValue(value);
        return this;
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

    public static final Parcelable.Creator<BasicNoteItemA> CREATOR = new Parcelable.Creator<>() {
        @Override
        public BasicNoteItemA createFromParcel(Parcel source) {
            return new BasicNoteItemA(source);
        }

        @Override
        public BasicNoteItemA[] newArray(int size) {
            return new BasicNoteItemA[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BasicNoteItemA that = (BasicNoteItemA) o;
        return mNoteId == that.mNoteId && mChecked == that.mChecked && Objects.equals(mName, that.mName) && Objects.equals(mValue, that.mValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), mNoteId, mName, mValue, mChecked);
    }
}
