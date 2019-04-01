package com.armaan.summarizer.models;

import java.io.Serializable;

public class Summary implements Serializable {
    String title;
    String text;
    String url;

    public Summary(String title, String text,String url) {
        this.title = title;
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl(){
        return url;
    }
}
