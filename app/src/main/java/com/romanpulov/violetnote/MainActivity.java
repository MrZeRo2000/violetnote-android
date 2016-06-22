package com.romanpulov.violetnote;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends PasswordActivity implements CategoryFragment.OnListFragmentInteractionListener, CategoryFragment.OnSearchFragmentInteractionListener {
    public static final boolean mSampleData = false;

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = CategoryFragment.newInstance(mPassDataA);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    public void onListFragmentInteraction(PassCategoryA item) {
        if (item.getNotesCount() > 0) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra(PASS_DATA, PassDataA.newCategoryInstance(mPassDataA, item));
            intent.putExtra(PASSWORD_REQUIRED, false);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    public void onSearchFragmentInteraction(String searchText) {
        Toast.makeText(this, "Search with value " + searchText, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, SearchResultActivity.class);
        intent.putExtra(PASS_DATA, PassDataA.newSearchInstance(mPassDataA, searchText));
        intent.putExtra(PASSWORD_REQUIRED, false);
        intent.putExtra(SearchResultActivity.SEARCH_TEXT, searchText);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
    }

    /**
     * To support loading sample data, should not be used in normal operation
     */
    @Override
    protected void onResume() {
        if ((mSampleData) && (mPassDataA == null)) {
            mPassDataA = Document.loadSamplePassData();
            refreshFragment();
        } else {
            super.onResume();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        // Associate searchable configuration with the SearchView
        /*
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);
        searchView.setIconified(false);
        */

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_refresh:
                removeFragment();
                requestPassword();
                return true;
            case R.id.action_search:
                Fragment fragment = getFragment();
                if (fragment != null)
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
        Log.d("MainActivity", "OnSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPassDataA = savedInstanceState.getParcelable(PASS_DATA);
        Log.d("MainActivity", "OnRestoreInstanceState");
    }
}
