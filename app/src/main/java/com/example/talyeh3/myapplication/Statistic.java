package com.example.talyeh3.myapplication;

import java.util.List;

/**
 * Created by talyeh3 on 22/12/2017.
 */

public class Statistic {
    public String key;
    public String team;
    public String name;
    public String games;
    public String goals;
    public String assist;

    public Statistic()
    {
        //empty constructor becouse firebase needs
    }
    public Statistic(String key, String team, String name,String games,String goals,String assist) {
        this.key = key;
        this.team=team;
        this.name=name;
        this.games=games;
        this.goals=goals;
        this.assist=assist;
    }

}
