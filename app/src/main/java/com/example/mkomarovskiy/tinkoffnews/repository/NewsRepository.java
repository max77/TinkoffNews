package com.example.mkomarovskiy.tinkoffnews.repository;

import android.util.Log;

import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.RepositoryRequestResult;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public class NewsRepository implements INewsRepository {

    private static final String TAG = "TINKOFF::REPO";

    private IOnlineDataSource mOnlineDataSource;
    private ICacheDataSource mCacheDataSource;

    public NewsRepository(IOnlineDataSource onlineDataSource, ICacheDataSource cacheDataSource) {
        mOnlineDataSource = onlineDataSource;
        mCacheDataSource = cacheDataSource;
    }

    private boolean shouldReload() {
        return false;
    }

    @Override
    public Observable<RepositoryRequestResult<List<INewsTitle>>> getNewsTitleList(boolean forceReload) {
        return retrieveData(mOnlineDataSource
                        .getNewsTitleList()
                        .flatMap(newsTitles ->
                                mCacheDataSource.saveNewsTitleList(newsTitles)),
                mCacheDataSource
                        .getNewsTitleList(),
                forceReload,
                null);
    }

    @Override
    public Observable<RepositoryRequestResult<INewsDetails>> getNewsDetailsById(long id, boolean forceReload) {
        return retrieveData(mOnlineDataSource
                        .getNewsDetailsById(id)
                        .flatMap(newsDetails ->
                                mCacheDataSource.saveNewsDetails(newsDetails)),
                mCacheDataSource
                        .getNewsDetailsById(id),
                forceReload,
                null);
    }

    private <T> Observable<RepositoryRequestResult<T>> retrieveData(Single<T> readOnlineAndSaveToCacheSingle,
                                                                    Single<T> readCacheSingle,
                                                                    boolean forceReload,
                                                                    Throwable fallbackThrowable) {
        if (shouldReload() || forceReload)
            return Observable
                    .just(new RepositoryRequestResult<T>(null,
                            false,
                            -1,
                            null,
                            RepositoryRequestResult.STATUS_PROGRESS))
                    .concatWith(
                            readOnlineAndSaveToCacheSingle
                                    .map(data -> {
                                        Log.d(TAG, "Received online data");

                                        return new RepositoryRequestResult<>(data,
                                                false,
                                                -1,
                                                null,
                                                RepositoryRequestResult.STATUS_SUCCESS);
                                    })
                                    .compose(s ->
                                            fallbackThrowable == null ?
                                                    s.onErrorResumeNext(throwable -> {
                                                        Log.w(TAG, "Error receiving online data, trying cache!" + "(" + throwable + ")");

                                                        return Single.fromObservable(
                                                                retrieveData(readOnlineAndSaveToCacheSingle, readCacheSingle, false, throwable));
                                                    }) :
                                                    s)
                                    .onErrorResumeNext(throwable -> {
                                        Log.w(TAG, "Error receiving data!" + "(" + throwable + ")");

                                        return Single.just(new RepositoryRequestResult<>(null,
                                                true,
                                                -1,
                                                throwable.getMessage(),
                                                RepositoryRequestResult.STATUS_ERROR));
                                    })
                                    .toObservable());

        else
            return readCacheSingle
                    .map(data -> {
                        Log.d(TAG, "Cache read success");

                        return new RepositoryRequestResult<>(data,
                                true,
                                -1,
                                null,
                                RepositoryRequestResult.STATUS_SUCCESS);
                    })
                    .toObservable()
                    .compose(s ->
                            fallbackThrowable == null ?
                                    s.onErrorResumeNext(throwable -> {
                                        Log.w(TAG, "Error receiving cached data, trying online!" + "(" + throwable + ")");

                                        return retrieveData(readOnlineAndSaveToCacheSingle, readCacheSingle, true, throwable);
                                    }) :
                                    s);
    }
}
