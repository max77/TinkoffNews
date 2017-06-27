package com.example.mkomarovskiy.tinkoffnews.datasource.online;

import com.google.gson.annotations.SerializedName;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 24/06/2017.
 */

class BaseResult<T> {
    private static final String RESULT_CODE_OK = "OK";

    @SerializedName("resultCode")
    private String mResultCode;

    @SerializedName("payload")
    private T mPayload;

    public String getResultCode() {
        return mResultCode;
    }

    public T getPayload() {
        return mPayload;
    }

    public boolean isOk() {
        return RESULT_CODE_OK.equals(mResultCode);
    }
}
