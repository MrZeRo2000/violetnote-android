package com.romanpulov.violetnote.dropboxchooser;

/**
 * Created by romanpulov on 02.07.2016.
 */

import android.content.Intent;
import android.os.Bundle;

import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;
import android.support.v4.app.Fragment;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.chooser.ChooseItem;


public class DropboxChooserActivity extends ActionBarCompatActivity implements DropboxChooserFragment.OnChooserInteractionListener {
    public static final String CHOOSER_INITIAL_PATH = "ChooserInitialPath";
    public static final String CHOOSER_RESULT_PATH = "ChooserResultPath";
    public static final String CHOOSER_RESULT_NAME = "ChooserResultName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_hr_chooser);

        String initialPath = getIntent().getStringExtra(CHOOSER_INITIAL_PATH);

        Fragment fragment = DropboxChooserFragment.newInstance(initialPath);
        getSupportFragmentManager().beginTransaction().add(R.id.hr_fragment_container, fragment).commit();
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
