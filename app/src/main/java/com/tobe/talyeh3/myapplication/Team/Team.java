package com.tobe.talyeh3.myapplication.Team;

import java.util.List;

/**
 * Created by talyeh3 on 19/11/2017.
 */

public class Team
{
    public String key;
    public String uid;
    public String name;
    public List<String> users,games,statistics,rating, permissions;
    public String imgUrl;
    public String howMouchPlayers;
    public String manager;



    public Team()
    {
        //empty constructor becouse firebase needs
    }

    public Team(String uid, String name, List<String> users,List<String> games, String key,String imgUrl,String howMouchPlayers,List<String> statistics,String manager,List<String> rating, List<String> permissions) {
        this.key = key;
        this.uid = uid;
        this.name = name;
        this.users = users;
        this.imgUrl=imgUrl;
        this.howMouchPlayers = howMouchPlayers;
        this.games=games;
        this.statistics=statistics;
        this.manager=manager;
        this.rating=rating;
        this.permissions = permissions;
    }
}
