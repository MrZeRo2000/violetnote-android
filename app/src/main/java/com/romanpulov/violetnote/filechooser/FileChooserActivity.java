package com.romanpulov.violetnote.filechooser;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import com.romanpulov.violetnote.chooser.ChooseItem;

public class FileChooserActivity extends ActionBarCompatActivity implements FileChooserFragment.OnChooserInteractionListener {
    public static final String CHOOSER_INITIAL_PATH = "ChooserInitialPath";
    public static final String CHOOSER_RESULT_PATH = "ChooserResultPath";
    private static final String CHOOSER_RESULT_NAME = "ChooserResultName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(android.R.id.content);
        if (fragment == null) {
            String initialPath = getIntent().getStringExtra(CHOOSER_INITIAL_PATH);
            fragment = FileChooserFragment.newInstance(initialPath);
            fm.beginTransaction().add(android.R.id.content, fragment).commit();
        }
    }

    @Override
    public void onChooserInteraction(ChooseItem item) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(CHOOSER_RESULT_PATH, item.getItemPath());
        resultIntent.putExtra(CHOOSER_RESULT_NAME, item.getItemName());
        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
