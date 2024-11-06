package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
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

    public static BottomToolbarHelper from(
            ActionMenuView mToolbar,
            ActionMenuView.OnMenuItemClickListener menuItemClickListener) {
        return new BottomToolbarHelper(mToolbar, menuItemClickListener);
    }

    private BottomToolbarHelper(
            @NonNull BottomToolbarProvider toolbarProvider,
            ActionMenuView.OnMenuItemClickListener menuItemClickListener) {
        this(toolbarProvider.getBottomToolbar(), menuItemClickListener);
    }

    private BottomToolbarHelper(ActionMenuView mToolbar,
                                ActionMenuView.OnMenuItemClickListener menuItemClickListener) {
        this.mToolbar = mToolbar;
        this.mToolbar.setOnMenuItemClickListener(menuItemClickListener);
        this.mToolbar.setPopupTheme(0);
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
