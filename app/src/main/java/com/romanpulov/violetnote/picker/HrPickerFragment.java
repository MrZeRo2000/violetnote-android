package com.romanpulov.violetnote.picker;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.cloud.CloudAccountFacadeFactory;
import com.romanpulov.violetnote.databinding.FragmentHrPickerBinding;
import org.jetbrains.annotations.NotNull;

public class HrPickerFragment extends Fragment {
    private static final String TAG = HrPickerFragment.class.getSimpleName();

    public static final String RESULT_KEY = HrPickerFragment.class.getSimpleName() + "_RESULT";
    public static final String RESULT_VALUE_KEY = HrPickerFragment.class.getSimpleName() + "_RESULT_VALUE";

    private FragmentHrPickerBinding binding;
    private HrPickerViewModel model;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mTextView;
        final View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;
            mTextView = v.findViewById(R.id.hr_picker_text);
        }
    }

    public class HrPickerAdapter extends RecyclerView.Adapter<HrPickerFragment.ViewHolder> {
        private final HrPickerScreen mHrPickerScreen;

        HrPickerAdapter(HrPickerScreen hrPickerScreen) {
            mHrPickerScreen = hrPickerScreen;
        }

        @Override
        @NonNull
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hr_picker_list_item, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final @NonNull ViewHolder holder, final int position) {
            switch (mHrPickerScreen.getItems().get(position).itemType) {
                case HrPickerItem.ITEM_TYPE_PARENT:
                    holder.mTextView.setText(null);
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_more_horiz, 0, 0, 0);
                    break;
                case HrPickerItem.ITEM_TYPE_FOLDER:
                    holder.mTextView.setText(mHrPickerScreen.getItems().get(position).name);
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_folder_closed, 0, 0, 0);
                    break;
                case HrPickerItem.ITEM_TYPE_FILE:
                    holder.mTextView.setText(mHrPickerScreen.getItems().get(position).name);
                    holder.mTextView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_action_document, 0, 0, 0);
                    break;
                default:
                    holder.mTextView.setText(mHrPickerScreen.getItems().get(position).name);
                    break;
            }

            holder.mView.setOnClickListener(view -> {
                HrPickerItem selectedItem = mHrPickerScreen.getItems().get(holder.getBindingAdapterPosition());

                switch (selectedItem.itemType) {
                    case HrPickerItem.ITEM_TYPE_FILE:
                        Bundle result = new Bundle();
                        result.putString(RESULT_VALUE_KEY,
                                HrPickerScreen.combinePath(mHrPickerScreen.getCurrentPath(), selectedItem));
                        HrPickerFragment.this.getParentFragmentManager().setFragmentResult(RESULT_KEY, result);

                        Navigation.findNavController(HrPickerFragment.this.requireView()).navigateUp();

                        break;
                    case HrPickerItem.ITEM_TYPE_PARENT:
                        HrPickerFragment.this.model.navigate(
                                getContext(),
                                mHrPickerScreen.getParentPath(),
                                selectedItem
                                );
                        break;
                    case HrPickerItem.ITEM_TYPE_FOLDER:
                        HrPickerFragment.this.model.navigate(
                                getContext(),
                                mHrPickerScreen.getCurrentPath(),
                                selectedItem
                        );
                        break;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mHrPickerScreen.getItems().size();
        }
    }


    public HrPickerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHrPickerBinding.inflate(getLayoutInflater());
        View v =  binding.getRoot();

        if (getActivity() != null) {
            binding.pickerList.setLayoutManager(new LinearLayoutManager(getActivity()));
        }

        model = new ViewModelProvider(this).get(HrPickerViewModel.class);

        model.getHrPickerScreen().observe(getViewLifecycleOwner(), hrPickerScreen -> {
            RecyclerView.Adapter<?> adapter = new HrPickerAdapter(hrPickerScreen);
            binding.pickerList.setAdapter(adapter);

            Log.d(TAG, "getHrPickerScreen observe:" + hrPickerScreen);

            binding.indeterminateBar.setVisibility(hrPickerScreen.getStatus() ==
                    HrPickerScreen.PICKER_SCREEN_STATUS_LOADING ? View.VISIBLE : View.GONE);

            binding.pickerErrorText.setText(hrPickerScreen.getErrorMessage());
            binding.pickerErrorText.setVisibility(hrPickerScreen.getStatus() ==
                    HrPickerScreen.PICKER_SCREEN_STATUS_ERROR ? View.VISIBLE : View.GONE);

            binding.pickerHeader.setText(hrPickerScreen.getCurrentPath());

            binding.pickerList.setVisibility(hrPickerScreen.getStatus() ==
                    HrPickerScreen.PICKER_SCREEN_STATUS_LOADING ? View.GONE : View.VISIBLE);

        });

        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int sourceType = HrPickerFragmentArgs.fromBundle(getArguments()).getSourceType();
        HrPickerNavigator navigator = CloudAccountFacadeFactory
                .fromDocumentSourceType(sourceType)
                .getHrPickerNavigator();
        model.setNavigator(navigator);

        String initialPath = HrPickerFragmentArgs.fromBundle(getArguments()).getInitialPath();

        if (!model.getHrPickerScreen().isInitialized()) {
            model.getHrPickerScreen().setValue(new HrPickerScreen(initialPath));
            model.navigate(requireContext(), initialPath, null);
        }
    }
}