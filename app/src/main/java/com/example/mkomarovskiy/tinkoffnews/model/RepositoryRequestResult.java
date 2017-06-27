package com.example.mkomarovskiy.tinkoffnews.model;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 26/06/2017.
 */

public class RepositoryRequestResult<T> {
    public static final int STATUS_PROGRESS = 1;
    public static final int STATUS_SUCCESS = 2;
    public static final int STATUS_ERROR = 3;

    private T mPayload;
    private boolean isCached;
    private int mErrorCode;
    private String mErrorMessage;
    private int mStatus;

    public RepositoryRequestResult(T payload, boolean isCached, int errorCode, String errorMessage, int status) {
        mPayload = payload;
        this.isCached = isCached;
        mErrorCode = errorCode;
        mErrorMessage = errorMessage;
        mStatus = status;
    }

    public RepositoryRequestResult(int status) {
        mStatus = status;
    }

    public T getPayload() {
        return mPayload;
    }

    public boolean isCached() {
        return isCached;
    }

    public int getErrorCode() {
        return mErrorCode;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

    public int getStatus() {
        return mStatus;
    }

    public boolean isInProgress() {
        return mStatus == STATUS_PROGRESS;
    }

    public boolean isSuccess() {
        return mStatus == STATUS_SUCCESS;
    }

    public boolean isError() {
        return mStatus == STATUS_ERROR;
    }
}
