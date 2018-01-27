package com.example.yuriy.myapplication;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yuriy.myapplication.model.EarthQuake;
import com.example.yuriy.myapplication.utils.DateConverter;
import com.example.yuriy.myapplication.utils.LocationSeparator;
import com.example.yuriy.myapplication.utils.MagnitudeColorCreator;

import java.util.ArrayList;

import static com.example.yuriy.myapplication.utils.MagnitudeFormatter.formatMagnitude;


public class EarthquakeListAdapter extends ArrayAdapter<EarthQuake> {

    public static final String TAG = EarthquakeListAdapter.class.getName();

    public EarthquakeListAdapter(@NonNull Context context, ArrayList<EarthQuake> dataList) {
        super(context, 0, dataList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            listItemView = inflater.inflate(R.layout.list_item, parent, false);
        }

        EarthQuake currentEarthQuakeObject = getItem(position);

        TextView magnitudeTV = listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude = formatMagnitude(currentEarthQuakeObject.getMagnitude());
        magnitudeTV.setText(formattedMagnitude);

        GradientDrawable gradientDrawableCircle = (GradientDrawable) magnitudeTV.getBackground();
        int magnitudeColor = MagnitudeColorCreator.create(getContext(), currentEarthQuakeObject.getMagnitude());
        gradientDrawableCircle.setColor(magnitudeColor);

        TextView cityTV = listItemView.findViewById(R.id.city);
        TextView proximityCoordinatesTV = listItemView.findViewById(R.id.proximity_coordinates);
//        cityTV.setText(String.valueOf(currentEarthQuakeObject.getCity()));

        String rawLocation = currentEarthQuakeObject.getCity();
        LocationSeparator separator = new LocationSeparator(rawLocation);
        proximityCoordinatesTV.setText(separator.getProximityCoordinates());
        cityTV.setText(separator.getCityCountry());


        TextView dayTV = listItemView.findViewById(R.id.date);
        TextView dayTimeTV = listItemView.findViewById(R.id.time);

        long timeInMilliseconds = currentEarthQuakeObject.getData();
        String dayToDisplay = DateConverter.convertToDay(timeInMilliseconds);
        dayTV.setText(dayToDisplay);


        String dayTimeToDisplay = DateConverter.convertToTime(timeInMilliseconds);
        Log.e("time","--------------"+dayTimeToDisplay);

        dayTimeTV.setText(dayTimeToDisplay);



        return listItemView;
    }
}
