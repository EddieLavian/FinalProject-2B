package com.tobe.talyeh3.myapplication;

import android.widget.ImageView;

import java.util.List;

/**
 * Object of User
 */

public class User
{
    public String key; // need to delete
    public String imgUrl; // image
    public String uid; // the key of the user
    public String userName;
    public String email; // need to check
    public int age;
    public String city;
    public String device_token;
    public double rating; // need to delete
    public String managerSite;
    public List<String> teams;

    public User()
    {
        //empty constructor because of firebase needs
    }

    public User(String uid, String userName, String email, int age, String key,List<String> teams,String imgUrl, String city, double rating, String managerSite, String device_token){
        this.key = key;
        this.device_token = device_token;
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
