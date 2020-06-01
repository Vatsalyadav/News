package com.vatsalyadav.apps.news.repository;

import com.google.gson.Gson;
import com.vatsalyadav.apps.news.model.Article;
import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.util.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
//    private NewsDatabaseHelper newsDatabaseHelper;

//    public NewsRepository() {
//        this.newsDatabaseHelper = newsDatabaseHelper;
//    }

    public Flowable<News> getNewsList(final boolean networkConnection) {
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
        News newsList = new News();
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
        News newsList = new News();
        try {
//            newsList.setArticle(newsDatabaseHelper.getData());
            newsList.setStatus(Constants.STATUS_OK);
        } catch (Exception e) {
            newsList.setStatus(Constants.STATUS_FAILED);
        }
        return newsList;
    }

    public boolean saveNewsItem(Article saveArticle) {
//        return newsDatabaseHelper.insertNews(saveArticle);
        return true; // TODO: Temporary return, replace with ROOM saveNewsItem implementation
    }

    public int deleteNewsArticle(Article deleteArticle) {
//        return newsDatabaseHelper.deleteNewsArticle(deleteArticle);
        return 1; // TODO: Temporary return, replace with ROOM deleteNewsArticle implementation
    }

}