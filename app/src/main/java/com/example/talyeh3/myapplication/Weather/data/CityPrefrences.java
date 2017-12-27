package com.example.talyeh3.myapplication.Weather.data;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by Eddie on 24/12/2017.
 */

public class CityPrefrences
{
    SharedPreferences prefs;

    public CityPrefrences(Activity activity)
    {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity()
    {
        return prefs.getString("city", "Jerusalem,IL");
    }

    public void setCity(String city)
    {
        prefs.edit().putString("city", city).commit();
    }
}
