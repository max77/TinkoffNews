package com.example.mkomarovskiy.tinkoffnews.datasource.online;

import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;

import java.util.Calendar;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

class NewsDetails implements INewsDetails {
    private NewsTitle title;
    private NewsDate creationDate;
    private NewsDate lastModificationDate;
    private String content;

    private Calendar mCreationDate;
    private Calendar mModificationDate;

    @Override
    public long getId() {
        return title.getId();
    }

    @Override
    public String getTitle() {
        return title.getTitle();
    }

    @Override
    public Calendar getCreationDate() {
        if (mCreationDate == null) {
            mCreationDate = Calendar.getInstance();
            mCreationDate.setTimeInMillis(creationDate.getMilliseconds());
        }

        return mCreationDate;
    }

    @Override
    public Calendar getModificationDate() {
        if (mModificationDate == null) {
            mModificationDate = Calendar.getInstance();
            mModificationDate.setTimeInMillis(lastModificationDate.getMilliseconds());
        }

        return mModificationDate;
    }

    @Override
    public String getContent() {
        return content;
    }
}
