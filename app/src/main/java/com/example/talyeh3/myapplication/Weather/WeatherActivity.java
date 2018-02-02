package com.example.talyeh3.myapplication.Weather;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talyeh3.myapplication.R;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

public class WeatherActivity extends AppCompatActivity {

    // Project Created by Ferdousur Rahman Shajib
    // www.androstock.com


    TextView cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField,btnPlace;
    private final int REQUEST_CODE_PLACEPICKER = 1;
    Typeface weatherFont;
    double latitude;
    double longitude;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_weather);


        weatherFont = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/weathericons-regular-webfont.ttf");

        cityField = (TextView)findViewById(R.id.city_field);
        updatedField = (TextView)findViewById(R.id.updated_field);
        btnPlace = (TextView)findViewById(R.id.btnPlace);
        detailsField = (TextView)findViewById(R.id.details_field);
        currentTemperatureField = (TextView)findViewById(R.id.current_temperature_field);
        humidity_field = (TextView)findViewById(R.id.humidity_field);
        pressure_field = (TextView)findViewById(R.id.pressure_field);
        weatherIcon = (TextView)findViewById(R.id.weather_icon);
        weatherIcon.setTypeface(weatherFont);

        btnPlace = (TextView) findViewById(R.id.btnPlace);
        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlacePickerActivity();
            }
        });

    }



    private void startPlacePickerActivity() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        // this would only work if you have your Google Places API working

        Intent intent;
        try {
            intent = intentBuilder.build( this);
            startActivityForResult(intent, REQUEST_CODE_PLACEPICKER);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displaySelectedPlaceFromPlacePicker(Intent data) {
        //  Place placeSelected = PlacePicker.getPlace(data, this);
        Place placeSelected = PlacePicker.getPlace(this, data);
        String name = placeSelected.getName().toString();
        String address = placeSelected.getAddress().toString();
        latitude = placeSelected.getLatLng().latitude;
        longitude = placeSelected.getLatLng().longitude;
        TextView enterCurrentLocation = (TextView) findViewById(R.id.btnPlace);
        enterCurrentLocation.setText(name + ", " + address);
    }


    protected  void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PLACEPICKER && resultCode == RESULT_OK) {
            displaySelectedPlaceFromPlacePicker(data);

            WheaterApp.placeIdTask asyncTask =new WheaterApp.placeIdTask(new WheaterApp.AsyncResponse() {
                public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {

                  //  cityField.setText(weather_city);
                    updatedField.setText(weather_updatedOn);
                    detailsField.setText(weather_description);
                    currentTemperatureField.setText(weather_temperature);
                    humidity_field.setText("Humidity: "+weather_humidity);
                    pressure_field.setText("Pressure: "+weather_pressure);
                    weatherIcon.setText(Html.fromHtml(weather_iconText));

                }
            });
            asyncTask.execute(String.valueOf(latitude), String.valueOf(longitude)); //  asyncTask.execute("Latitude", "Longitude")
        }
    }



}