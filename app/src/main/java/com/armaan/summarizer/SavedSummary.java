package com.armaan.summarizer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.armaan.summarizer.models.Summary;

import java.io.IOException;

public class SavedSummary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_summary);
        Intent intent = getIntent();

        TextView title = (TextView) findViewById(R.id.savedSummaryTitle);
        TextView summary = (TextView) findViewById(R.id.savedSummaryText);

        try {
            Summary summaryObject = (Summary) ObjectSerializer.deserialize(intent.getStringExtra("savedSummary"));
            title.setText(summaryObject.getTitle());
            summary.setText(summaryObject.getText());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
