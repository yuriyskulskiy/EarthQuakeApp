package com.example.yuriy.myapplication.utils;


import android.util.Log;

public class LocationSeparator {

    private static final String LOCATION_SEPARATOR = "of ";
    private boolean containsLocationSeparator;
    private String[] splitLocation;

    private String proximityCoordinates = "";
    private String cityCountry = "";

    public LocationSeparator(String validLocation) {
        containsLocationSeparator = validLocation.contains(LOCATION_SEPARATOR);
        if (containsLocationSeparator) {
            splitLocation = validLocation.split(LOCATION_SEPARATOR);
            proximityCoordinates = splitLocation[0];
            cityCountry = splitLocation[1];
            Log.e("split", "proximityCoordinates = " + proximityCoordinates);
            Log.e("split", "cityCountry = " + cityCountry);
        } else {
            cityCountry = validLocation;
        }
    }

    public String getProximityCoordinates() {
        if (containsLocationSeparator) {
            return proximityCoordinates;
        } else {
            return "Near the";
        }

    }

    public String getCityCountry() {
        return cityCountry;
    }
}
