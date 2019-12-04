package com.romanpulov.violetnote.onedrivechooser;

import androidx.fragment.app.Fragment;

import com.romanpulov.violetnote.chooser.AbstractHrChooserActivity;

public class OneDriveChooserActivity extends AbstractHrChooserActivity {
    @Override
    protected Fragment createChooserFragment(String initialPath) {
        return OneDriveChooserFragment.newInstance(initialPath);
    }
}
