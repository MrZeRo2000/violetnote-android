package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

import java.util.List;

public class DashboardActivity extends ActionBarCompatActivity {

    private void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_list);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        DBNoteManager noteManager = new DBNoteManager(this);
        List<BasicNoteGroupA> basicNoteGroupList = noteManager.mBasicNoteGroupDAO.getAll();

        recyclerView.setAdapter(new DashboardItemRecyclerViewAdapter(this, basicNoteGroupList));

        /*
        setContentView(R.layout.activity_dashboard);

        Button passNoteButton = findViewById(R.id.pass_note_button);
        passNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(CategoryActivity.class);
            }
        });

        Button basicNoteButton = findViewById(R.id.basic_note_button);
        basicNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(BasicNoteActivity.class);
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(SettingsActivity.class);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
