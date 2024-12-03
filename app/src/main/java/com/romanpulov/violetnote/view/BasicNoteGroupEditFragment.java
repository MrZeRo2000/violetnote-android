package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteBinding;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import org.jetbrains.annotations.NotNull;

public class BasicNoteGroupEditFragment extends Fragment {
    private final static String TAG = BasicNoteGroupEditFragment.class.getSimpleName();

    public BasicNoteGroupEditFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentBasicNoteBinding binding = FragmentBasicNoteBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BasicNoteGroupA editItem = BasicNoteGroupEditFragmentArgs.fromBundle(getArguments()).getEditItem();
        Log.d(TAG, "onViewCreated with edit item: " + editItem);
        if (editItem != null) {
            requireActivity().setTitle(getString(R.string.title_activity_basic_note_group_edit, editItem.getDisplayTitle()));
        }
    }
}