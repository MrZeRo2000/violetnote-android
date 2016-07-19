package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.model.PassNoteA;

public class NoteActivity extends PasswordActivity implements OnPassNoteItemInteractionListener {

    private static void log(String message) {
        Log.d("NoteActivity", message);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_note);

        refreshFragment();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.note_fragment_container;
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = NoteFragment.newInstance(mPassDataA);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
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
