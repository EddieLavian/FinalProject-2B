package com.example.talyeh3.myapplication;

/**
 * Created by talyeh3 on 16/12/2017.
 */

public class Game {
    public String date;
    public String time;
    public String location;
    public int minimumPlayers;
    public String key;
    public String keyTeam;
    public String userOpen;

    public Game()
    {
        //empty constructor becouse firebase needs
    }

    public Game(String date, String time, String location, int minimumPlayers, String key, String keyTeam, String userOpen){
        this.date=date;
        this.time=time;
        this.location=location;
        this.minimumPlayers=minimumPlayers;
        this.key=key;
        this.keyTeam= keyTeam;
        this.userOpen=userOpen;
    }
}
