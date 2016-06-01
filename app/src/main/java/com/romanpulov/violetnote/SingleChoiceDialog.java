package com.romanpulov.violetnote;

import android.content.Context;

/**
 * Created by romanpulov on 01.06.2016.
 */
public class SingleChoiceDialog {

    public static class Builder {
        private final Context mContext;
        private final int mTheme;

        private int mTitleId;
        private int mItemsId;
        private int mInitialValue;

        public Builder(Context context, int theme) {
            mContext = context;
            mTheme = theme;
        }

        public Builder setTitleId(int titleId) {
            this.mTitleId = titleId;
            return this;
        }

        public Builder setItemsId(int itemsId) {
            this.mItemsId = itemsId;
            return this;
        }

        public Builder setInitialValue(int initialValue) {
            this.mInitialValue = initialValue;
            return this;
        }

        public void execute() {

        }

    }

    interface OnChoiceInputListener {
        void onChoiceInputListener(int value);
    }

    private final Context mContext;

    private OnChoiceInputListener mOnChoiceInputListener;

    public void setOnChoiceInputListener(OnChoiceInputListener listener) {
        mOnChoiceInputListener = listener;
    }

    private int mInitialValue;

    public void setInitialValue(int initialValue) {
        mInitialValue = initialValue;
    }

    public SingleChoiceDialog(Context context) {
        mContext = context;
    }

    public void show() {

    }

}
