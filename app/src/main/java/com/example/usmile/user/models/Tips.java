package com.example.usmile.user.models;

import java.util.Date;

public class Tips {
    private int ID;
    private String title;
    private String URL;
    private String type;
    private boolean isDeleted;



    public Tips(int ID, String title, String URL, String type) {
        this.ID = ID;
        this.title = title;
        this.URL = URL;
        this.type = type;
        this.isDeleted = false;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getURL() {
        return URL;
    }

    // https://github.com/NamPham1906/USmile/tree/nam -> github.com
    public String getSourceWebsWebsite() {
        String cut = URL.substring(URL.indexOf("://") + 3);
        String source = cut.substring(0, cut.indexOf('/'));
        return source;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
