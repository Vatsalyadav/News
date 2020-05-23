package com.vatsalyadav.apps.news.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.button.MaterialButton;
import com.vatsalyadav.apps.news.R;
import com.vatsalyadav.apps.news.model.Article;
import com.vatsalyadav.apps.news.util.Utils;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;


public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {

    private List<Article> articles;
    private Context context;
    private OnNewsClickListener onNewsClickListener;

    public NewsListAdapter(List<Article> articles, Context context, OnNewsClickListener onNewsClickListener) {
        this.articles = articles;
        this.context = context;
        this.onNewsClickListener = onNewsClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_news, parent, false);
        return new ViewHolder(view, onNewsClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holders, int position) {
        Article article = articles.get(position);
        holders.bindTo(article);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface OnNewsClickListener {
        void onNewsClick(int position);

        void onSaveClick(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, desc, author, published_ad, source;
        ImageView imageView;
        ProgressBar progressBar;
        MaterialButton buttonSave;

        public ViewHolder(View itemView, final OnNewsClickListener onNewsClickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNewsClickListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        onNewsClickListener.onNewsClick(getAdapterPosition());
                    }
                }
            });
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            author = itemView.findViewById(R.id.author);
            published_ad = itemView.findViewById(R.id.publishedAt);
            source = itemView.findViewById(R.id.source);
            imageView = itemView.findViewById(R.id.img);
            progressBar = itemView.findViewById(R.id.prograss_load_photo);
            buttonSave = itemView.findViewById(R.id.button_save);
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onNewsClickListener != null) {
                        onNewsClickListener.onSaveClick(getAdapterPosition());
                    }
                }
            });
        }

        public void bindTo(Article article) {

            RequestOptions requestOptions = new RequestOptions()
                    .placeholder(new ColorDrawable(Color.parseColor("#f9f9fa")))
                    .error(new ColorDrawable(Color.parseColor("#f9f9fa")))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop();

            Glide.with(context)
                    .load(article.getUrlToImage())
                    .apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);

            title.setText(article.getTitle());
            desc.setText(article.getDescription());
            source.setText(article.getSource().getName());
            published_ad.setText(Utils.dateFormat(article.getPublishedAt()));
            author.setText(article.getAuthor());
            buttonSave.setIcon(article.getArticleSaved() ? itemView.getResources().getDrawable(R.drawable.ic_love_pink) : itemView.getResources().getDrawable(R.drawable.ic_love_grey));
        }

    }
}
