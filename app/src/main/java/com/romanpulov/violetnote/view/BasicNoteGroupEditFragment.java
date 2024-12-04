package com.romanpulov.violetnote.view;

import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.navigation.Navigation;
import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.databinding.FragmentBasicNoteGroupEditBinding;
import com.romanpulov.violetnote.model.BasicNoteGroupA;
import com.romanpulov.violetnote.view.helper.DrawableSelectionHelper;

import java.util.Objects;

public class BasicNoteGroupEditFragment extends Fragment {
    private final static String TAG = BasicNoteGroupEditFragment.class.getSimpleName();

    public static final String RESULT_KEY = BasicNoteGroupEditFragment.class.getName() + "_RESULT_KEY";
    public static final String RESULT_VALUE_KEY = BasicNoteGroupEditFragment.class.getName() + "_RESULT_VALUE_KEY";

    private FragmentBasicNoteGroupEditBinding binding;

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
        binding = FragmentBasicNoteGroupEditBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final BasicNoteGroupA editItem = BasicNoteGroupEditFragmentArgs.fromBundle(getArguments()).getEditItem();
        Log.d(TAG, "onViewCreated with edit item: " + editItem);

        binding.imgSelector.setAdapter(new BasicNoteGroupImageAdapter(requireContext(),
                R.layout.view_img_selector_spinner,
                DrawableSelectionHelper.getDrawableList())
        );

        if (editItem == null) {
            binding.imgSelector.setSelection(0);
            binding.titleEditText.requestFocus();
        } else {
            Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar())
                    .setTitle(getString(R.string.title_activity_basic_note_group_edit, editItem.getDisplayTitle()));

            binding.titleEditText.setText(editItem.getGroupName());

            int drawable = DrawableSelectionHelper.getDrawableForNoteGroup(editItem);
            int position = DrawableSelectionHelper.getDrawablePosition(drawable);
            binding.imgSelector.setSelection(position);

            binding.showTotal.setChecked(editItem.getDisplayOptions().getTotalFlag());
            binding.showUnchecked.setChecked(editItem.getDisplayOptions().getUncheckedFlag());
            binding.showChecked.setChecked(editItem.getDisplayOptions().getCheckedFlag());
        }

        binding.okButton.setOnClickListener(v -> {
            if (binding.titleEditText.getText().toString().trim().isEmpty()) {
                binding.titleEditText.setError(this.getString(R.string.error_field_not_empty));
            } else {
                BasicNoteGroupA newItem = editItem == null ?
                        BasicNoteGroupA.newEditInstance(BasicNoteGroupA.BASIC_NOTE_GROUP_TYPE, null, 0) :
                        editItem.cloneInstance();

                newItem.setGroupName(binding.titleEditText.getText().toString().trim());
                newItem.setGroupIcon(
                        DrawableSelectionHelper.getGroupIconByPosition(binding.imgSelector.getSelectedItemPosition())
                );

                newItem.getDisplayOptions().setTotalFlag(binding.showTotal.isChecked());
                newItem.getDisplayOptions().setUncheckedFlag(binding.showUnchecked.isChecked());
                newItem.getDisplayOptions().setCheckedFlag(binding.showChecked.isChecked());

                Bundle result = new Bundle();
                result.putParcelable(RESULT_VALUE_KEY, newItem);
                BasicNoteGroupEditFragment.this.getParentFragmentManager().setFragmentResult(RESULT_KEY, result);

                Navigation.findNavController(BasicNoteGroupEditFragment.this.requireView()).navigateUp();
            }
        });
    }
}