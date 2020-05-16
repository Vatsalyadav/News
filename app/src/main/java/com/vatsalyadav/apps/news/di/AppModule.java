package com.vatsalyadav.apps.news.di;

import android.app.Application;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.RequestOptions;
import com.vatsalyadav.apps.news.repository.NewsRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    // App level module which will have Application level dependencies like Repository, Glide Instance

    @Singleton
    @Provides
    static RequestOptions provideRequestOptions() {
//        return RequestOptions
//                .placeholderOf(R.drawable.placeholder)
//                .error(R.drawable.placeholder);
        return null;
    }

    @Singleton
    @Provides
    static RequestManager provideGlideInstance(Application application, RequestOptions requestOptions) {
        return Glide.with(application)
                .setDefaultRequestOptions(requestOptions);
    }

    @Singleton
    @Provides
    static NewsRepository provideNewsRepository() {
        return new NewsRepository();
    }
}
