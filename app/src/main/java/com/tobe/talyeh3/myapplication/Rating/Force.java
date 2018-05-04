package com.tobe.talyeh3.myapplication.Rating;

import java.util.List;

/**
 * Created by talyeh3 on 19/02/2018.
 */

public class Force {
    public String userKey;
    public String teamKey;
    public String userName;
    public String photo;
    public int avgRating;

    public Force()
    {
        //empty constructor becouse firebase needs
    }

    public Force(String userKey, String teamKey, String userName,String photo,int avgRating)
    {
        this.userKey = userKey;
        this.teamKey=teamKey;
        this.userName=userName;
        this.photo=photo;
        this.avgRating=avgRating;
    }

}


