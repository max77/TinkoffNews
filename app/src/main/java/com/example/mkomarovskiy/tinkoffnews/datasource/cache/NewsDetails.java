package com.example.mkomarovskiy.tinkoffnews.datasource.cache;

import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;

import java.util.Calendar;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

class NewsDetails implements INewsDetails {
    private long mId;
    private String mTitle;
    private Calendar mCreationDate;
    private Calendar mModificationDate;
    private String mContent;

    public NewsDetails(long id, long creationDate, long modificationDate, String title, String content) {
        mId = id;
        mTitle = title;
        mCreationDate = Calendar.getInstance();
        mCreationDate.setTimeInMillis(creationDate);
        mModificationDate = Calendar.getInstance();
        mModificationDate.setTimeInMillis(modificationDate);
        mContent = content;
    }

    @Override
    public long getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    @Override
    public Calendar getCreationDate() {
        return mCreationDate;
    }

    @Override
    public Calendar getModificationDate() {
        return mModificationDate;
    }

    @Override
    public String getContent() {
        return mContent;
    }
}
