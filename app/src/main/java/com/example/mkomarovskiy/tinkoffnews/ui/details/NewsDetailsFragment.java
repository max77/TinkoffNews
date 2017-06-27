package com.example.mkomarovskiy.tinkoffnews.ui.details;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.WebViewFragment;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 27/06/2017.
 */

public class NewsDetailsFragment extends WebViewFragment {
    private static final String KEY_NEWS_ID = "newsid";

    public static NewsDetailsFragment newInstance(String id) {

        Bundle args = new Bundle();
        args.putString(KEY_NEWS_ID, id);

        NewsDetailsFragment fragment = new NewsDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String id = getArguments().getString(KEY_NEWS_ID);
    }
}
