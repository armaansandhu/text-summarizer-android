package com.armaan.summarizer

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import com.armaan.summarizer.models.Summary
import com.armaan.summarizer.utils.Utils
import com.google.gson.Gson
import com.squareup.picasso.Picasso

import java.io.IOException
import java.util.ArrayList

class SummaryActivity : AppCompatActivity() {

    internal lateinit var summaryText: TextView
    internal lateinit var summaryTitle: TextView
    internal lateinit var summaryImage: ImageView

    internal lateinit var summary: Summary
    internal lateinit var sharedPreferences: SharedPreferences
    internal var favourite: Boolean = false
    internal lateinit var utils: Utils

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        utils = Utils()
        summaryText = findViewById<View>(R.id.summaryText) as TextView
        summaryTitle = findViewById<View>(R.id.summaryTitle) as TextView
        summaryImage = findViewById<View>(R.id.summaryImage) as ImageView
        val fab = findViewById(R.id.fab) as FloatingActionButton
        sharedPreferences = this.getSharedPreferences("com.armaan.summarizer", Context.MODE_PRIVATE)
        val intent = intent

        summary = intent.getSerializableExtra("summaryText") as Summary
        this.summaryText.text = summary.text
        this.summaryTitle.text = summary.title
        if(summary.imageUrl != null)
            Picasso.get().load(summary.imageUrl).into(summaryImage);
        else
            summaryImage.setVisibility(View.GONE)
        val summaries : ArrayList<Summary> = utils.deserialize(sharedPreferences.getString("summaries", ""))
        if(contains(summary,summaries) == true){
            favourite = true
            fab.setImageDrawable(getDrawable(R.drawable.star))
        } else{
            favourite = false
            fab.setImageDrawable(getDrawable(R.drawable.star_border))
        }
        fab.setOnClickListener {
            if (!favourite ) {

                summaries!!.add(summary)
                sharedPreferences
                        .edit()
                        .putString("summaries", utils.serialize(summaries))
                        .apply()
                Toast.makeText(applicationContext, "Summary added!", Toast.LENGTH_LONG).show()
                fab.setImageDrawable(getDrawable(R.drawable.star))
                favourite = true
            } else {
                summaries.removeAt(remove(summary,summaries))
                sharedPreferences
                        .edit()
                        .putString("summaries", utils.serialize(summaries)).apply()
                Toast.makeText(applicationContext, "Summary removed!", Toast.LENGTH_LONG).show()
                fab.setImageDrawable(getDrawable(R.drawable.star_border))
                favourite = false
            }
        }
    }

    fun contains(summary: Summary,list: ArrayList<Summary>): Boolean {
        for (i in list){
            if (summary.title == i.title) return true
        }
        return false
    }

    fun remove(summary: Summary,list: ArrayList<Summary>): Int {
        for (i in 0..list.size){
            if (summary.title == list[i].title) return i
        }
        return -1
    }
}
