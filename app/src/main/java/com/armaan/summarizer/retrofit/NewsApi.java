package com.armaan.summarizer.retrofit;

import com.armaan.summarizer.models.Articles;

import retrofit2.Call;
import retrofit2.http.GET;

public interface NewsApi {
    @GET("top-headlines?sources=techcrunch&apiKey=368ddf85ef624bd1927f273ca934723f")
    Call<Articles> getArticles();
}
