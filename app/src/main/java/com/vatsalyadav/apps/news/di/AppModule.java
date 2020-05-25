package com.vatsalyadav.apps.news.di;

import android.app.Application;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.google.gson.Gson;
import com.vatsalyadav.apps.news.repository.NewsRepository;
import com.vatsalyadav.apps.news.repository.localStorageNews.NewsDatabaseHelper;
import com.vatsalyadav.apps.news.util.NetworkUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    // App level module which will have Application level dependencies like Repository, Glide Instance

    @Singleton
    @Provides
    static NewsRepository provideNewsRepository(NewsDatabaseHelper newsDatabaseHelper) {
        return new NewsRepository(newsDatabaseHelper);
    }

    @Singleton
    @Provides
    static NewsDatabaseHelper provideNewsDatabaseHelper(Application application, Gson gson) {
        return new NewsDatabaseHelper(application, gson);
    }

    @Singleton
    @Provides
    static NetworkUtil provideNetworkUtil(Application application) {
        return new NetworkUtil(application);
    }

    @Singleton
    @Provides
    static WebView provideWebView(Application application) {
        WebView webView = new WebView(application.getApplicationContext());
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setAppCachePath(application.getCacheDir().getAbsolutePath());
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient());
        return webView;
    }

    @Singleton
    @Provides
    static Gson provideGson() {
        return new Gson();
    }
}
