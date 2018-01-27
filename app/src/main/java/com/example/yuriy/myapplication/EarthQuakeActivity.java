package com.example.yuriy.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yuriy.myapplication.loader.EarthQuakeLoader;
import com.example.yuriy.myapplication.model.EarthQuake;
import com.example.yuriy.myapplication.settings.SettingsActivity;
import com.example.yuriy.myapplication.utils.NetworkUtils;

import java.util.ArrayList;


;

public class EarthQuakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<EarthQuake>> {

    //    public static final String REQUEST_STRING = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/4.5_week.geojson";
//    public static final String REQUEST_STRING = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-02-01";
//    public static final String REQUEST_STRING = "http://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=1&limit=10";
//    public static final String REQUEST_STRING =   "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    public static final String BASE_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query";
    //    public static final String REQUEST_STRING = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    public static final String TAG = EarthQuakeActivity.class.getName();
    ListView earthQuakeListView;
    private ProgressBar progressBar;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earth_quake);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        earthQuakeListView = findViewById(R.id.list_view);
        earthQuakeListView.setVisibility(View.INVISIBLE);
        emptyView = findViewById(R.id.emptyView);


        Toast.makeText(this, BASE_REQUEST_URL, Toast.LENGTH_SHORT).show();
        if (hasInternetConnectivity()) {
            getSupportLoaderManager().initLoader(1, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            emptyView.setText("Опаньки няма интернету!!!");
            earthQuakeListView.setEmptyView(emptyView);
        }

    }


    @Override
    public Loader<ArrayList<EarthQuake>> onCreateLoader(int id, Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPreferences.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));


        Uri baseUri = Uri.parse(BASE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "20");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthQuakeLoader(this, uriBuilder.toString());

//        return new EarthQuakeLoader(this, REQUEST_STRING);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<EarthQuake>> loader, ArrayList<EarthQuake> data) {
        progressBar.setVisibility(View.INVISIBLE);
        earthQuakeListView.setVisibility(View.VISIBLE);
        earthQuakeListView.setEmptyView(emptyView);

        if (data == null || data.size() == 0) {
            return;
        }
        ArrayAdapter<EarthQuake> adapter = new EarthquakeListAdapter(EarthQuakeActivity.this, (ArrayList<EarthQuake>) data);
        earthQuakeListView.setAdapter(adapter);

        activateClickEvent(earthQuakeListView, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        if (selectedItemId == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<EarthQuake>> loader) {

    }


    private class EarthQuakeAsyncTask extends AsyncTask<String, Void, ArrayList<EarthQuake>> {

        @Override
        protected ArrayList<EarthQuake> doInBackground(String... strings) {
            String urlString = strings[0];
            if (urlString == null || strings.length < 1) {
                return null;
            }
            ArrayList<EarthQuake> earthQuakesList = NetworkUtils.getEarthQuakeData(urlString);
            return earthQuakesList;
        }

        @Override
        protected void onPostExecute(ArrayList<EarthQuake> earthQuakes) {
            ArrayAdapter<EarthQuake> adapter = new EarthquakeListAdapter(EarthQuakeActivity.this, earthQuakes);
            earthQuakeListView.setAdapter(adapter);
            progressBar.setVisibility(View.INVISIBLE);
            earthQuakeListView.setVisibility(View.VISIBLE);
            activateClickEvent(earthQuakeListView, earthQuakes);
        }


    }

    private boolean hasInternetConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    private void activateClickEvent(ListView earthQuakeListView, final ArrayList<EarthQuake> earthQuakesList) {
        earthQuakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake clickedEarthQuake = earthQuakesList.get(position);
                String currentUrl = clickedEarthQuake.getUrl();
                Toast.makeText(EarthQuakeActivity.this, currentUrl, Toast.LENGTH_SHORT).show();

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentUrl));
                startActivity(browserIntent);
            }
        });
    }
}
