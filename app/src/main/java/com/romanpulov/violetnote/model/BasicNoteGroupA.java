package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.romanpulov.violetnote.db.provider.BasicNoteGroupDBManagementProvider;

import java.util.Arrays;

public class BasicNoteGroupA extends BasicCommonNoteA implements Parcelable {
    public static final int DEFAULT_NOTE_GROUP_ID = 2;
    public static final int PASSWORD_NOTE_GROUP_TYPE = 1;
    public static final int BASIC_NOTE_GROUP_TYPE = 10;
    public static final String BASIC_NOTE_GROUP_DATA = "BasicNoteGroupData";

    private long mGroupType;

    public long getGroupType() {
        return mGroupType;
    }

    private String mGroupName;

    public String getGroupName() {
        return mGroupName;
    }

    private long mGroupIcon;

    public long getGroupIcon() {
        return mGroupIcon;
    }

    @Override
    public String getDisplayTitle() {
        return mGroupName;
    }

    private BasicNoteGroupA() {
        setDBManagementProvider(new BasicNoteGroupDBManagementProvider(this));
    }

    public static BasicNoteGroupA newInstance(long id, long groupType, String groupName, long groupIcon, long orderId) {
        BasicNoteGroupA instance = new BasicNoteGroupA();

        instance.setId(id);
        instance.setOrderId(orderId);

        instance.mGroupType = groupType;
        instance.mGroupName = groupName;
        instance.mGroupIcon = groupIcon;

        return instance;
    }

    public static BasicNoteGroupA newEditInstance(String groupName, long groupIcon) {
        BasicNoteGroupA instance = new BasicNoteGroupA();

        instance.mGroupName = groupName;
        instance.mGroupIcon = groupIcon;

        return instance;
    }

    private BasicNoteGroupA(@NonNull Parcel in) {
        setId(in.readLong());
        setOrderId(in.readLong());
        mGroupType = in.readLong();
        mGroupName = in.readString();
        mGroupIcon = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeLong(getOrderId());
        dest.writeLong(mGroupType);
        dest.writeString(mGroupName);
        dest.writeLong(mGroupIcon);
    }

    public static final Parcelable.Creator<BasicNoteGroupA> CREATOR = new Parcelable.Creator<BasicNoteGroupA>() {
        @Override
        public BasicNoteGroupA createFromParcel(Parcel source) {
            return new BasicNoteGroupA(source);
        }

        @Override
        public BasicNoteGroupA[] newArray(int size) {
            return new BasicNoteGroupA[size];
        }
    };


    @Override
    @NonNull
    public String toString() {
        return "{" +
                "[id=" + getId() + "]," +
                "[orderId=" + getOrderId() + "]," +
                "[groupType=" + mGroupType + "]," +
                "[groupName=" + mGroupName + "]," +
                "[groupIcon=" + mGroupIcon + "]" +
                "}";
    }
}
