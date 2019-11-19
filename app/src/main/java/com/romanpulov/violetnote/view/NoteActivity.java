package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.PassDataPasswordActivity;
import com.romanpulov.violetnote.view.core.PasswordActivity;
import com.romanpulov.violetnote.model.PassDataA;
import com.romanpulov.violetnote.model.PassNoteA;

public class NoteActivity extends PassDataPasswordActivity implements OnPassNoteItemInteractionListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        refreshFragment();
    }

    @Override
    protected void refreshFragment() {
        Fragment fragment = NoteFragment.newInstance(mPassDataA);
        removeFragment().beginTransaction().add(getFragmentContainerId(), fragment).commit();
    }

    @Override
    protected void updatePassword(String password) {
        setLoadErrorFragment(getString(R.string.error_load));
    }

    @Override
    public void onPassNoteItemInteraction(PassNoteA item) {
        Intent intent = new Intent(this, NoteDetailsActivity.class);
        intent.putExtra(PasswordActivity.PASS_DATA, PassDataA.newNoteInstance(mPassDataA, item));
        PasswordActivity.getPasswordValidityChecker().startPeriod();
        startActivityForResult(intent, 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pass_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Workaround for issue with back button in toolbar default behavior
        switch (item.getItemId()) {
            case android.R.id.home:
                updateResult();
                finish();
                return true;
            case R.id.action_search:
                Fragment fragment = getFragment();
                if (fragment != null)
                    ((NoteFragment)fragment).showSearchLayout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        updateResult();
        finish();
        super.onBackPressed();
    }
}
