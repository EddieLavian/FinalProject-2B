package com.example.talyeh3.myapplication;

import android.widget.ImageView;

import java.util.List;

/**
 * Created by talyeh3 on 16/11/2017.
 */

public class User
{
    public String key,imgUrl;
    public String uid;
    public String userName;
    public String email;
    public int age;
    public String city;
    public double rating;
    public String managerSite;
    public List<String> teams;

    public User()
    {
        //empty constructor becouse firebase needs
    }

    public User(String uid, String userName, String email, int age, String key,List<String> teams,String imgUrl, String city, double rating, String managerSite){
        this.key = key;
        this.managerSite=managerSite;
        this.imgUrl=imgUrl;
        this.teams=teams;
        this.uid = uid;
        this.userName = userName;
        this.email = email;
        this.age = age;
        this.city = city;
        this.rating = rating;
    }
}
