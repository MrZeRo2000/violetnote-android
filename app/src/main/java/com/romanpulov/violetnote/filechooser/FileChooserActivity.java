package com.romanpulov.violetnote.filechooser;

import android.support.v4.app.Fragment;

import com.romanpulov.violetnote.chooser.AbstractHrChooserActivity;

public class FileChooserActivity extends AbstractHrChooserActivity {
    @Override
    protected Fragment createChooserFragment(String initialPath) {
        return FileChooserFragment.newInstance(initialPath);
    }
}
