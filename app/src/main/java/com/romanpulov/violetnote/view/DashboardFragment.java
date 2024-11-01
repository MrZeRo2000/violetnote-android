package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import com.romanpulov.violetnote.databinding.FragmentDashboardBinding;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.BasicNoteGroupViewModel;

public class DashboardFragment extends Fragment implements OnBasicGroupInteractionListener {
    private FragmentDashboardBinding binding;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int orientation = getResources().getConfiguration().orientation;
        int gridSpanCount =  orientation == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2;
        binding.list.setLayoutManager(new GridLayoutManager(getContext(), gridSpanCount));

        BasicNoteGroupViewModel model = new ViewModelProvider(this).get(BasicNoteGroupViewModel.class);
        model.getAllWithTotals().observe(this, basicNoteGroupList ->
                binding.list.setAdapter(new DashboardItemRecyclerViewAdapter(basicNoteGroupList, this)));
    }

    @Override
    public void onBasicGroupSelection(BasicNoteGroupA item) {
        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            startActivity(new Intent(requireActivity(), PassDataHostActivity.class));
        } else if (item.getGroupType() == BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE) {
            Intent intent = new Intent(requireActivity(), BasicNoteActivity.class);
            intent.putExtra(BasicNoteGroupA.BASIC_NOTE_GROUP_DATA, item);
            startActivity(intent);
        }
    }
}