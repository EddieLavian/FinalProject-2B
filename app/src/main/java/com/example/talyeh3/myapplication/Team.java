package com.example.talyeh3.myapplication;

import java.util.List;

/**
 * Created by talyeh3 on 19/11/2017.
 */

public class Team {
    public String key;
    public String uid;
    public String name;
    public List<String> users;



    public Team()
    {
        //empty constructor becouse firebase needs
    }

    public Team(String uid, String name, List<String> users, String key) {
        this.key = key;
        this.uid = uid;
        this.name = name;
        this.users = users;

    }
}
