package com.example.mkomarovskiy.tinkoffnews.ui.details;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String EXTRA_NEWS_ID = "newsid";

    public static void show(Context context, String newsId) {
        context.startActivity(new Intent(context, NewsDetailsActivity.class)
                .putExtra(EXTRA_NEWS_ID, newsId));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            finish();
            return;
        }

        if (savedInstanceState == null) {
            String id = getIntent().getStringExtra(EXTRA_NEWS_ID);
            if (id == null)
                return;

            NewsDetailsFragment details = NewsDetailsFragment.newInstance(id);
            getFragmentManager().beginTransaction().add(android.R.id.content, details).commit();
        }
    }
}