package com.romanpulov.violetnote.model;

/**
 * Created by rpulov on 28.08.2016.
 */
public class BasicCommonNoteA extends BasicOrderedEntityNoteA {
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
