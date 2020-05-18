package com.vatsalyadav.apps.news.repository;

import android.database.Cursor;

import com.google.gson.Gson;
import com.vatsalyadav.apps.news.model.Article;
import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.repository.localStorageNews.NewsDatabaseHelper;
import com.vatsalyadav.apps.news.util.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
    private News newsList;
    private NewsDatabaseHelper newsDatabaseHelper;

    public void setNewsDatabaseHelper(NewsDatabaseHelper newsDatabaseHelper) {
        this.newsDatabaseHelper = newsDatabaseHelper;
    }

    public Flowable<News> getNewsList(final boolean networkConnection) {
        newsList = new News();
        return Flowable.create(new FlowableOnSubscribe<News>() {
            @Override
            public void subscribe(FlowableEmitter<News> emitter) throws Exception {
                emitter.onNext(networkConnection ? getNewsFromUrl() : getNewsFromLocalStorage());
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());
    }

    private News getNewsFromUrl() {
        URL url;
        String responseString;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(Constants.BASE_URL);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            responseString = result.toString();
            newsList = new Gson().fromJson(responseString, News.class);
        } catch (Exception e) {
            newsList.setStatus(Constants.STATUS_FAILED);
        } finally {
            urlConnection.disconnect();
        }
        return newsList;
    }

    private News getNewsFromLocalStorage() {
        List<Article> articleList = new ArrayList<>();
        try {
            Cursor cursor = newsDatabaseHelper.getData();
            while (cursor.moveToNext()) {
                articleList.add(new Gson().fromJson(cursor.getString(3), Article.class));
            }
            newsList.setStatus(Constants.STATUS_OK);
            newsList.setArticle(articleList);
            cursor.close();
        } catch (Exception e) {
            newsList.setStatus(Constants.STATUS_FAILED);
        }
        return newsList;
    }

    public boolean saveNewsItem(Article saveArticle) {
        return newsDatabaseHelper.insertNews(saveArticle);
    }

    public int deleteNewsArticle(Article deleteArticle) {
        return newsDatabaseHelper.deleteNewsArticle(deleteArticle);
    }

}