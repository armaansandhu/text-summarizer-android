package com.armaan.summarizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.armaan.summarizer.models.Article;
import com.armaan.summarizer.models.Articles;
import com.armaan.summarizer.models.Summary;
import com.armaan.summarizer.retrofit.NewsApi;
import com.armaan.summarizer.ui.NewsAdapter;
import com.armaan.summarizer.utils.Utils;
import com.facebook.shimmer.ShimmerFrameLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public void favourites(View view) {
        Intent intent = new Intent(this,FavouriteSummaries.class);
        startActivity(intent);
    }

    EditText urlInput;
    RecyclerView listView;

    void linkSummarize(View view){
        String text = urlInput.getText().toString();
        String url = extractUrls(text);
        if(handleUrlError(url,this)) {
            Intent intent = new Intent(getBaseContext(),SummaryActivity.class);
            intent.putExtra("summaryText",url);
            startActivity(intent);
        }
    }

    String extractUrls(String text){
        String s = text;
        if (text.isEmpty()) return "";
        else if(!text.contains("http") || !text.contains(".")) return "NOT_URL";
        else{
            return text;
        }
    }

    Boolean handleUrlError(String text, Context context){
        if(text == ""){
            Toast.makeText(getApplicationContext(),"URL cannot be empty!",Toast.LENGTH_LONG).show();
            return false;
        }
        else if(text.equals("NOT_URL")){
            Toast.makeText(getApplicationContext(),"Invalid Url",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    void fetchArticles(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApi newsApi = retrofit.create(NewsApi.class);
        Call<Articles> call = newsApi.getArticles();

        call.enqueue(new Callback<Articles>() {
            @Override
            public void onResponse(Call<Articles> call, Response<Articles> response) {
                List<Article> articlesList = response.body().articles;
                ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) findViewById(R.id.shimmer_view_container);
                shimmerFrameLayout.setVisibility(View.GONE);
                listView = (RecyclerView) findViewById(R.id.newsRecyclerView);
                listView.setHasFixedSize(true);
                listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                listView.getLayoutManager().setMeasurementCacheEnabled(false);
                NewsAdapter newsAdapter = new NewsAdapter(getApplicationContext(),articlesList);
                listView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<Articles> call, Throwable t) {

            }
        });
    }

    void firstRun(){
        SharedPreferences preferences = this.getSharedPreferences("com.armaan.summarizer", Context.MODE_PRIVATE);
        if(preferences.getString("first_run","") == null){
            Utils utils = new Utils();
            ArrayList<Summary> summaries = new ArrayList<Summary>();
            preferences
                    .edit()
                    .putString("summaries", utils.serialize(summaries))
                    .apply();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        listView = (RecyclerView) findViewById(R.id.newsRecyclerView);
        firstRun();
        fetchArticles();
        setContentView(R.layout.activity_main);
        urlInput = (EditText) findViewById(R.id.urlInput);
    }


}
