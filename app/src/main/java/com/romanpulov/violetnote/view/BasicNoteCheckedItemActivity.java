package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.BasicNoteDataPasswordActivity;
import com.romanpulov.violetnote.view.core.PasswordActivity;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //for ToolBar
        setContentView(R.layout.activity_toolbar_fragment_toolbar);

        //setup ToolBar instead of ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(mBasicNoteData.getNote().getTitle());
        setSupportActionBar(toolbar);
        setupActionBar();

        //bottom toolbar
        mBottomToolbar = findViewById(R.id.toolbar_bottom);
        if (mBottomToolbar != null) {
            getMenuInflater().inflate(R.menu.menu_listitem_bottom_actions, mBottomToolbar.getMenu());
            mBottomToolbar.setVisibility(View.GONE);
        }

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
                switch (item.getItemId()) {
                    case R.id.action_add:
                        ((BasicNoteCheckedItemFragment) fragment).showAddLayout();
                        return true;
                    case R.id.action_check_all:
                        ((BasicNoteCheckedItemFragment) fragment).performUpdateChecked(true);
                        return true;
                    case R.id.action_uncheck_all:
                        ((BasicNoteCheckedItemFragment) fragment).performUpdateChecked(false);
                        return true;
                    case R.id.action_checkout:
                        ((BasicNoteCheckedItemFragment) fragment).checkOut();
                        return true;
                    case R.id.action_refresh:
                        if (PasswordActivity.getPasswordValidityChecker().isValid()) {
                            ((BasicNoteCheckedItemFragment) fragment).refreshListWithView();
                        } else {
                            removeFragment();
                            requestPassword();
                        }
                        return true;
                    default:
                        return super.onOptionsItemSelected(item);
                }
            } else
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public ActionMenuView getBottomToolbar() {
        return mBottomToolbar;
    }

}
