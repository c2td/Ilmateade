package com.example.pm.forecast;

/* A class that represents one forecast object */

import android.os.Parcel;
import android.os.Parcelable;

class Forecast implements Parcelable {

    private String date;
    private int dayIconId;
    private int nightIconId;

    private String nightTempMax;
    private String nightTempMin;
    private String nightPhenomenon;
    private String nightDescription;

    private String dayTempMax;
    private String dayTempMin;
    private String dayPhenomenon;
    private String dayDescription;

    // possible phenomenon tag values
    private final String CLEAR = "clear";
    private final String CLOUDY = "cloudy";
    private final String CLOUDS = "clouds";
    private final String SPELLS = "spells";
    private final String SNOW = "snow";
    private final String FOG = "fog";
    private final String MIST = "mist";
    private final String HAIL = "hail";
    private final String THUNDER = "thunder";
    private final String THUNDERSTORM = "thunderstorm";
    private final String SHOWER = "shower";
    private final String RAIN = "rain";
    private final String SLEET = "sleet";
    private final String SNOWFALL = "snowfall";

    public Forecast() {}

    // Parcel constructor
    public Forecast(Parcel in){
        String[] data= new String[9];
        int[] iconData = new int[2];

        in.readStringArray(data);
        in.readIntArray(iconData);

        date = data[0];
        nightPhenomenon = data[1];
        nightTempMax = data[2];
        nightTempMin = data[3];
        nightDescription = data[4];
        dayPhenomenon = data[5];
        dayTempMax = data[6];
        dayTempMin = data[7];
        dayDescription = data[8];

        dayIconId = iconData[0];
        nightIconId = iconData[1];
    }

    public void setDate(String date) {
        this.date = formatDate(date);
    }

    public String getDate() {
        return date;
    }

    public void setNightTempMin(String nightTempMin) {
        this.nightTempMin = nightTempMin;
    }

    public Double getNightTempMin() {
        return Double.parseDouble(nightTempMin);
    }

    public void setNightTempMax(String nightTempMax) {
        this.nightTempMax = nightTempMax;
    }

    public Double getNightTempMax() {
        return Double.parseDouble(nightTempMax);
    }

    public void setNightDescription(String nightDescription) {
        this.nightDescription = nightDescription;
    }

    public String getNightDescription() {
        return nightDescription;
    }

    public void setDayTempMin(String dayTempMin) {
        this.dayTempMin = dayTempMin;
    }

    public Double getDayTempMin() {
        return Double.parseDouble(dayTempMin);
    }

    public void setDayTempMax(String dayTempMax) {
        this.dayTempMax = dayTempMax;
    }

    public Double getDayTempMax() {
        return Double.parseDouble(dayTempMax);
    }

    public void setDayPhenomenon(String dayPhenomenon) {
        this.dayPhenomenon = resolvePhenomenon(dayPhenomenon);
    }

    public String getDayPhenomenon() {
        return dayPhenomenon;
    }

    public void setDayDescription(String dayDescription) {
        this.dayDescription = dayDescription;
    }

    public String getDayDescription() {
        return dayDescription;
    }

    /* Sets the icon depending on phenomenon  value */
    public void setDayIconId(String text) {
        if (text.contains(SLEET) || text.contains(SNOW)) {
            dayIconId = R.drawable.snow;
        } else if (text.contains(SHOWER) || text.contains(RAIN)) {
            dayIconId = R.drawable.rain;
        } else if (text.equalsIgnoreCase(CLEAR)) {
            dayIconId = R.drawable.sun;
        } else if (text.contains(CLOUDS) || text.contains(SPELLS)) {
            dayIconId = R.drawable.cloudy;
        } else {
            dayIconId = R.drawable.cloud;
        }
    }

    public int getDayIconId() {
        return dayIconId;
    }

    /* Sets the icon based on phenomenon  value */
    public void setNightIconId(String text) {
        if ( text.contains(SLEET) || text.contains(SNOW)) {
            nightIconId = R.drawable.snow;
        } else if (text.contains(SHOWER) || text.contains(RAIN)) {
            nightIconId = R.drawable.rain;
        } else if (text.equalsIgnoreCase(CLEAR)) {
            nightIconId = R.drawable.moon;
        } else if (text.contains(CLOUDS) || text.contains(SPELLS)) {
            nightIconId = R.drawable.cloudy_night;
        } else {
            nightIconId = R.drawable.cloud;
        }
    }

    public int getNightIconId() {
        return nightIconId;
    }

    /* Helper method for resolving the <phenomenon> tag values*/
    private String resolvePhenomenon(String text) {
        if (text.equalsIgnoreCase(CLEAR)) {
            return MainActivity.resources.getString(R.string.clear);
        } else if (text.equalsIgnoreCase(CLOUDY)) {
            return MainActivity.resources.getString(R.string.cloudy);
        } else if (text.contains(CLOUDS)) {
            return MainActivity.resources.getString(R.string.clouds);
        } else if (text.contains(SPELLS)) {
            return MainActivity.resources.getString(R.string.spells);
        } else if (text.contains(SNOW)) {
            return MainActivity.resources.getString(R.string.snow);
        } else if (text.contains(FOG)) {
            return MainActivity.resources.getString(R.string.fog);
        } else if (text.contains(MIST)) {
            return MainActivity.resources.getString(R.string.mist);
        } else if (text.contains(HAIL)) {
            return MainActivity.resources.getString(R.string.hail);
        } else if (text.contains(THUNDER)) {
            return MainActivity.resources.getString(R.string.thunder);
        } else if (text.contains(THUNDERSTORM)) {
            return MainActivity.resources.getString(R.string.thunderstorm);
        } else return MainActivity.resources.getString(R.string.rain);
    }

    /* Helper method for formatting the date */
    private String formatDate(String date) {

        StringBuilder sb = new StringBuilder();
        String[] dateParts = date.split("-");
        sb.append(dateParts[2]);
        switch (dateParts[1]) {
            case "01": sb.append(". ").append(MainActivity.resources.getString(R.string.month_1));
                break;
            case "02": sb.append(". ").append(MainActivity.resources.getString(R.string.month_2));
                break;
            case "03": sb.append(". ").append(MainActivity.resources.getString(R.string.month_3));
                break;
            case "04": sb.append(". ").append(MainActivity.resources.getString(R.string.month_4));
                break;
            case "05": sb.append(". ").append(MainActivity.resources.getString(R.string.month_5));
                break;
            case "06": sb.append(". ").append(MainActivity.resources.getString(R.string.month_6));
                break;
            case "07": sb.append(". ").append(MainActivity.resources.getString(R.string.month_7));
                break;
            case "08": sb.append(". ").append(MainActivity.resources.getString(R.string.month_8));
                break;
            case "09": sb.append(". ").append(MainActivity.resources.getString(R.string.month_9));
                break;
            case "10": sb.append(". ").append(MainActivity.resources.getString(R.string.month_10));
                break;
            case "11": sb.append(". ").append(MainActivity.resources.getString(R.string.month_11));
                break;
            default: sb.append(". ").append(MainActivity.resources.getString(R.string.month_12));
        }

        return sb.toString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Write the object to parcel for sending it to other activity
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeStringArray(new String[]{this.date, this.nightPhenomenon, this.nightTempMax, this.nightTempMin,
                this.nightDescription, this.dayPhenomenon, this.dayTempMax, this.dayTempMin, this.dayDescription});

        dest.writeIntArray(new int[]{this.dayIconId, this.nightIconId});
    }

    public static final Creator<Forecast> CREATOR = new Creator<Forecast>() {

        @Override
        public Forecast createFromParcel(Parcel source) {
            return new Forecast(source);
        }

        @Override
        public Forecast[] newArray(int size) {
            return new Forecast[size];
        }
    };

}