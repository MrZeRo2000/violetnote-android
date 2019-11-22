package com.romanpulov.violetnote.onedrivechooser;

import android.os.Bundle;

import com.romanpulov.violetnote.chooser.ChooseItem;
import com.romanpulov.violetnote.chooser.HrChooserFragment;

public class OneDriveChooserFragment extends HrChooserFragment {
    @Override
    protected ChooseItem getChooseItem() {
        return null;
    }

    public OneDriveChooserFragment() {
        super();
        setRetainInstance(true);
    }

    public static HrChooserFragment newInstance(String initialPath) {
        HrChooserFragment fragment = new OneDriveChooserFragment();
        Bundle args = new Bundle();
        args.putString(HR_CHOOSER_INITIAL_PATH, initialPath);
        fragment.setArguments(args);
        return fragment;
    }
}
