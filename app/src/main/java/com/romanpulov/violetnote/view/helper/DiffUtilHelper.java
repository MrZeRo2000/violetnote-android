package com.romanpulov.violetnote.view.helper;

import androidx.recyclerview.widget.DiffUtil;
import com.romanpulov.violetnote.model.BasicEntityNoteA;

import java.util.List;
import java.util.function.Supplier;

public class DiffUtilHelper {
    public static <T extends BasicEntityNoteA> DiffUtil.Callback getEntityListCallback(
            final Supplier<List<T>> oldListProvider, final List<T> newList) {
        return new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return oldListProvider.get().size();
            }

            @Override
            public int getNewListSize() {
                return newList.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return oldListProvider.get().get(oldItemPosition).getId() ==
                        newList.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return oldListProvider.get().get(oldItemPosition).equals(newList.get(newItemPosition));
            }
        };
    }

    public static <T extends BasicEntityNoteA> DiffUtil.DiffResult getEntityListDiffResult(
            final Supplier<List<T>> oldListProvider, final List<T> newList) {
        return DiffUtil.calculateDiff(getEntityListCallback(oldListProvider, newList));
    }
}
