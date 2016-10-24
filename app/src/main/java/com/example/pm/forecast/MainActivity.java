package com.example.pm.forecast;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Forecast>>,
        SharedPreferences.OnSharedPreferenceChangeListener {

    // A custom adapter for displaying forecasts in the list view
    private static ForecastAdapter forecastAdapter;

    // A list for storing all the retrieved forecast objects
    private static List<Forecast> forecasts;

    public static Resources resources;

    // Url for making the xml query
    public static final String URL = "http://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php";

    // A constant value for the loader ID
    public static final int FORECAST_LOADER_ID = 1;

    // A string for storing the location set in user preferences
    private String location;

    // A variable for holding shared preferences
    SharedPreferences preferences;

    // An empty text view for error messages
    TextView emptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Retrieve the location from preferences
        location = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        // Prepare the loader
        final LoaderManager loaderManager = getLoaderManager();

        resources = getResources();
        forecasts = new ArrayList<>();

        // Textview displayed when the forecasts list is epty
        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        ListView listView = (ListView) findViewById(R.id.listview_forecast);
        listView.setEmptyView(emptyStateTextView);

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Initialize the loader
            loaderManager.initLoader(FORECAST_LOADER_ID, null, this);

        } else {

            // Hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        // Create a custom adapter
        forecastAdapter = new ForecastAdapter(this, forecasts);
        listView.setAdapter(forecastAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                intent.putExtra("forecast", forecasts.get(position));
                startActivity(intent);
            }
        });

        // Register preferences change listener
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        preferences.registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Forecast>> onCreateLoader(int id, Bundle args) {

        // Create a new loader for the given URL
        return new ForecastLoader(this, URL, location);
    }

    @Override
    public void onLoadFinished(Loader<List<Forecast>> loader, List<Forecast> forecasts) {

        // Clear the adapter of previous data
        forecastAdapter.clear();

        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Add the data to adapter's data set
        if (forecasts != null && !forecasts.isEmpty()) {

            forecastAdapter.clear();
            forecastAdapter.addAll(forecasts);
        } else {
            // Display error message when there is no data
            emptyStateTextView.setText(R.string.no_data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onLoaderReset(Loader loader) {

        // Clear the existing data
        forecastAdapter.clear();

        // Retrieve the location from preferences
        location = PreferenceManager.getDefaultSharedPreferences(this)
                .getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        // Restart the loader for a new query when user changes the location
        if (key.equals("location")) {
            getLoaderManager().destroyLoader(FORECAST_LOADER_ID);
            getLoaderManager().initLoader(FORECAST_LOADER_ID, null, this);
        }
    }
}
