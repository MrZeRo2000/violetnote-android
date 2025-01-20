package com.romanpulov.violetnote.view;

import com.romanpulov.violetnote.model.BasicNoteItemA;

public interface OnBasicNoteCheckedItemInteractionListener extends OnBasicNoteItemFragmentInteractionListener {
    void onBasicNoteItemPriceClick(BasicNoteItemA item, int position);
}
