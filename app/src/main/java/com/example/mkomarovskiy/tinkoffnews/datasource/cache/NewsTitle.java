package com.example.mkomarovskiy.tinkoffnews.datasource.cache;

import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;

import java.util.Calendar;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 24/06/2017.
 */

class NewsTitle implements INewsTitle {
    private long mId;
    private String mName;
    private String mText;
    private Calendar mPublicationDate;

    public NewsTitle(long id, String name, String text, long publicationDate) {
        mId = id;
        mName = name;
        mText = text;
        mPublicationDate = Calendar.getInstance();
        mPublicationDate.setTimeInMillis(publicationDate);
    }

    @Override
    public long getId() {
        return mId;
    }

    @Override
    public String getInternalName() {
        return mName;
    }

    @Override
    public String getTitle() {
        return mText;
    }

    @Override
    public Calendar getPublicationDate() {
        return mPublicationDate;
    }
}
