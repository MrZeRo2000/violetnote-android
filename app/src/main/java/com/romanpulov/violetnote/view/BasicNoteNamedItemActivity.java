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
                switch (item.getItemId()) {
                    //workaround for issue with losing data after navigating via back button
                    case android.R.id.home:
                        finish();
                        return true;
                    case R.id.action_add:
                        if (fragment instanceof BasicNoteNamedItemFragment)
                            ((BasicNoteNamedItemFragment) fragment).performAddAction();
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
