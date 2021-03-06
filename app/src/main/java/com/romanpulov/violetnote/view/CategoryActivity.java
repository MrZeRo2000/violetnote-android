package com.romanpulov.violetnote.view;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataPasswordActivity;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.model.PassCategoryA;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.view.core.PassDataProgressFragment;
import com.romanpulov.violetnote.view.core.PasswordActivity;

public class CategoryActivity extends PassDataPasswordActivity implements PassDataProgressFragment.OnPassDataFragmentInteractionListener, CategoryFragment.OnPassCategoryInteractionListener, OnSearchInteractionListener {
    private static final boolean mSampleData = false;

    @Override
    protected void refreshFragment() {
        //leave Progress if it is active
        if (!getProgress()) {
            removeProgressFragment();
            Fragment fragment = CategoryFragment.newInstance(mPassDataA);
            removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
        }
    }

    @Override
    protected void updatePassword(String password) {
        PassDataProgressFragment passDataProgressFragment = PassDataProgressFragment.newInstance(this);
        removeFragment().beginTransaction().add(getFragmentContainerId(), passDataProgressFragment).commit();
        passDataProgressFragment.loadPassData(password);
        setProgress(true);
    }

    @Override
    public void onPassDataFragmentAttached() {
        onProgressAttached();
    }

    @Override
    public void onPassDataLoaded(PassDataA passDataA, String errorText) {
        mPasswordProvider = passDataA;
        mPassDataA = passDataA;
        setProgress(false);

        try {
            removeProgressFragment();
            if (errorText != null)
                setLoadErrorFragment(errorText);
            else {
                refreshFragment();
                PassDataPasswordActivity.getPasswordValidityChecker().startPeriod();
            }
        } catch (Exception e) {
            e.printStackTrace();
            requirePassword();
        }
    }

    @Override
    public void onPassCategorySelection(PassCategoryA item) {
        if (item.getNotesCount() > 0) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra(PASS_DATA, PassDataA.newCategoryInstance(mPassDataA, item));
            PasswordActivity.getPasswordValidityChecker().startPeriod();
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onSearchFragmentInteraction(String searchText, boolean isSearchSystem, boolean isSearchUser) {
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(PASS_DATA, PassDataA.newSearchInstance(mPassDataA, searchText, isSearchSystem, isSearchUser));
        PasswordActivity.getPasswordValidityChecker().startPeriod();
        intent.putExtra(SearchResultActivity.SEARCH_TEXT, searchText);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPassDataA = savedInstanceState.getParcelable(PASS_DATA);
            mPasswordProvider = mPassDataA;
        }
    }

    /**
     * To support loading sample data, should not be used in normal operation
     */
    @Override
    protected void onResume() {
        if ((mSampleData) && (mPassDataA == null)) {
            mPassDataA = DocumentPassDataLoader.loadSamplePassData();
            refreshFragment();
        } else {
            super.onResume();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getPasswordValidityChecker().resetPeriod();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pass_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getPasswordValidityChecker().resetPeriod();
                return super.onOptionsItemSelected(item);
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_search:
                Fragment fragment = getFragment();
                if (fragment instanceof CategoryFragment)
                    ((CategoryFragment)fragment).showSearchLayout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PASS_DATA, mPassDataA);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPassDataA = savedInstanceState.getParcelable(PASS_DATA);
    }

}
