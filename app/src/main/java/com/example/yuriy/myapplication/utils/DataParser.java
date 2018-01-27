package com.example.yuriy.myapplication.utils;


import android.text.TextUtils;
import android.util.Log;

import com.example.yuriy.myapplication.model.EarthQuake;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

class DataParser {


    public static ArrayList<EarthQuake> parseForQuakeData(String jsonResponse) {

        Log.e("qqq", "start = ");
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        ArrayList<EarthQuake> resultEarthQuakeList = new ArrayList<>();
        try {

            JSONObject root = new JSONObject(jsonResponse);
            JSONArray featuresJsonArray = root.getJSONArray("features");
            for (int i = 0; i < featuresJsonArray.length(); i++) {

                JSONObject featureObject = featuresJsonArray.getJSONObject(i);
                JSONObject propertiesObject = featureObject.getJSONObject("properties");

                String city = propertiesObject.getString("place");
                Log.e("qqq", "----city = " + city);

                Long time = propertiesObject.getLong("time");
                Log.e("qqq", "time = " + time);

                double magnitude = propertiesObject.getDouble("mag");
                Log.e("qqq", "mag: " + magnitude);

                String UrlString = propertiesObject.getString("url");

                EarthQuake earthQuakeObject = new EarthQuake();
                earthQuakeObject.setData(time);

                earthQuakeObject.setCity(city);
                earthQuakeObject.setMagnitude(magnitude);
                earthQuakeObject.setUrl(UrlString);

                resultEarthQuakeList.add(earthQuakeObject);

            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }


        return resultEarthQuakeList;
    }

}
