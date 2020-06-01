package com.vatsalyadav.apps.news.repository.localStorageNewsDeprecated;

import android.provider.BaseColumns;

public class NewsReaderContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private NewsReaderContract() {
    }

    /* Inner class that defines the table contents */
    public static class NewsEntry implements BaseColumns {
        public static final String TABLE_NAME = "saved_news";
        public static final String COLUMN_NAME_ARTICLE_JSON = "article_json";
        public static final String COLUMN_NAME_ARTICLE_TITLE = "article_title";
        public static final String COLUMN_NAME_ARTICLE_URL = "article_url";
    }
}