package com.example.mkomarovskiy.tinkoffnews.repository;

import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.model.RepositoryRequestResult;

import java.util.List;

import io.reactivex.Observable;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 27/06/2017.
 */

public interface INewsRepository {
    Observable<RepositoryRequestResult<List<INewsTitle>>> getNewsTitleList(boolean forceReload);

    Observable<RepositoryRequestResult<INewsDetails>> getNewsDetailsById(long id, boolean forceReload);
}
