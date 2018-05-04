package com.tobe.talyeh3.myapplication;

import java.util.List;

/**
 * Created by talyeh3 on 16/12/2017.
 */

public class Game {
    public String date;
    public String time;
    public String location;
    public int minimumPlayers;
    public int attending;
    public String key;
    public String keyTeam;
    public String userOpen;
    public List<String> whoIsComming;

    public Game()
    {
        //empty constructor becouse firebase needs
    }

    public Game(String date, String time, String location, int minimumPlayers, String key, String keyTeam, String userOpen,int attending,List<String> whoIsComming){
        this.attending=attending;
        this.date=date;
        this.time=time;
        this.location=location;
        this.minimumPlayers=minimumPlayers;
        this.key=key;
        this.keyTeam= keyTeam;
        this.userOpen=userOpen;
        this.whoIsComming=whoIsComming;
    }
}
