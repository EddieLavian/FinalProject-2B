package com.example.talyeh3.myapplication.Weather.data;

import com.example.talyeh3.myapplication.Weather.Util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
    get data from url and return as String
 */

public class WeatherHttpClient
{
    // Create Connection between the app and the web api //
    public String getWeatherData(String place)
    {
        HttpURLConnection connection = null;
        InputStream inputStream = null; // input from web

        try {
            connection = (HttpURLConnection)(new URL(Utils.BASE_URL + place)).openConnection(); // open connection with url = BASE_URL
            connection.setRequestMethod("GET"); // get data from the web
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();

            // Read the response from url
            StringBuffer stringBuffer = new StringBuffer(); // Buffer for data from internet
            inputStream = connection.getInputStream(); // get data from web - from the api

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream)); // the data is in bufferdReader
            String line = null;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuffer.append(line + "\r\n");
            }

            inputStream.close(); // close the stream
            connection.disconnect();

            return stringBuffer.toString();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
