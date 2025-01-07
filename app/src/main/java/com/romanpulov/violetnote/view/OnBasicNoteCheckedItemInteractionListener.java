package com.romanpulov.violetnote.view;

import com.romanpulov.violetnote.model.BasicNoteItemA;

public interface OnBasicNoteCheckedItemInteractionListener extends BasicNoteItemFragment.OnBasicNoteItemFragmentInteractionListener {
    void onBasicNoteItemPriceClick(BasicNoteItemA item, int position);
}
