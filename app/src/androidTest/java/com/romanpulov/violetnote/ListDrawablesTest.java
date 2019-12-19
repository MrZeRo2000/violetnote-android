package com.romanpulov.violetnote;

import android.content.Context;
import android.content.res.Resources;
import androidx.test.filters.SmallTest;

import android.util.Log;

import org.junit.Test;

import java.lang.reflect.Field;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

@SmallTest
public class ListDrawablesTest {

    private Context getTargetContext() {
        return getInstrumentation().getTargetContext();
    }

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
