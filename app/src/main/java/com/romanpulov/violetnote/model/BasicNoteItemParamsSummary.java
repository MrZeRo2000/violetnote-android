package com.romanpulov.violetnote.model;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class BasicNoteItemParamsSummary {
    private int mCheckedCount;
    private long mCheckedDisplayValue;
    private long mTotalDisplayValue;
    private long mTotalValue;
    private boolean mIsInt = true;

    public int getCheckedCount() {
        return mCheckedCount;
    }

    public long getCheckedDisplayValue() {
        return mCheckedDisplayValue;
    }

    public long getTotalDisplayValue() {
        return mTotalDisplayValue;
    }

    public long getTotalValue() {
        return mTotalValue;
    }

    public boolean getIsInt() {
        return mIsInt;
    }

    @Override
    public @NotNull String toString() {
        return "ParamsSummary{" +
                "mCheckedCount=" + mCheckedCount +
                ", mCheckedDisplayValue=" + mCheckedDisplayValue +
                ", mTotalDisplayValue=" + mTotalDisplayValue +
                ", mTotalValue=" + mTotalValue +
                ", mIsInt=" + mIsInt +
                ", checkedDisplayValue=" + getCheckedDisplayValue() +
                ", totalDisplayValue=" + getTotalDisplayValue() +
                ", totalValue=" + getTotalValue() +
                ", isInt=" + getIsInt() +
                '}';
    }

    private BasicNoteItemParamsSummary() {}

    public static BasicNoteItemParamsSummary fromNoteItems(Collection<BasicNoteItemA> items, long noteItemParamTypeId) {
        BasicNoteItemParamsSummary paramsSummary = new BasicNoteItemParamsSummary();

        long totalDisplayValue = 0;
        long totalIntDisplayValue = 0;
        long checkedDisplayValue = 0;
        long checkedIntDisplayValue = 0;
        int checkedCount = 0;

        for (BasicNoteItemA item : items) {
            long value = item.getParamLong(noteItemParamTypeId);
            long intValue = value > 0 ? value : 100;

            totalDisplayValue += value;
            totalIntDisplayValue += intValue;
            if (item.isChecked()) {
                checkedDisplayValue += value;
                checkedIntDisplayValue += intValue;
                checkedCount ++;
            }
            if (paramsSummary.mIsInt && value % 100 != 0) {
                paramsSummary.mIsInt = false;
            }
        }

        if (paramsSummary.mIsInt && totalDisplayValue == 0) {
            paramsSummary.mIsInt = false;
        }

        if (paramsSummary.mIsInt) {
            paramsSummary.mTotalDisplayValue = totalIntDisplayValue;
            paramsSummary.mCheckedDisplayValue = checkedIntDisplayValue;
        } else {
            paramsSummary.mTotalDisplayValue = totalDisplayValue;
            paramsSummary.mCheckedDisplayValue = checkedDisplayValue;
        }
        paramsSummary.mCheckedCount = checkedCount;
        paramsSummary.mTotalValue = totalDisplayValue;

        return paramsSummary;
    }
}
