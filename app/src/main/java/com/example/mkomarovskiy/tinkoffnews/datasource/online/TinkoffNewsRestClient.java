package com.example.mkomarovskiy.tinkoffnews.datasource.online;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 26/06/2017.
 */

public interface TinkoffNewsRestClient {
    @GET("/v1/news")
    Single<NewsBlock> getNewsTitleList();

    @GET("/v1/news_content?id={id}")
    Single<NewsDetails> getNewsDetailsById(@Path("id") long id);
}
