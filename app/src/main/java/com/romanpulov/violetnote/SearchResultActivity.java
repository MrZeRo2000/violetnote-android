package com.romanpulov.violetnote;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class SearchResultActivity extends PasswordActivity implements OnPassNoteItemInteractionListener {
    public static String SEARCH_TEXT = "SearchText";

    private String mSearchText;

    private static void log(String message) {
        Log.d("SearchResultActivity", message);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.search_result_fragment_container;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = SearchResultFragment.newInstance(mPassDataA, mSearchText);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        mSearchText = getIntent().getStringExtra(SEARCH_TEXT);

        refreshFragment();
    }


    @Override
    public void onPassNoteItemInteraction(PassNoteA item) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra(PasswordActivity.PASS_DATA, PassDataA.newNoteInstance(mPassDataA, item));
        intent.putExtra(PASSWORD_REQUIRED, false);
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Workaround for issue with back button in toolbar default behavior
        if (item.getItemId() == android.R.id.home) {
            updateResult();
            finish();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        updateResult();
        finish();
        super.onBackPressed();
    }
}
