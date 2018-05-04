package com.tobe.talyeh3.myapplication.Posts;

import java.util.Date;

/**
 * Created by talyeh3 on 16/11/2017.
 */

public class Post {
    public String key;
    public String uid;
    public String title;
    public String body;
    public int likes=0;
    public String imgUrl;
    public String subtitle;
    public String date;

    public Post()
    {
        //empty constructor becouse firebase needs
    }

    public Post(String uid, String title, String body, int likes, String key ,String imgUrl,String subtitle, String date) {
        this.subtitle=subtitle;
        this.key = key;
        this.imgUrl = imgUrl;
        this.uid = uid;
        this.title = title;
        this.body = body;
        this.likes = likes;
        this.date = date;
    }
}
