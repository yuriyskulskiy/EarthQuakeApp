package com.example.yuriy.myapplication.utils;


import java.text.DecimalFormat;

public class MagnitudeFormatter {

    private static final String MAGNITUDE_FORMAT_PATTERN = "0.0";

    private MagnitudeFormatter() {
    }

    public static String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat(MAGNITUDE_FORMAT_PATTERN);
        return magnitudeFormat.format(magnitude);
    }

}
