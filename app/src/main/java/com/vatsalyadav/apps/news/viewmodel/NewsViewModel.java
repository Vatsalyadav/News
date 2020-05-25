package com.vatsalyadav.apps.news.viewmodel;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.vatsalyadav.apps.news.model.Article;
import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.repository.NewsRepository;
import com.vatsalyadav.apps.news.util.Constants;

import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.LiveDataReactiveStreams;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class NewsViewModel extends ViewModel {

    private static final String TAG = "NewsViewModel";
    @Inject
    Application application;
    private NewsRepository repository;
    private MutableLiveData<List<Article>> articleList = new MutableLiveData<>();
    private MutableLiveData<Boolean> saveNewsSuccess = new MutableLiveData<>();
    private LiveData<NewsResource<News>> newsResourceLiveData;

    @Inject
    public NewsViewModel(NewsRepository newsRepository) {
        this.repository = newsRepository;
    }

    public void init() {
        if (newsResourceLiveData != null) {
            return;
        }
        newsResourceLiveData = fetchNewsList();
    }

    public LiveData<NewsResource<News>> getNewsList() {
        return newsResourceLiveData;
    }

    private LiveData<NewsResource<News>> fetchNewsList() {
        return LiveDataReactiveStreams.fromPublisher(
                repository.getNewsList(isNetworkConnected())
                        .onErrorReturn(new Function<Throwable, News>() {
                            @Override
                            public News apply(Throwable throwable) throws Exception {
                                News errorNews = new News();
                                errorNews.setStatus(Constants.STATUS_FAILED);
                                return errorNews;
                            }
                        })
                        .map(new Function<News, NewsResource<News>>() {
                            @Override
                            public NewsResource<News> apply(News news) throws Exception {
                                if (news.getStatus().equals(Constants.STATUS_FAILED)) {
                                    return NewsResource.error("Could not authenticate", null);
                                }
                                articleList.postValue(news.getArticle());
                                return NewsResource.success(news);
                            }
                        })
                        .subscribeOn(Schedulers.io())
        );
    }

    public LiveData<Boolean> saveNewsItem(int position) {
        boolean isSuccessfullySaved = repository.saveNewsItem(articleList.getValue().get(position));
        saveNewsSuccess.postValue(isSuccessfullySaved);
        if (isSuccessfullySaved) {
            saveWebViewCache(articleList.getValue().get(position).getUrl());
        }
        return saveNewsSuccess;
    }

    public boolean deleteNewsArticle(Article article) {
        int result = repository.deleteNewsArticle(article);
        return result != -1;
    }

    private void saveWebViewCache(String url) {
        if (isNetworkConnected()) {
            WebView webView = new WebView(application.getApplicationContext());
            webView.getSettings().setLoadsImagesAutomatically(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setAppCachePath(application.getCacheDir().getAbsolutePath());
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setAppCacheEnabled(true);
            webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
            webView.setWebViewClient(new WebViewClient());
            webView.loadUrl(url);
        }
    }

    public boolean isNetworkConnected() {
        boolean result = false;
        ConnectivityManager cm = (ConnectivityManager) application.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (cm != null) {
                NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
                if (capabilities != null) {
                    // connected to the internet
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        result = true;
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        result = true;
                    }
                }
            }
        } else {
            if (cm != null) {
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if (activeNetwork != null) {
                    // connected to the internet
                    if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                        result = true;
                    } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                        result = true;
                    }
                }
            }
        }
        return result;
    }
}