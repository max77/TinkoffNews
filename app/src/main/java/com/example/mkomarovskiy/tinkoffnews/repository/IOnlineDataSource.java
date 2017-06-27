package com.example.mkomarovskiy.tinkoffnews.repository;

import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;

import java.util.List;

import io.reactivex.Single;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public interface IOnlineDataSource {
    Single<List<INewsTitle>> getNewsTitleList();

    Single<INewsDetails> getNewsDetailsById(long id);
}
