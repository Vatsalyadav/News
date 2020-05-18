package com.vatsalyadav.apps.news.di;

import com.vatsalyadav.apps.news.view.NewsActivity;
import com.vatsalyadav.apps.news.view.NewsDetailsActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
            modules = {NewsViewModelsModule.class}
    )
    abstract NewsActivity contributeNewsActivity();

    @ContributesAndroidInjector
    abstract NewsDetailsActivity contributeNewsDetailsActivity();
}