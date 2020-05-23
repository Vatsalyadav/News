package com.vatsalyadav.apps.news.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.vatsalyadav.apps.news.R;

import androidx.appcompat.app.ActionBar;
import dagger.android.support.DaggerAppCompatActivity;

public class NewsDetailsActivity extends DaggerAppCompatActivity {

    private static final String EXTRA_SELECTED_NEWS_PUBLISHER = "EXTRA_SELECTED_NEWS_PUBLISHER";
    private static final String EXTRA_NEWS_URL = "EXTRA_NEWS_URL";

    private String selectedNewsTitle;
    private String newsUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);
        getExtrasFromIntent(getIntent());
        ActionBar actionBar = getSupportActionBar();
        setActionBar(actionBar);
        initWebView(newsUrl);
    }

    private void getExtrasFromIntent(Intent intent) {
        selectedNewsTitle = intent.getStringExtra(EXTRA_SELECTED_NEWS_PUBLISHER);
        newsUrl = intent.getStringExtra(EXTRA_NEWS_URL);
    }

    // Create intent for NewsDetailsActivity with data to initiate it
    public static Intent newIntent(Context context, String articleTitle, String articleUrl) {
        Intent intent = new Intent(context, NewsDetailsActivity.class);
        intent.putExtra(EXTRA_SELECTED_NEWS_PUBLISHER, articleTitle);
        intent.putExtra(EXTRA_NEWS_URL, articleUrl);
        return intent;
    }

    private void setActionBar(ActionBar actionBar) {
        if (actionBar != null) {
            actionBar.setTitle(selectedNewsTitle);
            actionBar.setSubtitle(newsUrl);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initWebView(String url) {
        WebView webView = findViewById(R.id.web_view_news_details);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setDisplayZoomControls(false);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        supportFinishAfterTransition();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.share) {
            try {
                Intent intentShare = new Intent(Intent.ACTION_SEND);
                intentShare.setType("text/plan");
                String body = selectedNewsTitle + "\n" + newsUrl + "\n" + "Share from the News App" + "\n";
                intentShare.putExtra(Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(intentShare, "Share with :"));

            } catch (Exception e) {
                Toast.makeText(this, "Unable to share, please try again", Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_news_details, menu);
        return true;
    }

}
