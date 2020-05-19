package com.vatsalyadav.apps.news.di;

import android.app.Application;

import com.vatsalyadav.apps.news.repository.NewsRepository;
import com.vatsalyadav.apps.news.repository.localStorageNews.NewsDatabaseHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    // App level module which will have Application level dependencies like Repository, Glide Instance

    @Singleton
    @Provides
    static NewsRepository provideNewsRepository() {
        return new NewsRepository();
    }

    @Singleton
    @Provides
    static NewsDatabaseHelper provideNewsDatabaseHelper(Application application) {
        return new NewsDatabaseHelper(application);
    }

}
