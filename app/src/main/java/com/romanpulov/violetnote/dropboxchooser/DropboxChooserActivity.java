package com.romanpulov.violetnote.dropboxchooser;

/**
 * Activity for choosing file from Dropbox
 * Created by romanpulov on 02.07.2016.
 */

import android.content.Intent;
import android.os.Bundle;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.romanpulov.violetnote.chooser.ChooseItem;


public class DropboxChooserActivity extends ActionBarCompatActivity implements DropboxChooserFragment.OnChooserInteractionListener {
    public static final String CHOOSER_INITIAL_PATH = "ChooserInitialPath";
    private static final String CHOOSER_RESULT_PATH = "ChooserResultPath";
    private static final String CHOOSER_RESULT_NAME = "ChooserResultName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(android.R.id.content);
        if (fragment == null) {
            String initialPath = getIntent().getStringExtra(CHOOSER_INITIAL_PATH);
            fragment = DropboxChooserFragment.newInstance(initialPath);
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
