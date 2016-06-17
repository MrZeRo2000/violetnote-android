package com.romanpulov.violetnote.chooser;

import java.io.File;

/**
 * Created by romanpulov on 07.06.2016.
 */
public class FileChooseItemFactory implements ChooseItemFactory {
    @Override
    public ChooseItem buildChooseItem(String initialPath) {
        return new FileChooseItem(new File(initialPath));
    }
}
