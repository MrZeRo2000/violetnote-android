package com.romanpulov.violetnote.HrChooser;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.romanpulov.violetnote.ActionBarCompatActivity;
import com.romanpulov.violetnote.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HrChooserActivity extends ActionBarCompatActivity implements HrChooserFragment.OnChooserInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_hr_chooser);

        String initialPath = getIntent().getStringExtra(HrChooserFragment.HR_CHOOSER_INITIAL_PATH);

        Fragment fragment = HrChooserFragment.newInstance(initialPath);
        getSupportFragmentManager().beginTransaction().add(R.id.hr_fragment_container, fragment).commit();
    }

    @Override
    public void onChooserInteraction(ChooseItem item) {
        Toast.makeText(this, item.toString(), Toast.LENGTH_SHORT).show();
    }
}
