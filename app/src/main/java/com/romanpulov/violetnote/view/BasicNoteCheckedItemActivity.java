package com.romanpulov.violetnote.view;

import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.appcompat.widget.ActionMenuView;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;

import java.util.Objects;

public class BasicNoteCheckedItemActivity extends BasicNoteDataPasswordActivity implements BottomToolbarProvider {

    private ActionMenuView mBottomToolbar;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_id;
    }

    @Override
    protected void refreshFragment() {
        if (BasicNoteCheckedItemFragment.mRefreshHandler.hasMessages(0)) {
            BasicNoteCheckedItemFragment.mRefreshHandler.removeMessages(0);
            setForceUpdatePassword(true);
        } else {
            setForceUpdatePassword(false);
        }

        if (!getProgress() && !(getFragment() instanceof BasicNoteCheckedItemFragment)) {
            BasicNoteCheckedItemFragment basicNoteCheckedItemFragment =
                    BasicNoteCheckedItemFragment.newInstance(mBasicNoteData, this);
            removeFragment().beginTransaction().add(
                    getFragmentContainerId(), basicNoteCheckedItemFragment
            ).commit();
        }
    }

    @Override
    protected void setupLayout() {
        //for ToolBar
        setContentView(R.layout.activity_toolbar_fragment_toolbar);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //bottom toolbar
        mBottomToolbar = findViewById(R.id.toolbar_bottom);
        if (mBottomToolbar != null) {
            getMenuInflater().inflate(R.menu.menu_listitem_bottom_actions, mBottomToolbar.getMenu());
            mBottomToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).setTitle(mBasicNoteData.getNote().getTitle());

        refreshFragment();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Fragment fragment = getFragment();
        if (fragment instanceof BasicNoteCheckedItemFragment) {
            ((BasicNoteCheckedItemFragment) fragment).hideAddLayout();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_checked_item, menu);

        if (mBasicNoteData.getNote().isEncrypted()) {
            menu.removeItem(R.id.action_history);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //workaround for issue with losing data after navigating via back button
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else {
            Fragment fragment = getFragment();
            if (fragment instanceof BasicNoteCheckedItemFragment) {
                BasicNoteCheckedItemFragment basicNoteCheckedItemFragment = (BasicNoteCheckedItemFragment) fragment;
                int itemId = item.getItemId();
                if (itemId == R.id.action_add) {
                    basicNoteCheckedItemFragment.showAddLayout();
                    return true;
                } else if (itemId == R.id.action_check_all) {
                    basicNoteCheckedItemFragment.performUpdateChecked(true);
                    return true;
                } else if (itemId == R.id.action_uncheck_all) {
                    basicNoteCheckedItemFragment.performUpdateChecked(false);
                    return true;
                } else if (itemId == R.id.action_checkout) {
                    basicNoteCheckedItemFragment.performCheckOut();
                    return true;
                } else if (itemId == R.id.action_history) {
                    basicNoteCheckedItemFragment.startHEventHistoryActivity();
                    return true;
                } else if (itemId == R.id.action_refresh) {
                    basicNoteCheckedItemFragment.setSwipeRefreshing(true);
                    basicNoteCheckedItemFragment.performRefresh();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            } else
                return super.onOptionsItemSelected(item);
        }
    }

    public void requestInvalidateFragment() {
        removeFragment();
        requestPassword();
    }

    @Override
    public ActionMenuView getBottomToolbar() {
        return mBottomToolbar;
    }

}
