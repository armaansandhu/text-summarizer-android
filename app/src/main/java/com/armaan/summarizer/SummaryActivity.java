package com.armaan.summarizer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.armaan.summarizer.models.Summary;

import java.io.IOException;
import java.util.ArrayList;

public class SummaryActivity extends AppCompatActivity {

    TextView summaryText;
    TextView summaryTitle;
    Summary summary;
    SharedPreferences sharedPreferences;
    ArrayList<Summary> summaries;
    boolean favourite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        //Declaration//
        final Button button = (Button) findViewById(R.id.button);
        favourite = false;
        summaryText = (TextView) findViewById(R.id.summaryText);
        summaryTitle = (TextView) findViewById(R.id.summaryTitle);
        sharedPreferences = this.getSharedPreferences("com.armaan.summarizer",MODE_PRIVATE);
        Intent intent = getIntent();

        try {
            summary = (Summary) ObjectSerializer.deserialize(intent.getStringExtra("summaryText"));
            this.summaryText.setText(summary.getText());
            this.summaryTitle.setText(summary.getTitle());
            summaries = new ArrayList<Summary>();
            summaries = (ArrayList<Summary>) ObjectSerializer.deserialize(sharedPreferences.getString("summaries","kk"));

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if(!favourite){
                            //summaries = new ArrayList<Summary>();
                            summaries.add(summary);
                            sharedPreferences.edit().putString("summaries",ObjectSerializer.serialize(summaries)).apply();
                            Toast.makeText(getApplicationContext(),"SummaryActivity added!",Toast.LENGTH_LONG).show();
                            button.setText("Remove");
                            favourite = true;
                        } else {
                            summaries.remove(summary);
                            sharedPreferences.edit().putString("summaries",ObjectSerializer.serialize(summaries)).apply();
                            Toast.makeText(getApplicationContext(),"SummaryActivity removed!",Toast.LENGTH_LONG).show();
                            button.setText("Add to Favourites");
                            favourite = false;
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

}
