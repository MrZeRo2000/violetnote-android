package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.db.manager.DBNoteManager;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

import java.util.List;

public class BasicNoteGroupActivity extends ActionBarCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_basic_note_group_list);

        RecyclerView recyclerView = findViewById(R.id.list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DBNoteManager noteManager = new DBNoteManager(this);
        List<BasicNoteGroupA> basicNoteGroupList = noteManager.mBasicNoteGroupDAO.getByGroupType(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE);

        recyclerView.setAdapter(new BasicNoteGroupItemRecyclerViewAdapter(basicNoteGroupList));
    }
}
