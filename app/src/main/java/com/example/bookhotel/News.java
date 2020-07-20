package com.example.bookhotel;

import java.io.Serializable;

public class News implements Serializable {
    private String title;
    private String description;
    private String img;
    private String date;

    public News(){}

    public News(String title, String description, String img, String date) {
        this.title = title;
        this.description = description;
        this.img = img;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }
    public String getDescription() {
        return description;
    }
    public String getImg() {
        return img;
    }
    public String getDate() {
        return date;
    }
}
