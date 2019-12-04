package com.romanpulov.violetnote.chooser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public abstract class AbstractHrChooserActivity extends ActionBarCompatActivity implements HrChooserFragment.OnChooserInteractionListener{
    public static final String CHOOSER_INITIAL_PATH = "ChooserInitialPath";
    public static final String CHOOSER_RESULT_PATH = "ChooserResultPath";
    public static final String CHOOSER_RESULT_NAME = "ChooserResultName";

    protected abstract Fragment createChooserFragment(String initialPath);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(android.R.id.content);
        if (fragment == null) {
            String initialPath = getIntent().getStringExtra(CHOOSER_INITIAL_PATH);
            fragment = createChooserFragment(initialPath);
            if (fragment != null) {
                fm.beginTransaction().add(android.R.id.content, fragment).commit();
            }
        }
    }

    @Override
    public void onChooserInteraction(AbstractChooseItem item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(CHOOSER_RESULT_PATH, item.getItemPath());
        resultIntent.putExtra(CHOOSER_RESULT_NAME, item.getItemName());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
