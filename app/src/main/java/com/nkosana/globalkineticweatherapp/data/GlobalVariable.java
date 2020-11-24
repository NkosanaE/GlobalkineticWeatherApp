package com.nkosana.globalkineticweatherapp.data;

import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.widget.ImageView;
import android.widget.LinearLayout;


import com.nkosana.globalkineticweatherapp.R;
import com.nkosana.globalkineticweatherapp.model.ItemLocation;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GlobalVariable extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }


    /**
     * Universal shared preference
     * for boolean
     */
    public boolean getBooleanPref(String key_val, boolean def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getBoolean(key_val, def_val);
    }

    public void setBooleanPref(String key_val, boolean val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putBoolean(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for integer
     */
    public int getIntPref(String key_val, int def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getInt(key_val, def_val);
    }

    public void setIntPref(String key_val, int val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putInt(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for string
     */
    public String getStringPref(String key_val, String def_val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        return pref.getString(key_val, def_val);
    }

    public void setStringPref(String key_val, String val) {
        SharedPreferences pref = getSharedPreferences("pref_" + key_val, MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putString(key_val, val);
        prefEditor.commit();
    }

    public String getDefaultCity() {
        return Utils.getDefaultCity(getApplicationContext());
    }


    /**
     * Preference for location
     */
    public ItemLocation getLocation(String id) {
        return Utils.getLocation(id, getApplicationContext());
    }

    public void saveLocation(ItemLocation itemloc) {
        Utils.saveLocation(itemloc, getApplicationContext());
    }

    //checking existed location
    public boolean isLocationExist(String id) {
        return Utils.isLocationExist(id, getApplicationContext());
    }

    public void deleteLocation(String id) {
        Utils.deleteLocation(id, getApplicationContext());
    }

    //this is to get enrolled city code
    public ArrayList<String> getListCode() {
        return Utils.getListCode(getApplicationContext());
    }

    public void saveListCode(ArrayList<String> listcode) {
        Utils.saveListCode(listcode, getApplicationContext());
    }


    public String generateCurrentDate(int format_key) {
        Date curDate = new Date();
        String DateToStr = "";
        //default 11-5-2014 11:11:51
        SimpleDateFormat format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");

        switch (format_key) {
            case 1:

                format = new SimpleDateFormat("dd/MM/yyy");
                DateToStr = format.format(curDate);
                break;

            case 2:
                //May 11, 2014 11:37 PM
                DateToStr = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT).format(curDate);
                break;
            case 3:
                //11-5-2014 11:11:51
                format = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");
                DateToStr = format.format(curDate);
                break;
        }
        return DateToStr;
    }

    public void setDrawableIcon(String icon, ImageView im) {
        if (icon.equals("01d") || icon.equals("01n")) { // clear sky
            im.setBackgroundResource(R.drawable.w_clear);

        } else if (icon.equals("02d") || icon.equals("02n")) { //few clouds
            im.setBackgroundResource(R.drawable.w_fewcloud);
        } else if (icon.equals("03d") || icon.equals("03n")) { // scattered clouds
            im.setBackgroundResource(R.drawable.w_cloud);
        } else if (icon.equals("04d") || icon.equals("04n")) { //broken clouds
            im.setBackgroundResource(R.drawable.w_cloud);

        } else if (icon.equals("09d") || icon.equals("09n")) {  //shower rain
            im.setBackgroundResource(R.drawable.w_shower);

        } else if (icon.equals("10d") || icon.equals("10n")) { //rain
            im.setBackgroundResource(R.drawable.w_rain);

        } else if (icon.equals("11d") || icon.equals("11n")) { //thunderstorm
            im.setBackgroundResource(R.drawable.w_thunderstorm);

        } else if (icon.equals("13d") || icon.equals("13n")) { //snow
            im.setBackgroundResource(R.drawable.w_snow);

        } else if (icon.equals("50d") || icon.equals("50n")) { //mist
            im.setBackgroundResource(R.drawable.w_mist);

        } else {
            im.setBackgroundResource(R.drawable.w_fewcloud);
        }
    }

    public void setDrawableSmallIcon(String icon, ImageView im) {

        if (icon.equals("01d") || icon.equals("01n")) { // clear sky
            im.setBackgroundResource(R.drawable.w_small_clear);

        } else if (icon.equals("02d") || icon.equals("02n")) { //few clouds
            im.setBackgroundResource(R.drawable.w_small_fewcloud);

        } else if (icon.equals("03d") || icon.equals("03n")) { // scattered clouds
            im.setBackgroundResource(R.drawable.w_small_cloud);

        } else if (icon.equals("04d") || icon.equals("04n")) { //broken clouds
            im.setBackgroundResource(R.drawable.w_small_cloud);

        } else if (icon.equals("09d") || icon.equals("09n")) {  //shower rain
            im.setBackgroundResource(R.drawable.w_small_shower);

        } else if (icon.equals("10d") || icon.equals("10n")) { //rain
            im.setBackgroundResource(R.drawable.w_small_rain);

        } else if (icon.equals("11d") || icon.equals("11n")) { //thunderstorm
            im.setBackgroundResource(R.drawable.w_small_thunderstorm);

        } else if (icon.equals("13d") || icon.equals("13n")) { //snow
            im.setBackgroundResource(R.drawable.w_small_snow);

        } else if (icon.equals("50d") || icon.equals("50n")) { //mist
            im.setBackgroundResource(R.drawable.w_small_mist);

        } else {
            im.setBackgroundResource(R.drawable.w_fewcloud);
        }
    }

    public int setLytColor(String icon, LinearLayout lyt) {
        int colorInt;

        String color[] = getResources().getStringArray(R.array.color_weather);
        if (icon.equals("01d") || icon.equals("01n")) { // clear sky
            colorInt = Color.parseColor(color[0]);

        } else if (icon.equals("02d") || icon.equals("02n")) { // few clouds
            colorInt = Color.parseColor(color[1]);

        } else if (icon.equals("03d") || icon.equals("03n")) { // scatteredclouds
            colorInt = Color.parseColor(color[2]);

        } else if (icon.equals("04d") || icon.equals("04n")) { // broken clouds
            colorInt = Color.parseColor(color[3]);

        } else if (icon.equals("09d") || icon.equals("09n")) { // shower rain
            colorInt = Color.parseColor(color[4]);

        } else if (icon.equals("10d") || icon.equals("10n")) { // rain
            colorInt = Color.parseColor(color[5]);

        } else if (icon.equals("11d") || icon.equals("11n")) { // thunderstorm
            colorInt = Color.parseColor(color[6]);

        } else if (icon.equals("13d") || icon.equals("13n")) { // snow
            colorInt = Color.parseColor(color[7]);

        } else if (icon.equals("50d") || icon.equals("50n")) { // mist
            colorInt = Color.parseColor(color[8]);

        } else {
            colorInt = Color.parseColor(color[9]);
        }

        lyt.setBackgroundColor(colorInt);
        return colorInt;
    }

    public void setLytColorDrw(String icon, GradientDrawable gd) {

        String color[] = getResources().getStringArray(R.array.color_weather);
        if (icon.equals("01d") || icon.equals("01n")) { // clear sky
            gd.setColor(Color.parseColor(color[0]));

        } else if (icon.equals("02d") || icon.equals("02n")) { // few clouds
            gd.setColor(Color.parseColor(color[1]));

        } else if (icon.equals("03d") || icon.equals("03n")) { // scatteredclouds
            gd.setColor(Color.parseColor(color[2]));

        } else if (icon.equals("04d") || icon.equals("04n")) { // broken clouds
            gd.setColor(Color.parseColor(color[3]));

        } else if (icon.equals("09d") || icon.equals("09n")) { // shower rain
            gd.setColor(Color.parseColor(color[4]));

        } else if (icon.equals("10d") || icon.equals("10n")) { // rain
            gd.setColor(Color.parseColor(color[5]));

        } else if (icon.equals("11d") || icon.equals("11n")) { // thunderstorm
            gd.setColor(Color.parseColor(color[6]));

        } else if (icon.equals("13d") || icon.equals("13n")) { // snow
            gd.setColor(Color.parseColor(color[7]));

        } else if (icon.equals("50d") || icon.equals("50n")) { // mist
            gd.setColor(Color.parseColor(color[8]));

        } else {
            gd.setColor(Color.parseColor(color[9]));
        }
    }

    public String getTime(Long a) {
        //DateFormat ddf = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
        Date d = new Date(a * 1000);
        String h = d.getHours() + "";
        String m = d.getMinutes() + "";
        if (h.length() == 1) {
            h = "0" + h;
        }
        if (m.length() == 1) {
            m = "0" + m;
        }
        return h + ":" + m;
    }

    public String getDay(Long l) {
        Date time = new Date(l * 1000);
        Calendar cal = Calendar.getInstance();
        cal.setTime(time);
        int daynum = cal.get(Calendar.DAY_OF_WEEK);

        switch (daynum) {
            case 1:
                return getString(R.string.sunday);
            case 2:
                return getString(R.string.monday);
            case 3:
                return getString(R.string.tuesday);
            case 4:
                return getString(R.string.wednesday);
            case 5:
                return getString(R.string.thursday);
            case 6:
                return getString(R.string.friday);
            case 7:
                return getString(R.string.saturday);
        }
        return null;
    }

    public String getLastUpdate(Long l) {
        Date curDate = new Date(l * 1000);
        //Wed, 4 Jul 2001 12:08:56 -0700
        SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yyyy HH:mm");
        String dateToStr = format.format(curDate);
        return "LAST UPDATE " + dateToStr.toUpperCase();
    }


    public String getLastUpdateDate(Long l) {
        Date curDate = new Date(l * 1000);
        //Wed, 4 Jul 2001 12:08:56 -0700
        SimpleDateFormat format = new SimpleDateFormat("EEE d MMM yyyy HH:mm");
        String dateToStr = format.format(curDate);
        return dateToStr.toUpperCase();
    }

    public String getTemp(Double d) {
        return Utils.getTemp(d, getApplicationContext());
    }

}
