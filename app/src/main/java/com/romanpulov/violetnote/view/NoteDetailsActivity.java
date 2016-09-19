package com.romanpulov.violetnote.view;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import com.romanpulov.violetnote.view.core.PassDataPasswordActivity;
import com.romanpulov.violetnote.model.PassNoteA;

public class NoteDetailsActivity extends PassDataPasswordActivity implements NoteDetailsFragment.OnNoteDetailsInteractionListener {
    @Override
    protected void refreshFragment() {
        Fragment fragment = NoteDetailsFragment.newInstance(mPassDataA);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected void updatePassword(String password) {
        setLoadErrorFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshFragment();
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
    public void onAttrItemSelection(PassNoteA.AttrItem item) {

    }

    @Override
    public void onBackPressed() {
        updateResult();
        finish();
        super.onBackPressed();
    }
}
