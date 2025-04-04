package com.memorygame.model;

public class Image {
    private String id;
    private String url;
    private String title;
    private String category;

    public Image() {
    }

    public Image(String id, String url, String title, String category) {
        this.id = id;
        this.url = url;
        this.title = title;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}

