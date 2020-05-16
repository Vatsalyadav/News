package com.vatsalyadav.apps.news.di;

import com.vatsalyadav.apps.news.viewmodel.ViewModelProviderFactory;

import androidx.lifecycle.ViewModelProvider;
import dagger.Binds;
import dagger.Module;

@Module
public abstract class ViewModelFactoryModule {
    // Responsible for doing dependency for ViewModelFactory class

    @Binds // Provides instance of ViewModelProviderFactory
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelProviderFactory modelProviderFactory);
}
