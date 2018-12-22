package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.BasicNoteItemDBManagementProvider;

import java.util.ArrayList;
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
    private long mParamPrice;
    private List<BasicNoteItemParamValueA> mNoteItemParams = new ArrayList<>();

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

    public String getValueWithParams() {
        return InputParser.composeFloatParams(mValue, mParamPrice);
    }

    public String getParamPriceDisplayValue() {
        return InputParser.getFloatDisplayValue(mParamPrice);
    }

    public void setValue(String value) {
        mValue = value;
    }

    private void setFloatParams(@NonNull InputParser.FloatParamsResult floatParams) {
        mValue = floatParams.getText();
        if (floatParams.hasValue())
            mParamPrice = floatParams.getLongValue();
        else
            mParamPrice = 0;
    }

    public void setValueWithParams(String value) {
        setFloatParams(InputParser.parseFloatParams(value));
    }

    public long getParamPrice() {
        return mParamPrice;
    }

    public void setParamPrice(long price) {
        mParamPrice = price;
    }

    public List<BasicNoteItemParamValueA> getNoteItemParams() {
        return mNoteItemParams;
    }

    public void setNoteItemParams(List<BasicNoteItemParamValueA> params) {
        mNoteItemParams = params;
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

    private static @NonNull BasicNoteItemA fromFloatParams(@NonNull InputParser.FloatParamsResult floatParamsResult) {
        BasicNoteItemA instance = new BasicNoteItemA();
        instance.setFloatParams(floatParamsResult);
        return instance;
    }

    public static @NonNull BasicNoteItemA newCheckedEditInstance(String value) {
        InputParser.FloatParamsResult floatParamsResult = InputParser.parseFloatParams(value);
        return fromFloatParams(floatParamsResult);
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
        dest.writeLong(mParamPrice);
        dest.writeTypedList(mNoteItemParams);
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
        mParamPrice = in.readLong();
        in.readTypedList(mNoteItemParams, BasicNoteItemParamValueA.CREATOR);
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
