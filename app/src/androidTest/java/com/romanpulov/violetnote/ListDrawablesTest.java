package com.romanpulov.violetnote;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.filters.SmallTest;
import android.util.Log;

import org.junit.Test;

import java.lang.reflect.Field;

import static android.support.test.InstrumentationRegistry.getTargetContext;

@SmallTest
public class ListDrawablesTest {

    @Test
    public void test1() {
        Context context = getTargetContext();
        Resources resources = context.getResources();

        for (Field f: R.drawable.class.getDeclaredFields()) {
            String name = f.getName();
            if (name.contains("img")) {
                int resId = resources.getIdentifier(name, "drawable", context.getPackageName());
                Log.d("ListDrawablesTest", "Name:" + name + ",id:" + resId);
            }
        }
    }
}
