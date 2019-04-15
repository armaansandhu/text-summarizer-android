package com.armaan.summarizer.utils

import com.armaan.summarizer.models.Summary
import com.armaan.summarizer.summarizer.SummaryTool
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

import java.io.IOException
import java.util.ArrayList

class Utils {
    internal var gson = Gson()

    fun serialize(obj: ArrayList<Summary>): String {
        return gson.toJson(obj)
    }

    fun deserialize(obj: String): ArrayList<Summary>? {
        return gson.fromJson<Any>(obj, object : TypeToken<ArrayList<Summary>>() {

        }.type) as ArrayList<Summary>
    }
}
