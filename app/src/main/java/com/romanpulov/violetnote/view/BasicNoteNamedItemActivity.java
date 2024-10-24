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

public class BasicNoteNamedItemActivity extends BasicNoteDataPasswordActivity implements BottomToolbarProvider {
    private ActionMenuView mBottomToolbar;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_id;
    }

    @Override
    protected void refreshFragment() {
        if (!getProgress() && !(getFragment() instanceof BasicNoteNamedItemFragment)) {
            BasicNoteNamedItemFragment basicNoteNamedItemFragment =
                    BasicNoteNamedItemFragment.newInstance(mBasicNoteData, this);
            removeFragment().beginTransaction().add(
                    getFragmentContainerId(),
                    basicNoteNamedItemFragment
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_action, menu);
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
            if (fragment != null) {
                int itemId = item.getItemId();//workaround for issue with losing data after navigating via back button
                if (itemId == android.R.id.home) {
                    finish();
                    return true;
                } else if (itemId == R.id.action_add) {
                    if (fragment instanceof BasicNoteNamedItemFragment)
                        ((BasicNoteNamedItemFragment) fragment).performAddAction();
                    return true;
                }
                return super.onOptionsItemSelected(item);
            } else
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public ActionMenuView getBottomToolbar() {
        return mBottomToolbar;
    }
}
