package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class DashboardActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        Button passNoteButton = (Button)findViewById(R.id.pass_note_button);
        passNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashboardActivity.this, CategoryActivity.class);
                DashboardActivity.this.startActivity(intent);
            }
        });


        //startActivity(new Intent(this, CategoryActivity.class));
    }

}
