package com.example.talyeh3.myapplication;

import java.util.List;

/**
 * Created by talyeh3 on 19/11/2017.
 */

public class Team {
    public String key;
    public String uid;
    public String name;
    public List<String> users,games;
    public String imgUrl;
    public String howMouchPlayers;


    public Team()
    {
        //empty constructor becouse firebase needs
    }

    public Team(String uid, String name, List<String> users,List<String> games, String key,String imgUrl,String howMouchPlayers) {
        this.key = key;
        this.uid = uid;
        this.name = name;
        this.users = users;
        this.imgUrl=imgUrl;
        this.howMouchPlayers = howMouchPlayers;
        this.games=games;

    }
}
