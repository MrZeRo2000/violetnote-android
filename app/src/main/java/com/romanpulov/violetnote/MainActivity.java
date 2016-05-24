package com.romanpulov.violetnote;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.romanpulov.violetnotecore.Model.*;

import java.util.ArrayList;

public class MainActivity extends ActionBarCompatActivity implements CategoryFragment.OnListFragmentInteractionListener {

    public static final String PASS_CATEGORY_ITEM = "PassCategoryItem";
    public static final String PASS_NOTE_DATA = "PassNoteData";

    private PassDataA mPassDataA;

    private boolean mResumeFromChild = false;

    private void loadData() {
        PassData passData = Document.getInstance().getPassData();
        if (passData != null)
            mPassDataA = PassDataA.fromPassData(passData);
    }

    private void updateData() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment != null) {
            fm.beginTransaction().remove(fragment).commit();
        }

        fragment = CategoryFragment.newInstance((ArrayList<PassCategoryA>)mPassDataA.getPassCategoryData());
        fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onListFragmentInteraction(PassCategoryA item) {
        if (item.getNotesCount() > 0) {
            Intent intent = new Intent(this, NoteActivity.class);
            intent.putExtra(PASS_CATEGORY_ITEM, item);
            intent.putParcelableArrayListExtra(PASS_NOTE_DATA, (ArrayList<PassNoteA>) mPassDataA.getPassNoteData(item));
            startActivityForResult(intent, 0);
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

                }
            });
            passwordInputDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        ActionBar actionBar = getSupportActionBar();
        actionBar.setIcon(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        */
        setContentView(R.layout.activity_fragment_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "OnActivityResult:" + resultCode);
        mResumeFromChild = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "Document.getPassData=" + Document.getInstance().getPassData());
        if (Document.getInstance().getPassData() != null) {
            Log.d("MainActivity", "Document.getPassCategoryList=" + Document.getInstance().getPassData().getPassCategoryList());
            Log.d("MainActivity", "Document.getPassNoteList=" + Document.getInstance().getPassData().getPassNoteList());
        }

        if (!mResumeFromChild)  {
            mResumeFromChild = false;

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);
            if (fragment != null) {
                fm.beginTransaction().remove(fragment).commit();
            }

            requestPassword();
        } else {
            if (mPassDataA == null) {
                loadData();
                updateData();
            }
        }

        //loadSamplePassData();
        //loadData();
        //updateData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity", "OnPause: passData=" + Document.getInstance().getPassData());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("MainActivity", "OnSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d("MainActivity", "OnRestoreInstanceState");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
