package com.vatsalyadav.apps.news.repository.localStorageNewsDeprecated;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.vatsalyadav.apps.news.model.Article;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class NewsDatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "NewsReader.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NewsReaderContract.NewsEntry.TABLE_NAME + " (" +
                    NewsReaderContract.NewsEntry._ID + " INTEGER PRIMARY KEY," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_TITLE + " TEXT," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_URL + " TEXT," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_JSON + " TEXT, UNIQUE(" +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_TITLE + "," +
                    NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_URL + "))";
    private static final String SQL_FETCH_ENTRIES =
            "SELECT * FROM " + NewsReaderContract.NewsEntry.TABLE_NAME;
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + NewsReaderContract.NewsEntry.TABLE_NAME;

    private Gson gson;

    public NewsDatabaseHelper(@Nullable Context context, Gson gson) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.gson = gson;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_TABLE);
    }

    public boolean insertNews(Article newsArticle) {
        // Gets the data repository in write mode
        SQLiteDatabase db = this.getWritableDatabase();
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        newsArticle.setArticleSaved(true);
        values.put(NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_TITLE, newsArticle.getTitle());
        values.put(NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_URL, newsArticle.getUrl());
        values.put(NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_JSON, gson.toJson(newsArticle));
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insertWithOnConflict(NewsReaderContract.NewsEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
        return newRowId != -1;
    }

    public List<Article> getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Article> articleList = new ArrayList<>();
        Cursor cursor = db.rawQuery(SQL_FETCH_ENTRIES, null);
        while (cursor.moveToNext()) {
            articleList.add(gson.fromJson(cursor.getString(3), Article.class));
        }
        cursor.close();
        return articleList;
    }

    public int deleteNewsArticle(Article article) {
        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(NewsReaderContract.NewsEntry.TABLE_NAME, NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_URL + "=? and " + NewsReaderContract.NewsEntry.COLUMN_NAME_ARTICLE_TITLE + "=?",
                new String[]{article.getUrl(), article.getTitle()});
        db.close();
        return delete;
    }
}
