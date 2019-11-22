package com.romanpulov.violetnote.dropboxchooser;

import com.romanpulov.violetnote.chooser.AbstractHrChooserActivity;
import android.support.v4.app.Fragment;

/**
 * Activity for choosing file from Dropbox
 * Created by romanpulov on 02.07.2016.
 */
public class DropboxChooserActivity extends AbstractHrChooserActivity {

    @Override
    protected Fragment createChooserFragment(String initialPath) {
        return DropboxChooserFragment.newInstance(initialPath);
    }

}
