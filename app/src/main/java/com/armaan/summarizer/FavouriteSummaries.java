package com.armaan.summarizer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.armaan.summarizer.models.Summary;
import com.armaan.summarizer.ui.SummaryAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;

public class FavouriteSummaries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_summaries);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.armaan.summarizer", MODE_PRIVATE);
        Gson gson = new Gson();
        final RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Summary> summaries = new ArrayList<Summary>();
        summaries = (ArrayList<Summary>) gson.fromJson(sharedPreferences.getString("summaries", "kk"),new TypeToken<ArrayList<Summary>>() {}.getType());

        final SummaryAdapter adapter = new SummaryAdapter(this, summaries);
        listView.setAdapter(adapter);
        final ArrayList<Summary> finalSummaries = summaries;

    }


}
