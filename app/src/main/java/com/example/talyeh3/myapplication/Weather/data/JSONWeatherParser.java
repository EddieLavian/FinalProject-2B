package com.example.talyeh3.myapplication.Weather.data;

import com.example.talyeh3.myapplication.Weather.Util.Utils;
import com.example.talyeh3.myapplication.Weather.model.Place;
import com.example.talyeh3.myapplication.Weather.model.Weather;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Parse the data that we get
 */

public class JSONWeatherParser
{
    public static Weather getWeather(String data)
    {
        Weather weather = new Weather();

        try{
            JSONObject jsonObject = new JSONObject(data);

            Place place = new Place();

            // Get the coord object //
            JSONObject coordObj = Utils.getObject("coord", jsonObject); // update our cordinate from the json object
            place.setLatitude(Utils.getFloat("lat", coordObj));
            place.setLongitude(Utils.getFloat("lon", coordObj));

            // Get the sys object //
            JSONObject sysObj = Utils.getObject("sys", jsonObject); // update our cordinate from the json object
            place.setCountry(Utils.getString("country", sysObj));
            place.setLastUpdate(Utils.getInt("dt", jsonObject)); // dt is the last update and it's in the parent json object
            place.setSunrise(Utils.getInt("sunrise", sysObj));
            place.setSunset(Utils.getInt("sunset", sysObj));
            place.setCity(Utils.getString("name", jsonObject));
            weather.place = place;

            // Get the weather info //
            JSONArray jsonArray = jsonObject.getJSONArray("weather"); // tag weather is json object in an array
            JSONObject jsonWeather = jsonArray.getJSONObject(0); // get the object as is.
            weather.currentCondition.setWeatherId(Utils.getInt("id", jsonWeather));
            weather.currentCondition.setDescription(Utils.getString("description", jsonWeather));
            weather.currentCondition.setCondition(Utils.getString("main", jsonWeather));
            weather.currentCondition.setIcon(Utils.getString("icon", jsonWeather));
            //weather.currentCondition.setDate(Utils.getString("dt_txt", jsonWeather));

            JSONObject mainObj = Utils.getObject("main", jsonObject);
            weather.currentCondition.setHumidity(Utils.getInt("humidity", mainObj));
            weather.currentCondition.setPressure(Utils.getInt("pressure", mainObj));
            weather.currentCondition.setMinTemp(Utils.getFloat("temp_min", mainObj));
            weather.currentCondition.setMaxTemp(Utils.getFloat("temp_max", mainObj));
            weather.currentCondition.setTemperature(Utils.getDouble("temp", mainObj));

            // Get the wind object //
            JSONObject windObj = Utils.getObject("wind", jsonObject); //
            weather.wind.setSpeed(Utils.getFloat("speed", windObj));
            weather.wind.setDegree(Utils.getFloat("deg", windObj));

            // Get the clouds object //
            JSONObject cloudsObj = Utils.getObject("clouds", jsonObject);
            weather.clouds.setPrecipitation(Utils.getInt("all", cloudsObj));

            return weather;

        }
        catch (JSONException e) {
            e.printStackTrace();

            return null;
        }
    }

}
