package com.example.mkomarovskiy.tinkoffnews.ui;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 27/06/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }
}
