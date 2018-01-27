package com.example.yuriy.myapplication.utils;


import android.util.Log;

import com.example.yuriy.myapplication.model.EarthQuake;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;


public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private NetworkUtils() {
    }

    public static ArrayList<EarthQuake> getEarthQuakeData(String urlString) {

        URL url = generateUrl(urlString);

        String jsonResponse = null;

        jsonResponse = makeHttpRequest(url);

        ArrayList<EarthQuake> earthQuakesList = DataParser.parseForQuakeData(jsonResponse);

        return earthQuakesList;
    }

    private static String makeHttpRequest(URL url) {

        String jsonResponseString = "";
        if (url == null) {
            return jsonResponseString;
        }

        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(150000);
            connection.setReadTimeout(100000);
            connection.connect();


            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                jsonResponseString = readJsonFromStream(inputStream);
            } else {
                Log.e(TAG, "makeHttpRequest: response code: " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(TAG, "makeHttpRequest: problem reading json result", e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {

                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing input stream", e);
                }

            }
        }
        return jsonResponseString;
    }

    private static String readJsonFromStream(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                line = bufferedReader.readLine();
            }
        }
        return stringBuilder.toString();
    }

    private static URL generateUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(TAG, "generateUrl: Problem building the URL ", e);
        }
        return url;
    }


}
