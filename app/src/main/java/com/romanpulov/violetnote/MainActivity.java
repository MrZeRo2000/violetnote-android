package com.romanpulov.violetnote;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.romanpulov.violetnotecore.Model.Model1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Model1 model = new Model1();
        model.method1();
    }
}
