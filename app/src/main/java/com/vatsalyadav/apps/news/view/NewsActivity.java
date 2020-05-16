package com.vatsalyadav.apps.news.view;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vatsalyadav.apps.news.R;
import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.viewmodel.NewsResource;
import com.vatsalyadav.apps.news.viewmodel.NewsViewModel;
import com.vatsalyadav.apps.news.viewmodel.ViewModelProviderFactory;

import javax.inject.Inject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.android.support.DaggerAppCompatActivity;

public class NewsActivity extends DaggerAppCompatActivity {

    @Inject
    ViewModelProviderFactory providerFactory;
    private NewsViewModel viewModel;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        progressBar = findViewById(R.id.progress_bar);
        viewModel = new ViewModelProvider(this, providerFactory).get(NewsViewModel.class);
        getNewsList();
    }

    private void getNewsList() {
        showProgressBar(true);
        viewModel.getNewsList()
                .observe(this, new Observer<NewsResource<News>>() {
                    @Override
                    public void onChanged(NewsResource<News> newsNewsResource) {
                        switch (newsNewsResource.status) {
                            case SUCCESS:
                                showProgressBar(false);
                                Toast.makeText(NewsActivity.this, "Login Success " + newsNewsResource.data.getArticle().get(0).getAuthor(), Toast.LENGTH_LONG).show();
                                Log.d("NewsActivity", "onChanged: Login Success" + newsNewsResource.data.getArticle().get(0).getAuthor());
                                break;
                            case ERROR:
                                showProgressBar(false);
                                Toast.makeText(NewsActivity.this, "Failed to fetch", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
    }

    private void showProgressBar(boolean isVisible) {
        progressBar.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }
}
