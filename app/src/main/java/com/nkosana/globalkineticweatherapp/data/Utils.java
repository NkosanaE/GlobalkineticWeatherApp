package com.nkosana.globalkineticweatherapp.data;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import androidx.annotation.ColorInt;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.URLUtil;
import android.widget.Toast;


import com.nkosana.globalkineticweatherapp.R;
import com.nkosana.globalkineticweatherapp.model.ItemLocation;
import com.nkosana.globalkineticweatherapp.widget.MyWidgetProvider;

import java.net.URI;
import java.util.ArrayList;

public class Utils {
    /**
     * Preference for location
     */
    public static ItemLocation getLocation(String id, Context context) {
        SharedPreferences pref = context.getSharedPreferences(id, context.MODE_PRIVATE);
        ItemLocation itemloc = new ItemLocation();
        itemloc.setId(pref.getString("id", "null"));
        itemloc.setName(pref.getString("name", "null"));
        itemloc.setCode(pref.getString("code", "null"));
        itemloc.setJsonWeather(pref.getString("jsonWeather", "null"));
        itemloc.setJsonForecast(pref.getString("jsonForecast", "null"));
        return itemloc;
    }

    public static void setUpdateWidget(Context context) {
        final Intent intent = new Intent(context, MyWidgetProvider.class);
        final PendingIntent pending = PendingIntent.getService(context, 0, intent, 0);
        final AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pending);
        long interval = 1000 * 5;
        alarm.setRepeating(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime(), interval, pending);
    }

    public static void saveLocation(ItemLocation itemloc, Context context) {
        SharedPreferences pref = context.getSharedPreferences(itemloc.getId(), context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putString("id", itemloc.getId());
        prefEditor.putString("name", itemloc.getName());
        prefEditor.putString("code", itemloc.getCode());
        prefEditor.putString("jsonWeather", itemloc.getJsonWeather());
        prefEditor.putString("jsonForecast", itemloc.getJsonForecast());
        prefEditor.commit();
        if (!isLocationExist(itemloc.getId(), context)) {
            ArrayList<String> listcode = getListCode(context);
            listcode.add(itemloc.getId());
            saveListCode(listcode, context);
        }
    }

    //checking existed location
    public static boolean isLocationExist(String id, Context context) {
        ArrayList<String> listcode = getListCode(context);
        boolean found = false;
        for (int i = 0; i < listcode.size(); i++) {
            if (listcode.get(i).equals(id)) {
                found = true;
                return found;
            }
        }
        return found;
    }

    public static void deleteLocation(String id, Context context) {
        // find code to be delete
        ArrayList<String> listcode = getListCode(context);
        for (int i = 0; i < listcode.size(); i++) {
            if (listcode.get(i).equals(id)) {
                listcode.remove(i);
            }
        }
        saveListCode(listcode, context);
        SharedPreferences pref = context.getSharedPreferences(id, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        if (id.equals(getStringPref(Constant.S_KEY_CURRENT_ID, getDefaultCity(context), context))) {
            if (getListCode(context).size() >= 0) {
                setStringPref(Constant.S_KEY_CURRENT_ID, getListCode(context).get(0), context);
            } else {
                setStringPref(Constant.S_KEY_CURRENT_ID, "null", context);
            }
        }
    }

    //this is to get enrolled city code
    public static ArrayList<String> getListCode(Context context) {
        ArrayList<String> listcode = new ArrayList<String>();
        String s = getStringPref(Constant.S_KEY_LIST_LOCATION, "null", context);
        if (!s.equals("null")) {
            for (int i = 0; i < s.split("\\|").length; i++) {
                listcode.add(s.split("\\|")[i]);
            }
        }
        return listcode;
    }

    public static void saveListCode(ArrayList<String> listcode, Context context) {
        String s = "";
        for (int i = 0; i < listcode.size(); i++) {
            s = s + listcode.get(i) + "|";
        }
        setStringPref(Constant.S_KEY_LIST_LOCATION, s, context);
    }

    public static String getCurrentCityId(Context context) {
        return getStringPref(Constant.S_KEY_CURRENT_ID, getDefaultCity(context), context);
    }

    /**
     * Universal shared preference
     * for string
     */
    public static String getStringPref(String key_val, String def_val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_" + key_val, context.MODE_PRIVATE);
        return pref.getString(key_val, def_val);
    }

    public static void setStringPref(String key_val, String val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_" + key_val, context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putString(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for integer
     */
    public static int getIntPref(String key_val, int def_val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_" + key_val, context.MODE_PRIVATE);
        return pref.getInt(key_val, def_val);
    }

    public static void setIntPref(String key_val, int val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_" + key_val, context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putInt(key_val, val);
        prefEditor.commit();
    }

    /**
     * Universal shared preference
     * for boolean
     */
    public static boolean getBooleanPref(String key_val, boolean def_val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_" + key_val, context.MODE_PRIVATE);
        return pref.getBoolean(key_val, def_val);
    }

    public static void setBooleanPref(String key_val, boolean val, Context context) {
        SharedPreferences pref = context.getSharedPreferences("pref_" + key_val, context.MODE_PRIVATE);
        SharedPreferences.Editor prefEditor = pref.edit();
        prefEditor.clear();
        prefEditor.putBoolean(key_val, val);
        prefEditor.commit();
    }

    public static String getDefaultCity(Context context) {
        return context.getResources().getString(R.string.default_city_code);
    }

    public static String getTemp(Double d, Context context) {
        if (getIntPref(Constant.I_KEY_UNIT, 0, context) == 0) { // for celcius
            return Constant.sSpiltter(d) + " \u00b0C";

        } else { // for farhenheit
            Double F = (d * (9 / 5)) + 32;
            return Constant.sSpiltter(F) + " \u00b0F";
        }
    }

    private static boolean isLollipopOrHigher() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    public static void systemBarLollipop(Activity act) {
        if (isLollipopOrHigher()) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    public static void systemBarLollipop(Activity act, @ColorInt int colorInt) {
        if (isLollipopOrHigher()) {
            Window window = act.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(colorDarker(colorInt));
        }
    }

    private static int colorDarker(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f; // value component
        return Color.HSVToColor(hsv);
    }

    public static void directLinkToBrowser(Activity activity, String url) {
        url = appendQuery(url, "t=" + System.currentTimeMillis());
        if (!URLUtil.isValidUrl(url)) {
            Toast.makeText(activity, "Ops, Cannot open url", Toast.LENGTH_LONG).show();
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    private static String appendQuery(String uri, String appendQuery) {
        try {
            URI oldUri = new URI(uri);
            String newQuery = oldUri.getQuery();
            if (newQuery == null) {
                newQuery = appendQuery;
            } else {
                newQuery += "&" + appendQuery;
            }
            URI newUri = new URI(
                    oldUri.getScheme(),
                    oldUri.getAuthority(),
                    oldUri.getPath(), newQuery, oldUri.getFragment()
            );
            return newUri.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return uri;
        }
    }
}
