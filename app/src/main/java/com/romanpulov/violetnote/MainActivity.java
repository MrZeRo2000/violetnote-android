package com.romanpulov.violetnote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.romanpulov.violetnotecore.Model.*;

import com.romanpulov.violetnotecore.AESCrypt.*;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;

import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;

    private ExpandableListView elv;
    private PassDataExpListAdapter adapter;

    private ExpandableListView getElv() {
        if (elv == null) {
            View v = fragment.getView();
            if (v != null) {
                elv = (ExpandableListView)v.findViewById(R.id.pd_list_view);
            }
        }

        return elv;
    }

    private PassDataExpListAdapter getAdapter() {
        if (getElv() != null) {
            return (PassDataExpListAdapter)(elv.getExpandableListAdapter());
        } else
            return null;
    }

    private PassDataExp data;

    private void loadData() {
        data = PassDataExp.newInstance(Document.getInstance().getPassData());
    }

    private void updateData() {
        PassDataExpListAdapter a = getAdapter();
        if (a != null) {
            a.setData(data);
            a.notifyDataSetChanged();
        }
    }

    private class LoadPassDataAsyncTask extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(R.string.caption_loading);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            return Document.getInstance().loadPassData(Document.DEF_FILE_NAME, mPassword);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            loadData();
            updateData();
        }
    }

    private String mPassword;

    private void resetPassword() {
        mPassword = null;
    }

    private void loadPassData() {
        new LoadPassDataAsyncTask().execute();
    }

    private void loadSamplePassData() {
        PassData result = new PassData();

        PassCategory pc = new PassCategory("Category 1");
        result.getPassCategoryList().add(pc);

        PassNote pn = new PassNote(pc, "System 1", "User 1", "Password 1", null, null, null);
        result.getPassNoteList().add(pn);
        pn = new PassNote(pc, "System 12", "User 12", "Password 12", null, null, null);
        result.getPassNoteList().add(pn);
        pn = new PassNote(pc, "System 13", "User 13", "Password 13", null, null, null);
        result.getPassNoteList().add(pn);

        pc = new PassCategory("Category 2");
        result.getPassCategoryList().add(pc);

        pn = new PassNote(pc, "System 2", "User 2", "Password 2", null, null, null);
        result.getPassNoteList().add(pn);

        Document.getInstance().setPassData(result);
    }

    private void requestPassword() {
        if (mPassword == null) {
            PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
            passwordInputDialog.setOnPasswordInputListener(new PasswordInputDialog.OnPasswordInputListener() {
                @Override
                public void onPasswordInput(String password) {
                    if (password == null) {
                        finish();
                    }
                    mPassword = password;
                    loadPassData();
                }
            });
            passwordInputDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_fragment_main);

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new PassDataListFragment();
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }

        /*
        try { 
            AESCryptService service = new AESCryptService();
            InputStream input =service.generateCryptInputStream(new FileInputStream(Environment.getExternalStorageDirectory() + "/temp/1.vnf"), "Password1");
            PassData pd = (new XMLPassDataReader()).readStream(input);
            Toast.makeText(getApplicationContext(), pd.getPassCategoryList().toString() + " - " + pd.getPassNoteList().toString(), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //TODO: unlock reset password, lock loadSamplePassData
        //requestPassword();
        loadSamplePassData();
        loadData();
        updateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetPassword();
    }
}
