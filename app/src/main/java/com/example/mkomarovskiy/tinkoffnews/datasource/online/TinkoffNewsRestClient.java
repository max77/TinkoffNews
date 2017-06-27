package com.example.mkomarovskiy.tinkoffnews.datasource.online;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 26/06/2017.
 */

public interface TinkoffNewsRestClient {
    @GET("/v1/news")
    Single<BaseResponse<List<NewsTitle>>> getNewsTitleList();

    @GET("/v1/news_content")
    Single<BaseResponse<NewsDetails>> getNewsDetailsById(@Query("id") long id);
}
