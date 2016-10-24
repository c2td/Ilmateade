package com.example.pm.forecast;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/* This class deals with connecting to the network and parsing XML data */

public class XmlQueryUtils {

    /** Tag for log messages */
    public static final String LOG_TAG = XmlQueryUtils.class.getName();

    // describes all the XML tags that need to be parsed
    public static final String FORECAST_TAG = "forecast";
    public static final String NIGHT_TAG = "night";
    public static final String DAY_TAG = "day";
    public static final String TEMPMIN_TAG = "tempmin";
    public static final String TEMPMAX_TAG = "tempmax";
    public static final String TEXT_TAG = "text";
    public static final String PHENOMENON_TAG = "phenomenon";
    public static final String PLACE_TAG = "place";
    public static final String NAME_TAG = "name";
    public static final String DATE_ATTR = "date";

    // holds the location info that the user has selected in preferences
    private String location;

    public XmlQueryUtils(String location) {
        this.location = location;
    }

    public List<Forecast> fetchForecastData(String requestUrl) {

        List<Forecast> forecasts = new ArrayList<>();

        // Create URL object
        URL url = createUrl(requestUrl);

        try {
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setReadTimeout(10000 /* milliseconds */);
                urlConnection.setConnectTimeout(15000 /* milliseconds */);
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // If the request was successful (response code 200),
                // then read the input stream and parse the response.
                if (urlConnection.getResponseCode() == 200) {

                    inputStream = urlConnection.getInputStream();
                    forecasts = parseXmlData(inputStream);

                } else {
                    Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Problem retrieving the xml results.", e);
            } catch (XmlPullParserException e) {
                Log.e(LOG_TAG, "Problem with parsing the xml.", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            }

        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        return forecasts;
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /* Parses the XML data tag by tag */
    private List<Forecast> parseXmlData(InputStream stream) throws XmlPullParserException, IOException {

        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);

        Forecast forecast = null;
        String text = "";
        List<Forecast> forecastObjects = new ArrayList<>();
        int eventType = parser.getEventType();
        boolean inNightTag = true;

        // true if the <place> tag that matches the preferences location has been found
        // in that case the next <place> tags can be skipped
        boolean placeFound = false;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            String tagname = parser.getName();
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    if (tagname.equalsIgnoreCase(FORECAST_TAG)) {
                        forecast = new Forecast();
                        forecast.setDate(parser.getAttributeValue(null, DATE_ATTR));
                    }
                    if (tagname.equalsIgnoreCase(NIGHT_TAG)) {
                        inNightTag = true;
                    }
                    if (tagname.equalsIgnoreCase(DAY_TAG)) {
                        inNightTag = false;
                        // place needs to be parsed again for day values
                        placeFound = false;
                    }

                    // no need to parse <place> tag when it has been found previously
                    if (tagname.equalsIgnoreCase(PLACE_TAG) && placeFound) {
                        skip(parser);
                    }
                    if (tagname.equalsIgnoreCase(DAY_TAG)) {
                        inNightTag = false;
                    }
                    break;

                case XmlPullParser.TEXT:
                    text = parser.getText();
                    break;

                case XmlPullParser.END_TAG:
                    if (tagname.equalsIgnoreCase(FORECAST_TAG)) {
                        forecastObjects.add(forecast);
                    } else if (tagname.equalsIgnoreCase(PHENOMENON_TAG)) {
                        setPhenomenonValue(text, inNightTag, forecast);
                    } else if (tagname.equalsIgnoreCase(TEXT_TAG)) {
                        setTextValue(text, inNightTag, forecast);
                    } else if (tagname.equalsIgnoreCase(TEMPMIN_TAG)) {
                        setTempMinValue(text, inNightTag, forecast);
                    } else if (tagname.equalsIgnoreCase(TEMPMAX_TAG)) {
                        setTempMaxValue(text, inNightTag, forecast);
                    } else if (tagname.equalsIgnoreCase(NAME_TAG)) {
                        if (text.equalsIgnoreCase(location)) {
                            placeFound = true;
                        }
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }

        stream.close();

        return forecastObjects;

    }

    /* Sets the TextValue value for forecast object */
    private void setTextValue(String text, boolean inNightTag, Forecast forecast) {
        if (inNightTag) {
            forecast.setNightDescription(text);
        } else {
            forecast.setDayDescription(text);
        }
    }

    /* Sets the Phenomenon value for forecast object */
    private void setPhenomenonValue(String text, boolean inNightTag, Forecast forecast) {
        if (inNightTag) {
            // For night we only need the phenomenon value to resolve the icon, it is not used elsewhere
            forecast.setNightIconId(text);
        } else {
            forecast.setDayPhenomenon(text);
            forecast.setDayIconId(text);
        }
    }

    /* Sets the TempMin value for forecast object */
    private void setTempMinValue(String text, boolean inNightTag, Forecast forecast) {
        if (inNightTag) {
            forecast.setNightTempMin(text);
        } else {
            forecast.setDayTempMin(text);
        }
    }

    /* Sets the TempMax value for forecast object */
    private void setTempMaxValue(String text, boolean inNightTag, Forecast forecast) {
        if (inNightTag) {
            forecast.setNightTempMax(text);
        } else {
            forecast.setDayTempMax(text);
        }
    }

    /* A helper method for skipping a tag when parsing xml */
    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

}
