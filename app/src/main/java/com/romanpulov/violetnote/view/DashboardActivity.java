package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.ActivityDashboardListBinding;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.loader.document.DocumentPassDataLoader;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

import java.util.List;

public class DashboardActivity extends ActionBarCompatActivity implements OnBasicGroupInteractionListener {

    private ActivityDashboardListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityDashboardListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int orientation = getResources().getConfiguration().orientation;
        int gridSpanCount =  orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        binding.list.setLayoutManager(new GridLayoutManager(this, gridSpanCount));
    }

    @Override
    protected void onResume() {
        super.onResume();
        DBNoteManager noteManager = new DBNoteManager(this);
        List<BasicNoteGroupA> basicNoteGroupList = noteManager.mBasicNoteGroupDAO.getAllWithTotals(
                DocumentPassDataLoader.getDocumentFile(this) == null
        );
        binding.list.setAdapter(new DashboardItemRecyclerViewAdapter(basicNoteGroupList, this));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBasicGroupSelection(BasicNoteGroupA item) {
        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            startActivity(new Intent(this, PassDataHostActivity.class));
        } else if (item.getGroupType() == BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE) {
            Intent intent = new Intent(this, BasicNoteActivity.class);
            intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, item);
            startActivity(intent);
        }
    }
}
