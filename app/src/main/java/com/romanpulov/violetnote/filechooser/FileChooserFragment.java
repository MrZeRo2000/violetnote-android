package com.romanpulov.violetnote.filechooser;

import android.os.Bundle;

import com.romanpulov.violetnote.chooser.AbstractChooseItem;
import com.romanpulov.violetnote.chooser.HrChooserFragment;

import java.io.File;

/**
 * Fragment for choosing file from device
 * Created by romanpulov on 07.06.2016.
 */
public class FileChooserFragment extends HrChooserFragment {

    @Override
    protected AbstractChooseItem getChooseItem() {
        File file = new File(mInitialPath);
        if (file.isFile()) {
            File parentFile = file.getParentFile();
            if (parentFile != null)
                return new FileChooseItem(parentFile);
        }
        return new FileChooseItem(file);
    }

    @Override
    protected void requestChooseItem(AbstractChooseItem item) {
        updateChooseItem(fillChooseItem(item));
    }

    public FileChooserFragment() {
        super();
        setRetainInstance(true);
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new FileChooserFragment();
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }

}
