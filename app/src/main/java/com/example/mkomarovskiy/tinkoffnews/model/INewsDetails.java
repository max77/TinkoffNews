package com.example.mkomarovskiy.tinkoffnews.model;

import java.util.Calendar;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public interface INewsDetails {
    long getId();

    String getTitle();

    Calendar getCreationDate();

    Calendar getModificationDate();

    String getContent();
}
