package com.vatsalyadav.apps.news.requests;

import android.util.Log;

import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.util.Constants;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

@Singleton
public class NewsApiClient {

    private static final String TAG = "NewsApiClient";
    @Inject
    Retrofit retrofit;
    private MutableLiveData<News> mNews;

    @Inject
    public NewsApiClient() {
        mNews = new MutableLiveData<>();
    }

    public LiveData<News> getNews() {
        return mNews;
    }

    public void getNewsApi() {
        News failedNews = new News();
        failedNews.setStatus(Constants.STATUS_FAILED);
        Call<News> newsCall = getNewsCall();
        try {
            newsCall.enqueue(new Callback<News>() {
                @Override
                public void onResponse(Call<News> call, Response<News> response) {
                    Log.d(TAG, "onResponse: Server Response: " + response.toString());
                    if (response.code() == 200 &&
                            response.body() != null &&
                            response.body().getStatus().equals(Constants.STATUS_OK)) {
                        mNews.postValue(response.body());
                    } else {
                        mNews.postValue(failedNews);
                    }
                }

                @Override
                public void onFailure(Call<News> call, Throwable t) {
                    mNews.postValue(failedNews);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Call<News> getNewsCall() {
        return retrofit.create(NewsApi.class).getNews(
                Constants.COUNTRY_CODE,
                Constants.API_KEY);
    }
}