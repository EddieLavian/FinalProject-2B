package com.example.talyeh3.myapplication;

/**
 * Created by talyeh3 on 16/11/2017.
 */

public class Post {
    public String key;
    public String uid;
    public String title;
    public String body;
    public int likes=0;


    public Post()
    {
        //empty constructor becouse firebase needs
    }

    public Post(String uid, String title, String body, int likes, String key) {
        this.key = key;
        this.uid = uid;
        this.title = title;
        this.body = body;
        this.likes = likes;
    }
}
