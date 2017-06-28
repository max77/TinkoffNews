package com.example.mkomarovskiy.tinkoffnews.datasource.cache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mkomarovskiy.tinkoffnews.model.INewsDetails;
import com.example.mkomarovskiy.tinkoffnews.model.INewsTitle;

import java.util.List;

/**
 * TinkoffNews
 * Created by mkomarovskiy on 28/06/2017.
 */

class DBHelper extends SQLiteOpenHelper {
    static final String DB_NAME = "tinkoff_cache.db";
    static final String TABLE_NAME = "news";
    static final String NEWS_COLUMN_ID = "id";
    static final String NEWS_COLUMN_TITLE = "title";
    static final String NEWS_COLUMN_INTERNAL_NAME = "int_name";
    static final String NEWS_COLUMN_CREATION_DATE = "date1";
    static final String NEWS_COLUMN_PUBLICATION_DATE = "date2";
    static final String NEWS_COLUMN_MODIFICATION_DATE = "date3";
    static final String NEWS_COLUMN_CONTENT = "content";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + NEWS_COLUMN_ID + " INTEGER PRIMARY KEY, "
                + NEWS_COLUMN_TITLE + " TEXT, "
                + NEWS_COLUMN_INTERNAL_NAME + " TEXT, "
                + NEWS_COLUMN_CREATION_DATE + " INTEGER, "
                + NEWS_COLUMN_PUBLICATION_DATE + " INTEGER, "
                + NEWS_COLUMN_MODIFICATION_DATE + " INTEGER, "
                + NEWS_COLUMN_CONTENT + " TEXT "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void updateOrInsertNewsTitles(List<INewsTitle> newsTitles) {
        SQLiteDatabase db = getWritableDatabase();

        db.beginTransaction();

        for (INewsTitle newsTitle : newsTitles) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(NEWS_COLUMN_ID, newsTitle.getId());
            contentValues.put(NEWS_COLUMN_INTERNAL_NAME, newsTitle.getInternalName());
            contentValues.put(NEWS_COLUMN_TITLE, newsTitle.getTitle());
            contentValues.put(NEWS_COLUMN_PUBLICATION_DATE, newsTitle.getPublicationDate().getTimeInMillis());

            db.insertWithOnConflict(TABLE_NAME,
                    null,
                    contentValues,
                    SQLiteDatabase.CONFLICT_REPLACE);
        }

        db.setTransactionSuccessful();
        db.endTransaction();
    }

    boolean updateOrInsertNewsDetails(INewsDetails newsDetails) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NEWS_COLUMN_CREATION_DATE, newsDetails.getCreationDate().getTimeInMillis());
        contentValues.put(NEWS_COLUMN_MODIFICATION_DATE, newsDetails.getModificationDate().getTimeInMillis());
        contentValues.put(NEWS_COLUMN_CONTENT, newsDetails.getContent());

        return db.update(TABLE_NAME,
                contentValues,
                NEWS_COLUMN_ID + " = ? ",
                new String[]{Long.toString(newsDetails.getId())}) > 0;
    }

    Cursor getAllData() {
        return getReadableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }

    void clearAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_NAME);
    }
}
