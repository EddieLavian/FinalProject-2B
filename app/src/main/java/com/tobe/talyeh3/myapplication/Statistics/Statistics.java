package com.tobe.talyeh3.myapplication.Statistics;

import java.util.List;

/**
 * Created by talyeh3 on 22/12/2017.
 */

public class Statistics {
    public String key;
    public String teamKey;
    public String name;
    public int games;
    public int goals;
    public int assist;
    public int wins;

    public Statistics()
    {
        //empty constructor becouse firebase needs
    }
    public Statistics(String key, String teamKey, String name, int games, int goals, int assist, int wins)
    {
        this.key = key;
        this.teamKey=teamKey;
        this.name=name;
        this.games=games;
        this.goals=goals;
        this.assist=assist;
        this.wins = wins;
    }

}
