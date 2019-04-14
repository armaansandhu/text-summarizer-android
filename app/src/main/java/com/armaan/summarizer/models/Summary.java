package com.armaan.summarizer.models;

import java.io.Serializable;

public class Summary implements Serializable {
    String title;
    String text;
    String url;
    String imageUrl;
    public Summary(String title, String text,String url,String imageUrl) {
        this.title = title;
        this.text = text;
        this.url = url;
        this.imageUrl = imageUrl;
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

    public String getImageUrl() {
        return imageUrl;
    }
}
