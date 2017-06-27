package com.example.mkomarovskiy.tinkoffnews.datasource.online;

import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;

import java.util.Calendar;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 24/06/2017.
 */

class NewsTitle implements INewsTitle {
    private String id;
    private String name;
    private String text;
    private NewsDate publicationDate;

    private long mId = -1;
    private Calendar mPublicationDate;

    @Override
    public long getId() {
        if (mId == -1) {
            try {
                mId = Long.valueOf(id);
            } catch (Exception e) {
            }
        }

        return mId;
    }

    @Override
    public String getInternalName() {
        return name;
    }

    @Override
    public String getTitle() {
        return text;
    }

    @Override
    public Calendar getPublicationDate() {
        if (mPublicationDate == null) {
            mPublicationDate = Calendar.getInstance();
            mPublicationDate.setTimeInMillis(publicationDate.getMilliseconds());
        }

        return mPublicationDate;
    }
}
