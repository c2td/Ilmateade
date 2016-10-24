package com.example.pm.forecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ForecastAdapter extends ArrayAdapter<Forecast> {

    public ForecastAdapter(Context context, List<Forecast> forecasts) {
        super(context, 0, forecasts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;

        int layoutId;
        if (position == 0) {
            layoutId = R.layout.list_item_forecast_today;
        } else {
            layoutId = R.layout.list_item;
        }

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(layoutId, parent, false);
        }

        // Get the current item located at that position in the list
        Forecast currentItem = getItem(position);

        TextView dateView = (TextView) listItemView.findViewById(R.id.list_item_date);
        TextView phenomenonView = (TextView) listItemView.findViewById(R.id.list_item_phenomenon);
        TextView minTempView = (TextView) listItemView.findViewById(R.id.list_item_low);
        TextView maxTempView = (TextView) listItemView.findViewById(R.id.list_item_high);
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.list_item_icon);

        dateView.setText(currentItem.getDate());
        phenomenonView.setText(currentItem.getDayPhenomenon());
        minTempView.setText(getContext().getString(R.string.format_temperature, currentItem.getDayTempMin()));
        maxTempView.setText(getContext().getString(R.string.format_temperature, currentItem.getDayTempMax()));
        iconView.setImageResource(currentItem.getDayIconId());

        return listItemView;
    }
}
