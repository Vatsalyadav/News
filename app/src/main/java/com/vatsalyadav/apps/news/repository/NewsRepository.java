package com.vatsalyadav.apps.news.repository;

import com.vatsalyadav.apps.news.model.Article;
import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.repository.localStorageNewsDeprecated.NewsDatabaseHelper;
import com.vatsalyadav.apps.news.requests.NewsApiClient;
import com.vatsalyadav.apps.news.util.Constants;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.schedulers.Schedulers;

public class NewsRepository {
    private NewsDatabaseHelper newsDatabaseHelper;
    private NewsApiClient newsApiClient;

    @Inject
    public NewsRepository(NewsDatabaseHelper newsDatabaseHelper, NewsApiClient newsApiClient) {
        this.newsDatabaseHelper = newsDatabaseHelper;
        this.newsApiClient = newsApiClient;
    }

    public Flowable<News> getNewsList(final boolean networkConnection) {
        return Flowable.create(new FlowableOnSubscribe<News>() {
            @Override
            public void subscribe(FlowableEmitter<News> emitter) throws Exception {
                emitter.onNext(networkConnection ? null : getNewsFromLocalStorage());
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io());
    }

    private News getNewsFromLocalStorage() {
        News newsList = new News();
        try {
            newsList.setArticle(newsDatabaseHelper.getData());
            newsList.setStatus(Constants.STATUS_OK);
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

    public void getNewsApi() {
        newsApiClient.getNewsApi();
    }

    public LiveData<News> getNews() {
        return newsApiClient.getNews();
    }
}