package com.example.mkomarovskiy.tinkoffnews.repository;

import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;

import java.util.List;

import io.reactivex.Single;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public interface ICacheDataSource {
    Single<Void> clearCache();

    Single<List<INewsTitle>> getNewsTitleList();

    Single<List<INewsTitle>> saveNewsTitleList(List<INewsTitle> newsTitles);

    Single<INewsDetails> getNewsDetailsById(long id);

    Single<INewsDetails> saveNewsDetails(INewsDetails newsDetails);
}
