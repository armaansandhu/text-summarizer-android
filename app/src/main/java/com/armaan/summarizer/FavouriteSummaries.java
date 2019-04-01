package com.armaan.summarizer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.armaan.summarizer.models.Summary;
import com.armaan.summarizer.ui.SummaryAdapter;

import java.io.IOException;
import java.util.ArrayList;

public class FavouriteSummaries extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_summaries);
        SharedPreferences sharedPreferences = this.getSharedPreferences("com.armaan.summarizer", MODE_PRIVATE);

        final RecyclerView listView = (RecyclerView) findViewById(R.id.list);
        listView.setHasFixedSize(true);
        listView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Summary> summaries = new ArrayList<Summary>();
        try {
            summaries = (ArrayList<Summary>) ObjectSerializer.deserialize(sharedPreferences.getString("summaries", "kk"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        final SummaryAdapter adapter = new SummaryAdapter(this, summaries);
        listView.setAdapter(adapter);

        final ArrayList<Summary> finalSummaries = summaries;

    }


}
