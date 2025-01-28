package com.romanpulov.violetnote;

import androidx.test.filters.SmallTest;

import org.junit.*;

@Ignore("Only needed on demand")
@SmallTest
public class DataGeneratorTest {
    @Test
    public void generateData() {
        DataGenerator d = new DataGenerator();
        d.generateData();
    }

}
