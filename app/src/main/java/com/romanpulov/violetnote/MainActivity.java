package com.romanpulov.violetnote;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;


import java.util.ArrayList;

public class MainActivity extends PasswordActivity implements CategoryFragment.OnListFragmentInteractionListener {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);
    }

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
    public void startActivity(Intent intent) {
        // check if search intent
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            /*
            intent.putExtra(PASS_DATA, mPassDataA);
            intent.putExtra(PASSWORD_REQUIRED, false);
            */
            Intent intent1 = new Intent(this, SearchResultActivity.class);
            intent1.putExtra(PASS_DATA, mPassDataA);
            intent1.putExtra(PASSWORD_REQUIRED, false);
            startActivityForResult(intent1, 0);
        } else
            super.startActivity(intent);
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
                //Toast.makeText(this, "Search invoked", Toast.LENGTH_SHORT).show();
                //PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
                //passwordInputDialog.show();
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
