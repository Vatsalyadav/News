package com.vatsalyadav.apps.news.repository;

import android.util.Log;

import com.google.gson.Gson;
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
    private News newsList;

    public Flowable<News> getNewsList() {
        newsList = new News();
        return Flowable.create(new FlowableOnSubscribe<News>() {
            @Override
            public void subscribe(FlowableEmitter<News> emitter) throws Exception {
                emitter.onNext(getNewsFromUrl());
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
            Log.e("Repo success", "httpCall: " + responseString);
            newsList = new Gson().fromJson(responseString, News.class);
        } catch (Exception e) {
            newsList.setStatus(Constants.STATUS_FAILED);
            Log.e("Repo fail", "httpCall error: " + e.getStackTrace());
        } finally {
            urlConnection.disconnect();
        }
        return newsList;
    }

}