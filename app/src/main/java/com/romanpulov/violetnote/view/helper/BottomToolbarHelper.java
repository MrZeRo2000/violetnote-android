package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.ActionMenuView;
import android.view.View;

import com.romanpulov.violetnote.view.BottomToolbarProvider;

public class BottomToolbarHelper {
    private final ActionMenuView mToolbar;

    @Nullable
    public static BottomToolbarHelper fromContext(
            @NonNull Context context,
            ActionMenuView.OnMenuItemClickListener menuItemClickListener) {
        if (context instanceof BottomToolbarProvider) {
            return new BottomToolbarHelper((BottomToolbarProvider) context, menuItemClickListener);
        } else {
            return null;
        }
    }

    private BottomToolbarHelper(
            @NonNull BottomToolbarProvider toolbarProvider,
            ActionMenuView.OnMenuItemClickListener menuItemClickListener) {
        mToolbar = toolbarProvider.getBottomToolbar();
        mToolbar.setOnMenuItemClickListener(menuItemClickListener);
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
