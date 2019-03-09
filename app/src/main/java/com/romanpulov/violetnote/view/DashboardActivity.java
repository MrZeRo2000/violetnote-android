package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

import java.util.List;

public class DashboardActivity extends ActionBarCompatActivity implements OnBasicGroupInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_list);

        RecyclerView recyclerView = findViewById(R.id.list);
        int orientation = getResources().getConfiguration().orientation;
        int gridSpanCount =  orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, gridSpanCount));

        DBNoteManager noteManager = new DBNoteManager(this);
        List<BasicNoteGroupA> basicNoteGroupList = noteManager.mBasicNoteGroupDAO.getAll();

        recyclerView.setAdapter(new DashboardItemRecyclerViewAdapter(basicNoteGroupList, this));
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
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBasicGroupSelection(BasicNoteGroupA item) {
        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            startActivity(new Intent(this, CategoryActivity.class));
        } else if (item.getGroupType() == BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE) {
            Intent intent = new Intent(this, BasicNoteActivity.class);
            intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, item);
            startActivity(intent);
        }
    }
}
