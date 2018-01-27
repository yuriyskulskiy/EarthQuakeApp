package com.example.yuriy.myapplication.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConverter {

    private static final String DAY_TIME_FORMAT = "h:mm a";
    private static final String DAY_FORMAT = "MM dd, yyyy";

    private DateConverter() {
    }

    public static String convertToDay(long timeInMilliseconds) {
        Date dateObject = new Date(timeInMilliseconds);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DAY_FORMAT);
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }

    public static String convertToTime(long timeInMilliseconds) {
        Date dateObject = new Date(timeInMilliseconds);
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DAY_TIME_FORMAT);
        String dateToDisplay = dateFormatter.format(dateObject);
        return dateToDisplay;
    }
}
