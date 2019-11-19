package com.romanpulov.violetnote.view.core;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.romanpulov.violetnote.R;

/**
 * Fragment displaying load error in password protected activity
 * Created by rpulov on 13.09.2016.
 */
public class LoadErrorFragment extends Fragment {
    private final static String ERROR_TEXT = "error_text";

    private String mErrorText;

    public static LoadErrorFragment createWithText(String errorText) {
        LoadErrorFragment instance = new LoadErrorFragment();

        Bundle args = new Bundle();
        args.putString(ERROR_TEXT, errorText);
        instance.setArguments(args);

        return instance;
    }

    public LoadErrorFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mErrorText = getArguments().getString(ERROR_TEXT);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_load_error, container, false);

        //set error text
        if ((mErrorText != null) && !(mErrorText.isEmpty())) {
            TextView loadStatusTextView = view.findViewById(R.id.load_status);
            loadStatusTextView.setText(mErrorText);
        }

        //setup reload button
        Button reloadButton = view.findViewById(R.id.reload);
        reloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof PasswordActivity)
                    ((PasswordActivity)getActivity()).reload();
            }
        });

        return view;
    }
}
