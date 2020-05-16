package com.vatsalyadav.apps.news.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NewsResource<T> {

    @NonNull
    public final NewsStatus status;

    @Nullable
    public final T data;

    @Nullable
    public final String message;


    public NewsResource(@NonNull NewsStatus status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> NewsResource<T> success(@Nullable T data) {
        return new NewsResource<>(NewsStatus.SUCCESS, data, null);
    }

    public static <T> NewsResource<T> error(@NonNull String msg, @Nullable T data) {
        return new NewsResource<>(NewsStatus.ERROR, data, msg);
    }

    public enum NewsStatus {SUCCESS, ERROR}

}