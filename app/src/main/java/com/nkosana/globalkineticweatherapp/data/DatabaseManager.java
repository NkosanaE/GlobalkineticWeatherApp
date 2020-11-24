package com.nkosana.globalkineticweatherapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.nkosana.globalkineticweatherapp.data.DatabaseHelper;
import com.nkosana.globalkineticweatherapp.model.City;


import java.util.ArrayList;


public class DatabaseManager {


    private static final String DATABASE_NAME = "db_city.sqlite";
    private static final String TABLE_NAME = "city_list";

    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase db;

    public DatabaseManager(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, 1);
        db = dbHelper.openDataBase();
    }

    public ArrayList<City> getCity() {
        ArrayList<City> data = new ArrayList<City>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                City city = new City();
                city.setId(c.getString(0));
                city.setName(c.getString(1));
                city.setLat(c.getString(2));
                city.setLng(c.getString(3));
                city.setCode(c.getString(4));
                data.add(city);
            } while (c.moveToNext());
        }
        return data;

    }

    public ArrayList<City> getWords(String string) {
        ArrayList<City> data = new ArrayList<City>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name LIKE '" + string + "%' OR name LIKE '" + string + "' LIMIT 5";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            do {
                City city = new City();
                city.setId(c.getString(0));
                city.setName(c.getString(1));
                city.setLat(c.getString(2));
                city.setLng(c.getString(3));
                city.setCode(c.getString(4));
                data.add(city);
            } while (c.moveToNext());
        }
        return data;
    }

    public City getWordsFormAutocomplate(String s) {
        City city = null;
        String scity = "", scode = "";
        if (s.contains(",")) {
            scity = s.split("\\,")[0];
            scode = s.split("\\,")[1].trim();
        } else {
            return null;
        }

        ArrayList<City> data = new ArrayList<City>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE name= '" + scity + "' AND code= '" + scode + "'";
        Cursor c = db.rawQuery(selectQuery, null);
        if (c.moveToFirst()) {
            city = new City();
            city.setId(c.getString(0));
            city.setName(c.getString(1));
            city.setLat(c.getString(2));
            city.setLng(c.getString(3));
            city.setCode(c.getString(4));
        }
        return city;
    }

}
