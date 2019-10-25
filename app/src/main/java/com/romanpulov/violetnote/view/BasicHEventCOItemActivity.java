package com.romanpulov.violetnote.view;

import android.view.MenuItem;

import com.romanpulov.violetnote.R;
import com.romanpulov.violetnote.view.core.ActionBarCompatActivity;

public class BasicHEventCOItemActivity extends ActionBarCompatActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            // Workaround for issue with back button in toolbar default behavior
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
