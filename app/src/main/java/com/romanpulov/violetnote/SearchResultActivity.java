package com.romanpulov.violetnote;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class SearchResultActivity extends PasswordActivity implements SearchResultFragment.OnFragmentInteractionListener {

    private static void log(String message) {
        Log.d("SearchResultActivity", message);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.search_result_fragment_container;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = SearchResultFragment.newInstance(mPassDataA);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        handleIntent(getIntent());

        log("onCreate");
    }


    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search your data somehow

            Toast.makeText(this, query + " Extra : " + intent.getParcelableExtra(PasswordActivity.PASS_DATA), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFragmentInteraction(PassNoteA item) {

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
