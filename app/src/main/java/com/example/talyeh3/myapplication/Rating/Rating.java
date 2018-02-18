package com.example.talyeh3.myapplication.Rating;

import java.util.List;

/**
 * Created by talyeh3 on 17/02/2018.
 */

public class Rating {
    public String key;
    public String teamKey;
    public String name;
    public int avgRating;
    public List<String> whoIsRating;
    public List<Integer> rating;



    public Rating()
    {
        //empty constructor becouse firebase needs
    }

    public Rating(String key, String teamKey, String name,int avgRating, List<Integer> rating, List<String> whoIsRating)
    {
        this.key = key;
        this.teamKey=teamKey;
        this.name=name;
        this.avgRating=avgRating;
        this.rating=rating;
        this.whoIsRating=whoIsRating;
    }
}
