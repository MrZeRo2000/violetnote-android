package com.romanpulov.violetnote.view.helper;

import android.content.Context;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.ActionMenuView;
import android.view.View;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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

        ViewCompat.setOnApplyWindowInsetsListener(this.mToolbar, (v, windowInsets) -> {
            Insets insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply the insets as a margin to the view. This solution sets only the
            // bottom, left, and right dimensions, but you can apply whichever insets are
            // appropriate to your layout. You can also update the view padding if that's
            // more appropriate.
            ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            mlp.leftMargin = insets.left;
            mlp.bottomMargin = insets.bottom;
            mlp.rightMargin = insets.right;
            v.setLayoutParams(mlp);

            // Return CONSUMED if you don't want the window insets to keep passing
            // down to descendant views.
            return WindowInsetsCompat.CONSUMED;
        });

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
