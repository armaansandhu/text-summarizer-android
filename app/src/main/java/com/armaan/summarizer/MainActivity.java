package com.armaan.summarizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ProgressBar;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import java.io.IOException;
import java.util.List;

import com.armaan.summarizer.models.Article;
import com.armaan.summarizer.models.Articles;
import com.armaan.summarizer.models.Summary;
import com.armaan.summarizer.retrofit.NewsApi;
import com.armaan.summarizer.summarizer.SummaryTool;
import com.armaan.summarizer.ui.NewsAdapter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {


    void linkPage(View view){
        Intent intent = new Intent(this,ThroughLink.class);
        startActivity(intent);
    }

    void textPage(View view){
        Intent intent = new Intent(this,ThroughText.class);
        startActivity(intent);
    }

    public void favourites(View view) {
        Intent intent = new Intent(this,FavouriteSummaries.class);
        startActivity(intent);
    }

    EditText urlInput;
    ProgressBar loadingSummary;
    RecyclerView listView;

    void linkSummarize(View view){
        loadingSummary.setVisibility(View.VISIBLE);
        String url = formUrl(urlInput.getText().toString());
        new UrlParser().execute(url);
    }

    String formUrl(String oldUrl){
        if(oldUrl.contains("http://") || oldUrl.contains("https://")){
            return oldUrl;
        }
        else{
            oldUrl = "http://" + oldUrl;
            return oldUrl;
        }
    }

    public class UrlParser extends AsyncTask<String,Void,Summary>{
        String text;
        String title;
        String url;
        @Override
        protected Summary doInBackground(String... strings) {
            try {
                Document doc = Jsoup.connect(strings[0]).get();
                SummaryTool summaryTool = new SummaryTool();
                summaryTool.init(doc.text());
                summaryTool.extractSentenceFromContext();
                summaryTool.groupSentencesIntoParagraphs();
                summaryTool.createIntersectionMatrix();
                summaryTool.createDictionary();
                summaryTool.createSummary();
                text = summaryTool.getSummary();
                title = doc.title();
                url = strings[0];
                return new Summary(title,text,url);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Summary s) {
            super.onPostExecute(s);
            loadingSummary.setVisibility(View.GONE);
            Intent intent = new Intent(getBaseContext(),SummaryActivity.class);
            try {
                String st = ObjectSerializer.serialize(s);
                System.out.print(s.getText());
                intent.putExtra("summaryText",st);
            } catch (IOException e) {
                e.printStackTrace();
            }
            startActivity(intent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = (RecyclerView) findViewById(R.id.newsRecyclerView);
        fetchArticles();
        setContentView(R.layout.activity_main);
        urlInput = (EditText) findViewById(R.id.urlInput);
        loadingSummary = (ProgressBar) findViewById(R.id.loadingSummary);
        loadingSummary.setVisibility(View.GONE);

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
                listView = (RecyclerView) findViewById(R.id.newsRecyclerView);
                listView.setHasFixedSize(true);
                listView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                NewsAdapter newsAdapter = new NewsAdapter(getApplicationContext(),articlesList);
                listView.setAdapter(newsAdapter);
            }

            @Override
            public void onFailure(Call<Articles> call, Throwable t) {

            }
        });
    }

}
