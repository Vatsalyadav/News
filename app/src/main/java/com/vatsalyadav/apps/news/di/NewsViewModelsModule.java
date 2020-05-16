package com.vatsalyadav.apps.news.di;

import com.vatsalyadav.apps.news.viewmodel.NewsViewModel;

import androidx.lifecycle.ViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class NewsViewModelsModule {
    // Responsible for dependency for AuthViewModelsModule itself

    @Binds
    @IntoMap
    @ViewModelKey(NewsViewModel.class)
    public abstract ViewModel bindAuthViewModel(NewsViewModel viewModel);
}
