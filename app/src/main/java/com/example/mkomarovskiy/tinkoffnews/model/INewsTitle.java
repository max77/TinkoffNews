package com.example.mkomarovskiy.tinkoffnews.model;

import java.util.Calendar;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 25/06/2017.
 */

public interface INewsTitle {
    long getId();

    String getInternalName();

    String getTitle();

    Calendar getPublicationDate();
}
