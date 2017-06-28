package com.example.mkomarovskiy.tinkoffnews.datasource.cache;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.repository.ICacheDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Single;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public class TinkoffNewsLocalDBDataSource implements ICacheDataSource {
    private static final String TAG = "TINKOFF::CACHE";

    private DBHelper mDBHelper;

    public TinkoffNewsLocalDBDataSource(Context context) {
        mDBHelper = new DBHelper(context);
    }

    @Override
    public Single<Void> clearCache() {
        return Single.fromCallable(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                Log.d(TAG, "Clearing cache...");

                mDBHelper.clearAllData();
                return null;
            }
        });
    }

    @Override
    public Single<List<INewsTitle>> getNewsTitleList() {
        return Single.fromCallable(() -> {
            Log.d(TAG, "Getting titles from cache...");

            List<INewsTitle> newsTitles = null;
            Cursor cursor = mDBHelper.getAllData();

            int idxId = cursor.getColumnIndex(DBHelper.NEWS_COLUMN_ID);
            int idxInternalNme = cursor.getColumnIndex(DBHelper.NEWS_COLUMN_INTERNAL_NAME);
            int idxTitle = cursor.getColumnIndex(DBHelper.NEWS_COLUMN_TITLE);
            int idxPubDate = cursor.getColumnIndex(DBHelper.NEWS_COLUMN_PUBLICATION_DATE);

            if (cursor.moveToFirst()) {
                newsTitles = new ArrayList<>();

                do {
                    newsTitles.add(
                            new NewsTitle(cursor.getInt(idxId),
                                    cursor.getString(idxInternalNme),
                                    cursor.getString(idxTitle),
                                    cursor.getLong(idxPubDate)));
                } while (cursor.moveToNext());
            }

            cursor.close();
            return newsTitles;
        });
    }

    @Override
    public Single<List<INewsTitle>> saveNewsTitleList(List<INewsTitle> newsTitles) {
        return Single.fromCallable(() -> {
            Log.d(TAG, "Saving news titles to cache...");
            mDBHelper.updateOrInsertNewsTitles(newsTitles);
            Log.d(TAG, "Saving news titles to cache... done");

            return newsTitles;
        });
    }

    @Override
    public Single<INewsDetails> getNewsDetailsById(long id) {
        return Single.fromCallable(() -> {
            //TODO: read from DB
            return null;
        });
    }

    @Override
    public Single<INewsDetails> saveNewsDetails(INewsDetails newsDetails) {
        return Single.fromCallable(() -> {
            //TODO: save to DB
            return newsDetails;
        });
    }
}
