package com.example.yuriy.myapplication.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.yuriy.myapplication.model.EarthQuake;
import com.example.yuriy.myapplication.utils.NetworkUtils;

import java.util.ArrayList;



public class EarthQuakeLoader extends AsyncTaskLoader<ArrayList<EarthQuake>> {

    private String url;



    public EarthQuakeLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    public ArrayList<EarthQuake> loadInBackground() {
        String urlString = url;
        if (urlString == null) {
            return null;
        }
        ArrayList<EarthQuake> earthQuakesList = NetworkUtils.getEarthQuakeData(urlString);
        return earthQuakesList;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
