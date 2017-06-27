package com.example.mkomarovskiy.tinkoffnews.datasource.cache;

import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.repository.ICacheDataSource;

import java.util.List;

import io.reactivex.Single;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public class TinkoffNewsLocalDBDataSource implements ICacheDataSource {
    @Override
    public Single<List<INewsTitle>> getNewsTitleList() {
        return Single.fromCallable(() -> {
            //TODO: read from DB
            return null;
        });
    }

    @Override
    public Single<List<INewsTitle>> saveNewsTitleList(List<INewsTitle> newsTitles) {
        return Single.fromCallable(() -> {
            //TODO: save to DB
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
