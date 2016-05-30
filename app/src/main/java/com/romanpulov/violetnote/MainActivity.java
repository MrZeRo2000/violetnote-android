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


import java.util.ArrayList;

public class MainActivity extends PasswordActivity implements CategoryFragment.OnListFragmentInteractionListener {
    public static final boolean mSampleData = false;

    public static final String PASS_CATEGORY_ITEM = "PassCategoryItem";
    public static final String PASS_NOTE_DATA = "PassNoteData";

    @Override
    protected int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = CategoryFragment.newInstance((ArrayList<PassCategoryA>)mPassDataA.getPassCategoryData());
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    public void onListFragmentInteraction(PassCategoryA item) {
        if (item.getNotesCount() > 0) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra(PASS_CATEGORY_ITEM, item);
            intent.putParcelableArrayListExtra(PASS_NOTE_DATA, (ArrayList<PassNoteA>) mPassDataA.getPassNoteData(item));
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
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default
        searchView.setIconified(false);

        return true;
    }
}
