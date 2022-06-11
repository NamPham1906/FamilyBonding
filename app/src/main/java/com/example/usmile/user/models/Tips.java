package com.example.usmile.user.models;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class Tips implements Serializable {

    private String title;
    private String url;
    private String type;
    private String short_content;
    private boolean deleted;
    private String source;
    private int resource;



    public Tips(String title, String URL, String type, String shortContent) {
        this.title = title;
        this.url = URL;
        this.type = type;
        this.short_content = shortContent;
        this.deleted = false;
    }

    public Tips(int res, String title, String URL, String type, String shortContent) {
        this.resource = res;
        this.title = title;
        this.url = URL;
        this.type = type;
        this.short_content = shortContent;
        this.deleted = false;
    }

    public int getResource() {
        return resource;
    }

    public void setResource(int resource) {
        this.resource = resource;
    }

    public Tips() {

    }

    @PropertyName("source")
    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @PropertyName("short_content")
    public String getShort_content() {
        return short_content;
    }

    public void setShort_content(String short_content) {
        this.short_content = short_content;
    }


    @PropertyName("title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @PropertyName("url")
    public String getUrl() {
        return url;
    }

    // https://github.com/NamPham1906/USmile/tree/nam -> github.com
    public String getSourceWebsWebsite() {
        String cut = url.substring(url.indexOf("://") + 3);
        String source = cut.substring(0, cut.indexOf('/'));
        return source;
    }

    public String findSource(String url) {
        String cut = url.substring(url.indexOf("://") + 3);
        String source = cut.substring(0, cut.indexOf('/'));
        return source;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @PropertyName("type")
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @PropertyName("deleted")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
