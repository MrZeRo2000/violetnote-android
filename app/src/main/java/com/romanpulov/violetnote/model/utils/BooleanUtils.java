package com.romanpulov.violetnote.model.utils;

/**
 * Boolean data conversion utils
 * Created by romanpulov on 22.06.2017.
 */

public class BooleanUtils {
    public static boolean fromInt(int value) {
        switch (value) {
            case 0: return false;
            case 1: return true;
            default: throw new IllegalArgumentException();
        }
    }

    public static int toInt(boolean value){
        return value ? 1 : 0;
    }
}
