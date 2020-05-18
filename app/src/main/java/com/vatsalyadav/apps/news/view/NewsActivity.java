package com.vatsalyadav.apps.news.view;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vatsalyadav.apps.news.R;
import com.vatsalyadav.apps.news.adapter.NewsListAdapter;
import com.vatsalyadav.apps.news.model.Article;
import com.vatsalyadav.apps.news.model.News;
import com.vatsalyadav.apps.news.repository.localStorageNews.NewsDatabaseHelper;
import com.vatsalyadav.apps.news.util.Constants;
import com.vatsalyadav.apps.news.viewmodel.NewsResource;
import com.vatsalyadav.apps.news.viewmodel.NewsViewModel;
import com.vatsalyadav.apps.news.viewmodel.ViewModelProviderFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dagger.android.support.DaggerAppCompatActivity;

public class NewsActivity extends DaggerAppCompatActivity implements NewsListAdapter.OnNewsClickListener {

    @Inject
    ViewModelProviderFactory providerFactory;
    private NewsViewModel viewModel;
    private ProgressBar progressBar;

    @Inject
    NewsDatabaseHelper newsDatabaseHelper;

    private RecyclerView recyclerView;
    private List<Article> articles = new ArrayList<>();
    private NewsListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        progressBar = findViewById(R.id.progress_bar);
        viewModel = new ViewModelProvider(this, providerFactory).get(NewsViewModel.class);
        getNewsList();
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
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
                                if (newsNewsResource.data != null && newsNewsResource.data.getStatus().equals(Constants.STATUS_OK)) {
                                    if (!articles.isEmpty()) {
                                        articles.clear();
                                    }
                                    articles = newsNewsResource.data.getArticle();
                                    adapter = new NewsListAdapter(articles, NewsActivity.this, NewsActivity.this);
                                    recyclerView.setAdapter(adapter);
                                    adapter.notifyDataSetChanged();
                                }
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

    @Override
    public void onNewsClick(int position) {
        Toast.makeText(this, "NEWS CLICK: " + position, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSaveClick(int position) {
        boolean savedState = articles.get(position).getArticleSaved();
        if (!savedState) {
            viewModel.saveNewsItem(position).observe(this, new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean isSuccessfullySaved) {
                    if (isSuccessfullySaved) {
                        Toast.makeText(getBaseContext(), "News Saved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getBaseContext(), "Failed to Save News", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            articles.get(position).setArticleSaved(true);
            adapter.notifyItemChanged(position);
        } else {
            viewModel.deleteNewsArticle(articles.get(position));
            if (viewModel.isNetworkConnected()) {
                articles.get(position).setArticleSaved(false);
                adapter.notifyItemChanged(position);
            } else {
                articles.remove(position);
                adapter.notifyItemRemoved(position);
            }

        }
    }
}