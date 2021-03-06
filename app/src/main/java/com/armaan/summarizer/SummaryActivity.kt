package com.armaan.summarizer

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.*

import com.armaan.summarizer.models.Summary
import com.armaan.summarizer.summarizer.SummaryTool
import com.armaan.summarizer.utils.Utils
import com.squareup.picasso.Picasso
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import org.jsoup.Jsoup

import java.util.ArrayList

class SummaryActivity : AppCompatActivity() {

    internal lateinit var summaryText: TextView
    internal lateinit var summaryTitle: TextView
    internal lateinit var summaryImage: ImageView
    internal lateinit var progressBar: ProgressBar
    internal lateinit var fab: FloatingActionButton

    internal lateinit var summary: Summary
    internal lateinit var summaries: ArrayList<Summary>
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
        progressBar = findViewById(R.id.progressBar) as ProgressBar
        fab = findViewById(R.id.fab) as FloatingActionButton
        sharedPreferences = this.getSharedPreferences("com.armaan.summarizer", Context.MODE_PRIVATE)

        val intent = intent
        val action = intent.action
        val type = intent.type
        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent)
            }
        } else {
            generateSummary(intent.getStringExtra("summaryText"),intent.getStringExtra("image"))
        }

        favourite = false
        summaries = utils.deserialize(sharedPreferences.getString("summaries", ""))

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

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun generateSummary(url: String, image: String?){
        doAsync {
            val doc = Jsoup.connect(url).get()
            val summaryTool = SummaryTool()
            summaryTool.init(doc.text())
            summaryTool.extractSentenceFromContext()
            summaryTool.groupSentencesIntoParagraphs()
            summaryTool.createIntersectionMatrix()
            summaryTool.createDictionary()
            summaryTool.createSummary()
            val text = summaryTool.summary.replace("[^\\x00-\\x7f]+".toRegex(), "")
            summary = Summary(doc.title(), text, url, image)

            uiThread {
                if(contains(summary,summaries) == true){
                    favourite = true
                    fab.setImageDrawable(getDrawable(R.drawable.star))
                } else{
                    favourite = false
                    fab.setImageDrawable(getDrawable(R.drawable.star_border))
                }
                fab.visibility = VISIBLE
                summaryText.text = summary.text
                summaryTitle.text = summary.title
                if(summary.imageUrl != null)
                    Picasso.get().load(summary.imageUrl).into(summaryImage);
                else
                    summaryImage.setVisibility(View.GONE)
                progressBar.visibility = GONE
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    internal fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            generateSummary(sharedText,null)
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
