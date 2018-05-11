package com.example.administrator.entity;


import java.io.Serializable;

public class Notice implements Serializable {
    private String title;
    private String content;
    private String date;

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
