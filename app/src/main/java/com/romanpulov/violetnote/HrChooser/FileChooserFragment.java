package com.romanpulov.violetnote.HrChooser;

import android.os.Bundle;

import java.io.File;

/**
 * Created by romanpulov on 07.06.2016.
 */
public class FileChooserFragment extends HrChooserFragment {

    @Override
    protected ChooseItem getChooseItem() {
        return new FileChooseItem(new File(mInitialPath));
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new FileChooserFragment();
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }

}
