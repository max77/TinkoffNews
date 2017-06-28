package com.example.mkomarovskiy.tinkoffnews;

import android.app.Application;

import com.example.mkomarovskiy.tinkoffnews.datasource.cache.TinkoffNewsLocalDBDataSource;
import com.example.mkomarovskiy.tinkoffnews.datasource.online.TinkoffNewsOnlineDataSource;
import com.example.mkomarovskiy.tinkoffnews.repository.INewsRepository;
import com.example.mkomarovskiy.tinkoffnews.repository.NewsRepository;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 27/06/2017.
 */

public class TinkoffNewsApplication extends Application {
    private INewsRepository mNewsRepository;

    @Override
    public void onCreate() {
        super.onCreate();

        mNewsRepository = new NewsRepository(
                new TinkoffNewsOnlineDataSource(BuildConfig.BACKEND_URL),
                new TinkoffNewsLocalDBDataSource(this)
        );
    }

    public INewsRepository getNewsRepository() {
        return mNewsRepository;
    }
}
