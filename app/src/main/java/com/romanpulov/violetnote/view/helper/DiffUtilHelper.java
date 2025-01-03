package com.romanpulov.violetnote.view.helper;

import androidx.recyclerview.widget.DiffUtil;
import com.romanpulov.violetnote.model.BasicEntityNoteA;

import java.util.List;

public class DiffUtilHelper {
    public static <T extends BasicEntityNoteA> DiffUtil.Callback getEntityListCallback(
            final List<T> oldList, final List<T> newList) {
        return new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldList.size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).getId() ==
                        newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        };
    }

    public static <T extends BasicEntityNoteA> DiffUtil.DiffResult getEntityListDiffResult(
            final List<T> oldList, final List<T> newList) {
        return DiffUtil.calculateDiff(getEntityListCallback(oldList, newList));
    }
}
