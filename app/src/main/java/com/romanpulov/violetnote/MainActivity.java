package com.romanpulov.violetnote;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;



import java.util.ArrayList;

public class MainActivity extends ActionBarCompatActivity implements CategoryFragment.OnListFragmentInteractionListener {
    public static final boolean mSampleData = false;

    public static final String PASS_DATA = "PassData";
    public static final String PASS_CATEGORY_ITEM = "PassCategoryItem";
    public static final String PASS_NOTE_DATA = "PassNoteData";

    private PassDataA mPassDataA;

    private boolean mResumeFromChild = false;
    private boolean mRestoreState = false;

    private void refreshFragment() {
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
        String mPassword;

        public LoadPassDataAsyncTask(String password) {
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle(R.string.caption_loading);
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            mPassDataA = Document.loadPassDataA(Document.DEF_FILE_NAME, mPassword);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            progressDialog.dismiss();
            refreshFragment();
        }
    }

    private void requestPassword() {
        PasswordInputDialog passwordInputDialog = new PasswordInputDialog(this);
        passwordInputDialog.setOnPasswordInputListener(new PasswordInputDialog.OnPasswordInputListener() {
            @Override
            public void onPasswordInput(String password) {
                if (password != null) {
                    if ((mPassDataA != null) && (mPassDataA.getPassword().equals(password))) {
                        refreshFragment();
                    } else {
                        new LoadPassDataAsyncTask(password).execute();
                    }
                }
            }
        });
        passwordInputDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        if (mSampleData) {
            mPassDataA = Document.loadSamplePassData();
            refreshFragment();
        } else {
            if (!mResumeFromChild) {

                if (!mRestoreState) {
                    FragmentManager fm = getSupportFragmentManager();
                    Fragment fragment = fm.findFragmentById(R.id.fragment_container);
                    if (fragment != null) {
                        fm.beginTransaction().remove(fragment).commit();
                    }

                    requestPassword();
                } else
                    mRestoreState = false;
            } else {
                mResumeFromChild = false;
                if (mPassDataA == null) {
                    refreshFragment();
                }
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PASS_DATA, mPassDataA);
        Log.d("MainActivity", "OnSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mPassDataA = savedInstanceState.getParcelable(PASS_DATA);
        mRestoreState = true;
        Log.d("MainActivity", "OnRestoreInstanceState");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
