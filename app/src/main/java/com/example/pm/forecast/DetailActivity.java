package com.example.pm.forecast;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView dateView = (TextView) findViewById(R.id.details_date);
        Forecast forecast = getIntent().getParcelableExtra("forecast");
        dateView.setText(forecast.getDate());

        TextView locationView = (TextView) findViewById(R.id.details_location);
        locationView.setText(PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default)));

        TextView dayHighView = (TextView) findViewById(R.id.details_day_high);
        TextView dayLowView = (TextView) findViewById(R.id.details_day_low);
        TextView dayDescView = (TextView) findViewById(R.id.details_day_description);
        ImageView dayIconView = (ImageView) findViewById(R.id.details_day_icon);

        TextView nightHighView = (TextView) findViewById(R.id.details_night_high);
        TextView nightLowView = (TextView) findViewById(R.id.details_night_low);
        TextView nightDescView = (TextView) findViewById(R.id.details_night_description);
        ImageView nightIconView = (ImageView) findViewById(R.id.details_night_icon);

        dayHighView.setText(getApplicationContext().getString(R.string.format_temperature, forecast.getDayTempMax()));
        dayLowView.setText(getApplicationContext().getString(R.string.format_temperature, forecast.getDayTempMin()));
        dayDescView.setText(forecast.getDayDescription());
        dayIconView.setImageResource(forecast.getDayIconId());

        nightHighView.setText(getApplicationContext().getString(R.string.format_temperature, forecast.getNightTempMax()));
        nightLowView.setText(getApplicationContext().getString(R.string.format_temperature, forecast.getNightTempMin()));
        nightDescView.setText(forecast.getNightDescription());
        nightIconView.setImageResource(forecast.getNightIconId());
    }
}
