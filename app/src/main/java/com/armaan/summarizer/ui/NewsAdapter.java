package com.armaan.summarizer.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.armaan.summarizer.models.Article;
import com.armaan.summarizer.models.Summary;
import com.armaan.summarizer.R;
import com.armaan.summarizer.summarizer.SummaryTool;
import com.armaan.summarizer.SummaryActivity;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<Article> articles;

    public NewsAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.news_list,null);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder newsViewHolder, int i) {
        final Article article = articles.get(i);
        newsViewHolder.titleText.setText(article.getTitle());
        newsViewHolder.descriptionText.setText(article.getDescription());
        newsViewHolder.sourceText.setText(article.getSource());
        Picasso.get().load(article.getUrlToImage()).into(newsViewHolder.imageView);
        newsViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SummaryActivity.class);
                intent.putExtra("summaryText",article.getUrl());
                intent.putExtra("image",article.getUrlToImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    class NewsViewHolder extends RecyclerView.ViewHolder{

        TextView titleText;
        TextView descriptionText;
        TextView sourceText;
        ImageView imageView;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleText = itemView.findViewById(R.id.newsListTitle);
            descriptionText = itemView.findViewById(R.id.newsListDescription);
            sourceText = itemView.findViewById(R.id.authorListText);
            imageView = itemView.findViewById(R.id.newsListThumbnail);
        }
    }

}