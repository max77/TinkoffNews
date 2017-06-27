package com.example.mkomarovskiy.tinkoffnews.datasource.online;

import android.util.Log;

import com.example.mkomarovskiy.tinkoffnews.BuildConfig;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;
import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.repository.IOnlineDataSource;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public class TinkoffNewsOnlineDataSource implements IOnlineDataSource {
    private TinkoffNewsRestClient mRestClient;

    public TinkoffNewsOnlineDataSource(String baseUrl) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            Interceptor loggingInterceptor =
                    new HttpLoggingInterceptor(message -> Log.d("TINKOFF::REST", message))
                            .setLevel(HttpLoggingInterceptor.Level.BODY);
            clientBuilder.addInterceptor(loggingInterceptor);
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .client(clientBuilder.build())
                .build();

        mRestClient = retrofit.create(TinkoffNewsRestClient.class);
    }

    @Override
    public Single<List<INewsTitle>> getNewsTitleList() {
        return mRestClient
                .getNewsTitleList()
                .map(newsBlock -> new ArrayList<INewsTitle>(newsBlock.getPayload()));
    }

    @Override
    public Single<INewsDetails> getNewsDetailsById(long id) {
        return mRestClient
                .getNewsDetailsById(id)
                .cast(INewsDetails.class);
    }
}
