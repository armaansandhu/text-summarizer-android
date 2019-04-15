package com.armaan.summarizer

import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.widget.ImageView
import android.widget.TextView

import com.armaan.summarizer.models.Summary
import com.squareup.picasso.Picasso

import java.io.IOException

class SavedSummary : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saved_summary)
        val intent = intent

        val title = findViewById<View>(R.id.savedSummaryTitle) as TextView
        val summary = findViewById<View>(R.id.savedSummaryText) as TextView
        val image = findViewById(R.id.summaryImage) as ImageView

        val summaryObject = intent.getSerializableExtra("savedSummary") as Summary
        title.text = summaryObject.title
        summary.text = summaryObject.text
        if(summaryObject.imageUrl != null)
            Picasso.get().load(summaryObject.imageUrl).into(image)
        else
            image.setVisibility(GONE)
    }

}

