package com.example.talyeh3.myapplication.Weather;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.talyeh3.myapplication.R;
import com.example.talyeh3.myapplication.Weather.Util.Utils;
import com.example.talyeh3.myapplication.Weather.data.CityPrefrences;
import com.example.talyeh3.myapplication.Weather.data.JSONWeatherParser;
import com.example.talyeh3.myapplication.Weather.data.WeatherHttpClient;
import com.example.talyeh3.myapplication.Weather.model.CurrentCondition;
import com.example.talyeh3.myapplication.Weather.model.Weather;
import com.google.android.gms.ads.internal.gmsg.HttpClient;

import java.text.DateFormat;
import java.text.DecimalFormat;


/**
 * Created by Eddie on 24/12/2017.
 */

public class WeatherMainActivity extends AppCompatActivity implements View.OnClickListener
{
    private TextView cityName, temp, description, humidity, pressure, wind, sunrise, sunset, updated;
    private ImageView iconView;
    private Button btnChangeCity;

    Weather weather = new Weather();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_main_activity);

        cityName = (TextView)findViewById(R.id.cityText);
        temp = (TextView)findViewById(R.id.tempText);
        description = (TextView)findViewById(R.id.cloudText);
        humidity = (TextView)findViewById(R.id.humidText);
        pressure = (TextView)findViewById(R.id.pressureText);
        wind = (TextView)findViewById(R.id.windText);
        iconView = (ImageView)findViewById(R.id.thumbnailIcon);
        btnChangeCity = (Button)findViewById(R.id.change_cityId);

        btnChangeCity.setOnClickListener(this);

        CityPrefrences cityPrefrences = new CityPrefrences(WeatherMainActivity.this);


        renderWeatherData(cityPrefrences.getCity());
    }

    public void renderWeatherData(String city)
    {
        WeatherTask weatherTask = new WeatherTask();
        weatherTask.execute(new String[]{city + "&APPID="+ "a78e8b7d14f69cdf7ca3922fcf1bca25" + "&units= metric"});

    }

    @Override
    public void onClick(View v) {
        WeatherTask w = new WeatherTask();
        w.showInputDialog();

    }


    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap>
    {
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            iconView.setImageBitmap(bitmap);
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            return downloadImage(strings[0]);
        }

        private Bitmap downloadImage(String code)
        {
            return null;
        }
    }


    // current weather
    private class WeatherTask extends AsyncTask<String, Void, Weather> // Params, Progress, Result
    {
        @Override
        protected Weather doInBackground(String... strings)
        {
            String data = ((new WeatherHttpClient()).getWeatherData(strings[0])); // pass string that we get in WeatherHttpClient

            weather.iconData = weather.currentCondition.getIcon();

            weather = JSONWeatherParser.getWeather(data); // parse the data that we get

            Log.v("Data: ", weather.place.getCity());

            new DownloadImageAsyncTask().execute(weather.iconData);
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather)
        {
            super.onPostExecute(weather);

            DateFormat df = DateFormat.getDateInstance();

            DecimalFormat decimalFormat = new DecimalFormat("#.#"); // round to decimal point

            String tempFormat = decimalFormat.format(weather.currentCondition.getTemperature() - 274.4);

            cityName.setText(weather.place.getCity() + ", " + weather.place.getCountry());
            temp.setText("" + tempFormat + "°C");
            humidity.setText("Humidity: " + weather.currentCondition.getHumidity() + "%");
            pressure.setText("Pressure: " + weather.currentCondition.getPressure() + "hPa");
            wind.setText("Wind: " + weather.wind.getSpeed()+ "mps");
            description.setText("Condition: " + weather.currentCondition.getCondition() + "(" + weather.currentCondition.getDescription() + ")");
        }

        private void showInputDialog()
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(WeatherMainActivity.this);
            builder.setTitle("Change City");

            final View sView = getLayoutInflater().inflate(R.layout.weather_dialog_spinner,null);

            final Spinner cityInput = (Spinner)sView.findViewById(R.id.spinner2);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(WeatherMainActivity.this,
                    android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.city));
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            cityInput.setAdapter(adapter);

            builder.setPositiveButton("Set", new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    CityPrefrences cityPrefrences = new CityPrefrences(WeatherMainActivity.this);
                    cityPrefrences.setCity(cityInput.getSelectedItem().toString());

                    String newCity = cityPrefrences.getCity();

                    renderWeatherData(newCity);
                }
            });
            builder.setView(sView);
            builder.show();
        }
    }
}
