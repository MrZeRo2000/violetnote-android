package com.romanpulov.violetnote.view.helper;

import android.support.v7.widget.ActionMenuView;
import android.view.View;

public class BottomToolbarHelper {
    private final ActionMenuView mToolbar;

    public BottomToolbarHelper(ActionMenuView toolbar) {
        mToolbar = toolbar;
        hideLayout();
    }

    public void hideLayout() {
        mToolbar.setVisibility(View.GONE);
    }

    public void showLayout(int selectedCount, int totalCount) {
        int visibleCount = ActionHelper.updateActionMenu(mToolbar.getMenu(), selectedCount, totalCount);
        mToolbar.setVisibility(visibleCount == 0 ? View.GONE : View.VISIBLE);
    }
}
