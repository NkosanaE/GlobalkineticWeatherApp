package com.nkosana.globalkineticweatherapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import com.nkosana.globalkineticweatherapp.data.DatabaseManager;
import com.nkosana.globalkineticweatherapp.model.City;

import java.util.ArrayList;

public class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
    private ArrayList<String> resultList;
    private Context context;
    private DatabaseManager db;

    public PlacesAutoCompleteAdapter(Activity context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context.getApplicationContext();
        db = new DatabaseManager(context);
    }

    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null && constraint.length() >= 2) {
                    resultList = autocomplete(constraint.toString());
                    // Assign the data to the FilterResults
                    filterResults.values = resultList;
                    filterResults.count = resultList.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results != null && results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }


    private ArrayList<String> autocomplete(String input) {

        ArrayList<String> resultList = new ArrayList<String>();
        ArrayList<City> cityData = db.getWords(input);
        for (City city : cityData) {
            resultList.add(city.getName() + ", " + city.getCode());
        }
        return resultList;
    }
}
