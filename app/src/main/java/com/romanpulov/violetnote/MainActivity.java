package com.romanpulov.violetnote;

import android.content.DialogInterface;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.widget.EditText;
import android.widget.Toast;

import com.romanpulov.violetnotecore.Model.*;

import com.romanpulov.violetnotecore.AESCrypt.*;
import com.romanpulov.violetnotecore.Processor.XMLPassDataReader;

import java.io.FileInputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private String mPassword;

    private void resetPassword() {
        mPassword = null;
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
                    Toast.makeText(getApplicationContext(), "Password: " + password, Toast.LENGTH_SHORT).show();
                }
            });
            passwordInputDialog.show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPassword();

        /*
        setContentView(R.layout.activity_main);
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
        resetPassword();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        requestPassword();
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestPassword();
    }

    @Override
    protected void onPause() {
        super.onPause();
        resetPassword();
    }
}
