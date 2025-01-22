package com.romanpulov.violetnote.view;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuHost;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentDashboardBinding;
import com.romanpulov.violetnote.model.vm.AppViewModel;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.model.vm.BasicNoteGroupViewModel;
import org.jetbrains.annotations.NotNull;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private BasicNoteGroupViewModel model;
    private AppViewModel appViewModel;

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

        MenuHost menuHost = requireActivity();
        menuHost.addMenuProvider(
                new MenuProvider() {
                    @Override
                    public void onCreateMenu(@NotNull Menu menu, @NotNull MenuInflater menuInflater) {
                        menuInflater.inflate(R.menu.dashboard, menu);
                    }

                    @Override
                    public boolean onMenuItemSelected(@NotNull MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_settings) {
                            NavHostFragment.findNavController(DashboardFragment.this).navigate(
                                    DashboardFragmentDirections.actionDashboardToSettings());
                            return true;
                        } else {
                            return false;
                        }
                    }
                }, getViewLifecycleOwner(), Lifecycle.State.RESUMED
        );

        model = new ViewModelProvider(requireActivity()).get(BasicNoteGroupViewModel.class);
        appViewModel = new ViewModelProvider(requireActivity()).get(AppViewModel.class);

        model.getAllWithTotals().observe(this, basicNoteGroupList ->
                binding.list.setAdapter(new DashboardItemRecyclerViewAdapter(basicNoteGroupList, this::onBasicGroupSelection)));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Boolean.TRUE.equals(appViewModel.getNoteGroupsChanged().getValue())) {
            model.loadAllWithTotals();
            appViewModel.getNoteGroupsChanged().setValue(false);
        }
    }

    public void onBasicGroupSelection(BasicNoteGroupA item) {
        if (item.getGroupType() == BasicNoteGroupA.PASSWORD_NOTE_GROUP_TYPE) {
            startActivity(new Intent(requireActivity(), PassDataHostActivity.class));
        } else if (item.getGroupType() == BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE) {
            NavHostFragment.findNavController(this).navigate(
                    DashboardFragmentDirections.actionDashboardToBasicNote().setNoteGroup(item));
        }
    }
}