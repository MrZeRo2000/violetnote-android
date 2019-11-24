package com.romanpulov.violetnote.chooser;

public class ChooseItemUtils {
    public static String getParentItemPath(String path) {
        int iPath = path.lastIndexOf("/");
        if (iPath>-1)
            return path.substring(0, iPath);
        else
            return "";
    }
}
