package com.romanpulov.violetnote;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.romanpulov.violetnotecore.Model.*;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CategoryFragment.OnListFragmentInteractionListener {

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

    private PassDataExp mData;
    private PassData mPassData;
    private PassDataA mPassDataA;

    private void loadData() {
        mPassData = Document.getInstance().getPassData();
        mData = PassDataExp.newInstance(mPassData);
        mPassDataA = PassDataA.fromPassData(mPassData);
    }

    private void updateData() {
        /*
        PassDataExpListAdapter a = getAdapter();
        if (a != null) {
            a.setData(mData);
            a.notifyDataSetChanged();
        }
        */

        FragmentManager fm = getSupportFragmentManager();
        fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = new CategoryFragment();
            Bundle args = new Bundle();
            args.putParcelableArrayList(CategoryFragment.PASS_CATEGORY_DATA, (ArrayList<PassCategoryA>)mPassDataA.getPassCategoryData());
            fragment.setArguments(args);
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onListFragmentInteraction(PassCategoryA item) {
        Toast.makeText(this, "Pressed item " + item.getCategoryName(), Toast.LENGTH_SHORT).show();
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

        for (int i = 3; i < 30; i ++) {
            result.getPassCategoryList().add(new PassCategory("Category " + i));
        }

        Document.getInstance().setPassData(result);
    }

    private void requestPassword() {
        if (mPassword == null) {
            PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
            passwordInputDialog.setOnPasswordInputListener(new PasswordInputDialog.OnPasswordInputListener() {
                @Override
                public void onPasswordInput(String password) {
                    mPassword = password;
                    loadPassData();
                    //loadData();
                    //updateData();

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

        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);


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
        Log.d("MainActivity", "OnResume");
        //TODO: unlock reset password, lock loadSamplePassData
        //requestPassword();
        loadSamplePassData();
        loadData();
        updateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "OnPause");
        resetPassword();
        Document.getInstance().resetData();
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().remove(fragment).commit();
            fragment = null;
        }
    }
}
