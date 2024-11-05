package com.romanpulov.violetnote.view.action;

import java.util.Collection;

public interface UIAction<T extends Collection<?>> {
    void execute(T data);
}
