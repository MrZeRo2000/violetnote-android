package com.romanpulov.violetnote.onedrivechooser;

import android.support.v4.app.Fragment;

import com.romanpulov.violetnote.chooser.AbstractHrChooserActivity;

public class OneDriveChooserActivity extends AbstractHrChooserActivity {
    @Override
    protected Fragment createChooserFragment(String initialPath) {
        return OneDriveChooserFragment.newInstance(initialPath);
    }
}
