package com.romanpulov.violetnote.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * BasicNote data class
 * Created by rpulov on 21.08.2016.
 */
public class BasicNoteDataA implements Parcelable, PasswordProvider {

    public static class ParamsSummary {
        private long mCheckedCount;
        private long mTotalCount;
        private boolean mIsInt = true;

        public long getCheckedCount() {
            return mCheckedCount;
        }

        public void setCheckedCount(long value) {
            this.mCheckedCount = value;
        }

        public long getTotalCount() {
            return mTotalCount;
        }

        public void setTotalCount(long value) {
            this.mTotalCount = value;
        }

        public boolean getIsInt() {
            return mIsInt;
        }

        public void setIsInt(boolean value) {
            mIsInt = value;
        }
    }

    private String mPassword;

    @Override
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    private BasicNoteGroupA mNoteGroup;

    private ArrayList<BasicNoteA> mNoteList;

    private List<BasicNoteA> mRelatedNoteList;

    public List<BasicNoteA> getRelatedNoteList() {
        return mRelatedNoteList;
    }

    public BasicNoteA getNote() {
        if ((mNoteList != null) && (!mNoteList.isEmpty()))
            return mNoteList.get(0);
        else
            return null;
    }

    private BasicNoteDataA(String password) {
        mPassword = password;
    }

    public static BasicNoteDataA newInstance(String password, BasicNoteGroupA noteGroup, ArrayList<BasicNoteA> noteList, List<BasicNoteA> relatedNoteList) {
        BasicNoteDataA newNoteData = new BasicNoteDataA(password);
        newNoteData.mNoteGroup = noteGroup;
        newNoteData.mNoteList = noteList;
        newNoteData.mRelatedNoteList = relatedNoteList;
        return newNoteData;
    }

    public int getCheckedCount() {
        int result = 0;

        for (BasicNoteA note : mNoteList) {
            for (BasicNoteItemA item : note.getItems()) {
                if (item.isChecked())
                    result ++;
            }
        }

        return result;
    }

    public ParamsSummary getParamsSummary(long noteItemParamTypeId) {
        ParamsSummary paramsSummary = new ParamsSummary();
        long totalCount = 0;
        long totalIntCount = 0;
        long checkedCount = 0;
        long checkedIntCount = 0;

        for (BasicNoteA note : mNoteList) {
            for (BasicNoteItemA item : note.getItems()) {
                long value = item.getParamLong(noteItemParamTypeId);
                long intValue = value > 0 ? value : 1;

                totalCount += value;
                totalIntCount += intValue;
                if (item.isChecked()) {
                    checkedCount += value;
                    checkedIntCount += intValue;
                }
                if (paramsSummary.mIsInt && value % 100 != 0) {
                    paramsSummary.mIsInt = false;
                }
            }
        }
        if (paramsSummary.mIsInt) {
            paramsSummary.mTotalCount = totalIntCount;
            paramsSummary.mCheckedCount = checkedIntCount;
        } else {
            paramsSummary.mTotalCount = totalCount;
            paramsSummary.mCheckedCount = checkedCount;
        }

        return paramsSummary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mPassword);
        dest.writeParcelable(mNoteGroup, 0);
        dest.writeTypedList(mNoteList);
        dest.writeTypedList(mRelatedNoteList);
    }

    private BasicNoteDataA(Parcel in) {
        mPassword = in.readString();
        mNoteGroup = in.readParcelable(BasicNoteGroupA.class.getClassLoader());
        mNoteList = new ArrayList<>();
        mRelatedNoteList = new ArrayList<>();
        in.readTypedList(mNoteList, BasicNoteA.CREATOR);
        in.readTypedList(mRelatedNoteList, BasicNoteA.CREATOR);
    }

    public static final Parcelable.Creator<BasicNoteDataA> CREATOR = new Parcelable.Creator<BasicNoteDataA>() {
        @Override
        public BasicNoteDataA createFromParcel(Parcel source) {
            return new BasicNoteDataA(source);
        }

        @Override
        public BasicNoteDataA[] newArray(int size) {
            return new BasicNoteDataA[size];
        }
    };
}
