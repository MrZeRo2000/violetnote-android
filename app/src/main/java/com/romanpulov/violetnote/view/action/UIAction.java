package com.romanpulov.violetnote.view.action;

import java.util.List;

public interface UIAction<T> {
    void execute(List<T> data);
}
