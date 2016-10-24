package com.example.pm.forecast;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class ForecastLoader extends AsyncTaskLoader<List<Forecast>> {

    // A tag for log messages
    private static final String LOG_TAG = ForecastLoader.class.getName();

    // Application context
    private Context context;

    // String for holding the user's preferred location
    private String location;

    // Query url
    private String url;

    public ForecastLoader(Context context, String url, String location) {
        super(context);
        this.context = context;
        this.url = url;
        this.location = location;
    }

    // load the list of forecasts immediately on loader start
    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Forecast> loadInBackground() {

        if (url == null) {
            return null;
        }

        // Perform the network request, parse the response and extract a list of forecasts
        List<Forecast> forecasts = new XmlQueryUtils(location).fetchForecastData(url);
        return forecasts;
    }
}

