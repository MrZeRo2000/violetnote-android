package com.romanpulov.violetnote.model.vo;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONException;
import org.json.JSONObject;

import com.romanpulov.violetnote.model.BooleanUtils;

public class BasicNoteGroupDisplayOptions implements Parcelable {

    private final static String INDICATORS = "indicators";

    private final static String TOTAL_FLAG = "total";
    private final static String UNCHECKED_FLAG = "unchecked";
    private final static String CHECKED_FLAG = "checked";

    private boolean mTotalFlag;

    public boolean getTotalFlag() {
        return mTotalFlag;
    }

    public void setTotalFlag(boolean value) {
        mTotalFlag = value;
    }

    private boolean mUncheckedFlag;

    public boolean getUncheckedFlag() {
        return mUncheckedFlag;
    }

    public void setUncheckedFlag(boolean value) {
        mUncheckedFlag = value;
    }

    private boolean mCheckedFlag;

    public boolean getCheckedFlag() {
        return mCheckedFlag;
    }

    public void setCheckedFlag(boolean value) {
        mCheckedFlag = value;
    }

    private BasicNoteGroupDisplayOptions() {

    }

    public static BasicNoteGroupDisplayOptions createWithDefaults() {
        return new BasicNoteGroupDisplayOptions();
    }

    public static BasicNoteGroupDisplayOptions fromJSONString(String jsonString) {
        BasicNoteGroupDisplayOptions instance = createWithDefaults();

        if ((jsonString != null) && !(jsonString.isEmpty()))
        try {
            JSONObject jo = new JSONObject(jsonString);
            JSONObject jo_indicators = jo.getJSONObject(INDICATORS);

            instance.mTotalFlag = jo_indicators.optBoolean(TOTAL_FLAG);
            instance.mUncheckedFlag = jo_indicators.optBoolean(UNCHECKED_FLAG);
            instance.mCheckedFlag = jo_indicators.optBoolean(CHECKED_FLAG);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return instance;
    }

    public String toJSONString() {
        JSONObject jo = new JSONObject();

        JSONObject jo_indicators = new JSONObject();

        try {
            if (mTotalFlag) {
                jo_indicators.put(TOTAL_FLAG, true);
            }
            if (mUncheckedFlag) {
                jo_indicators.put(UNCHECKED_FLAG, true);
            }
            if (mCheckedFlag) {
                jo_indicators.put(CHECKED_FLAG, true);
            }

            if (jo_indicators.length() > 0) {
                jo.put(INDICATORS, jo_indicators);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jo.length() > 0 ? jo.toString() : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(BooleanUtils.toInt(mTotalFlag));
        dest.writeInt(BooleanUtils.toInt(mUncheckedFlag));
        dest.writeInt(BooleanUtils.toInt(mCheckedFlag));
    }

    private BasicNoteGroupDisplayOptions(Parcel in) {
        mTotalFlag = BooleanUtils.fromInt(in.readInt());
        mUncheckedFlag = BooleanUtils.fromInt(in.readInt());
        mCheckedFlag = BooleanUtils.fromInt(in.readInt());
    }

    public static final Parcelable.Creator<BasicNoteGroupDisplayOptions> CREATOR = new Parcelable.Creator<BasicNoteGroupDisplayOptions>() {
        @Override
        public BasicNoteGroupDisplayOptions createFromParcel(Parcel source) {
            return new BasicNoteGroupDisplayOptions(source);
        }

        @Override
        public BasicNoteGroupDisplayOptions[] newArray(int size) {
            return new BasicNoteGroupDisplayOptions[size];
        }
    };

}
