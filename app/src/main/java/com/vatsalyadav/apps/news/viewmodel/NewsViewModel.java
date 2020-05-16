package com.vatsalyadav.apps.news.viewmodel;

import android.util.Log;

import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.repository.NewsRepository;
import com.vatsalyadav.apps.news.util.Constants;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.ViewModel;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsViewModel extends ViewModel {

    private static final String TAG = "NewsViewModel";
    private NewsRepository instance;

    @Inject
    public NewsViewModel(NewsRepository newsRepository) { // Requirement for multi binding
        Log.d("NewsViewModel", "NewsViewModel: is working");
        this.instance = newsRepository;
    }

    public LiveData<NewsResource<News>> getNewsList() { //query user id and do RxJava stuff
        return LiveDataReactiveStreams.fromPublisher(
                instance.getNewsList()
                        .onErrorReturn(new Function<Throwable, News>() {
                            @Override
                            public News apply(Throwable throwable) throws Exception {
                                News errorNews = new News();
                                Log.e(TAG, "apply: onErrorReturn");
                                errorNews.setStatus(Constants.STATUS_FAILED);
                                return errorNews;
                            }
                        })
                        .map(new Function<News, NewsResource<News>>() {
                            @Override
                            public NewsResource<News> apply(News news) throws Exception {
                                Log.e(TAG, "apply: map" + news.getStatus());
                                if (news.getStatus().equals(Constants.STATUS_FAILED)) {
                                    Log.e(TAG, "apply: map news failed");
                                    return NewsResource.error("Could not authenticate", null);
                                }
                                return NewsResource.success(news);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );
    }

}
