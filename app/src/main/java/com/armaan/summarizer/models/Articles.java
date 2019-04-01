package com.armaan.summarizer.models;

import java.util.List;

public class Articles {
    public List<Article> articles;

    public Articles(List<Article> articles) {
        this.articles = articles;
    }

    public List<Article> getArticles() {
        return articles;
    }
}
