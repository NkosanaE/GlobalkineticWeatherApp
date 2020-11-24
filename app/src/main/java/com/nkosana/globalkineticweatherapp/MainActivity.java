package com.nkosana.globalkineticweatherapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nkosana.globalkineticweatherapp.adapter.ItemLocationAdapter;
import com.nkosana.globalkineticweatherapp.adapter.PlacesAutoCompleteAdapter;
import com.nkosana.globalkineticweatherapp.data.ConnectionDetector;
import com.nkosana.globalkineticweatherapp.data.Constant;
import com.nkosana.globalkineticweatherapp.data.DatabaseManager;
import com.nkosana.globalkineticweatherapp.data.GlobalVariable;
import com.nkosana.globalkineticweatherapp.data.Utils;
import com.nkosana.globalkineticweatherapp.json.JSONLoader;
import com.nkosana.globalkineticweatherapp.lib.app.SlidingActivity;
import com.nkosana.globalkineticweatherapp.model.ForecastResponse;
import com.nkosana.globalkineticweatherapp.model.ItemLocation;
import com.nkosana.globalkineticweatherapp.model.WeatherResponse;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends SlidingActivity {
    private TextView tv_addres, tv_date, tv_temp,
            tv_pressure, tv_humidity, tv_wind,
            tv_sunset, tv_sunrise, tv_description;

    private TextView tv_temp_[] = new TextView[5];
    private ImageView img_small_[] = new ImageView[5];

    private TextView tv_day_1, tv_day_2, tv_day_3,
            tv_day_4, tv_day_5;

    private LinearLayout lyt_main;

    private Button bt_location, bt_about;
    private Button bt_addlocation;
    public ListView listview_location;
    private ScrollView scroll_view;

    private GlobalVariable global;
    private ImageView img_weather;
    private DatabaseManager db;
    private ConnectionDetector cd;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.8F);

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    SwipeRefreshLayout mSwipeRefreshLayout;

    private boolean isOnexecute = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_page);

        setBehindContentView(R.layout.menu_location);
        getSlidingMenu().setBehindOffset(100);

        Utils.setUpdateWidget(getApplicationContext());

        global = (GlobalVariable) getApplication();
        db = new DatabaseManager(MainActivity.this);

        initComponent();
        buttonAction();
    }

    public void displayData(WeatherResponse w, ForecastResponse f) {
        try {

            String savedDate = global.getLastUpdateDate(w.dt);
            Date date_old = new Date(savedDate);
            boolean isToday = DateUtils.isToday(date_old.getTime());

            if(isToday) {

                tv_addres.setText(w.name + ", " + w.sys.country);
                tv_date.setText(global.getLastUpdate(w.dt));
                tv_temp.setText(global.getTemp(w.main.temp));
                tv_pressure.setText(Constant.sSpiltter(w.main.pressure) + " hpa");
                tv_humidity.setText(Constant.sSpiltter(w.main.humidity) + " %");
                tv_wind.setText(Constant.sSpiltter(w.wind.speed) + " m/s");
                global.setDrawableIcon(w.weather.get(0).icon, img_weather);
                int colorInt = global.setLytColor(w.weather.get(0).icon, lyt_main);
                Utils.systemBarLollipop(this, colorInt);
                tv_description.setText(w.weather.get(0).description.toUpperCase());

                tv_sunset.setText(global.getTime(w.sys.sunset) + " sunset");
                tv_sunrise.setText(global.getTime(w.sys.sunrise) + " sunrise");
                for (int i = 0; i < f.list.size(); i++) {
                    tv_temp_[i].setText(global.getTemp(f.list.get(i).temp.day));
                }

                for (int i = 0; i < f.list.size(); i++) {
                    Log.d("icon" + i, f.list.get(i).weather.get(0).icon);
                    global.setDrawableSmallIcon(f.list.get(i).weather.get(0).icon, img_small_[i]);
                }

                tv_day_1.setText(global.getDay(f.list.get(0).dt));
                tv_day_2.setText(global.getDay(f.list.get(1).dt));
                tv_day_3.setText(global.getDay(f.list.get(2).dt));
                tv_day_4.setText(global.getDay(f.list.get(3).dt));
                tv_day_5.setText(global.getDay(f.list.get(4).dt));
            }else{
                new LoadJson().execute("");
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Failed read data", Toast.LENGTH_SHORT).show();
        }

    }

    public class LoadJson extends AsyncTask<String, String, String> {
        com.simple.weather.json.JSONParser jsonParser = new com.simple.weather.json.JSONParser();
        String jsonWeather = null,
                jsonForecast = null,
                status = "null";
        Gson gson = new Gson();

        @Override
        protected void onPreExecute() {
            mSwipeRefreshLayout.setRefreshing(true);
            //gps			= new GPSTracker(ActivityMain.this);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                isOnexecute = true;
                Thread.sleep(50);
                if (cd.isConnectingToInternet()) {
                    List<NameValuePair> param = new ArrayList<NameValuePair>();

                    String url_weather = Constant.getURLweather(global.getStringPref(Constant.S_KEY_CURRENT_ID, global.getDefaultCity()));
                    String url_forecast = Constant.getURLforecast(global.getStringPref(Constant.S_KEY_CURRENT_ID, global.getDefaultCity()));

                    JSONObject json_weather = jsonParser.makeHttpRequest(url_weather, "POST", param);
                    JSONObject json_forecast = jsonParser.makeHttpRequest(url_forecast, "POST", param);

                    jsonWeather = json_weather.toString();
                    jsonForecast = json_forecast.toString();
                    status = "success";
                } else {
                    status = "offline";
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (status == "success") {
                isOnexecute = false;
                WeatherResponse weather = gson.fromJson(jsonWeather, WeatherResponse.class);
                ForecastResponse forecast = gson.fromJson(jsonForecast, ForecastResponse.class);
                Log.d("jsonForecast", jsonForecast);
                displayData(weather, forecast);
                try {
                    ItemLocation itemloc = new ItemLocation();
                    itemloc.setId(weather.id + "");
                    itemloc.setName(weather.name);
                    itemloc.setCode(weather.sys.country);
                    itemloc.setJsonWeather(jsonWeather);
                    itemloc.setJsonForecast(jsonForecast);
                    global.saveLocation(itemloc);
                    global.setStringPref(Constant.S_KEY_CURRENT_ID, itemloc.getId());
                    refreshList();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed convert data", Toast.LENGTH_SHORT).show();
                }
                Toast.makeText(getApplicationContext(), "Weather updated", Toast.LENGTH_SHORT).show();
            } else if (status == "offline") {
                Toast.makeText(getApplicationContext(), "Internet is offline", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed retrive data", Toast.LENGTH_SHORT).show();
            }
            mSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }

    }


    protected void dialogAddLocation() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_ACTION_BAR); // before
        dialog.setContentView(R.layout.dialog_add_location);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        final Button button_no = (Button) dialog.findViewById(R.id.button_no);
        final Button button_yes = (Button) dialog.findViewById(R.id.button_yes);
        final TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
        final LinearLayout lyt_form = (LinearLayout) dialog.findViewById(R.id.lyt_form);
        final LinearLayout lyt_progress = (LinearLayout) dialog.findViewById(R.id.lyt_progress);
        final AutoCompleteTextView address = (AutoCompleteTextView) dialog.findViewById(R.id.address);
        address.setAdapter(new PlacesAutoCompleteAdapter(MainActivity.this, android.R.layout.simple_dropdown_item_1line));
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!address.getText().toString().trim().equals("")) {
                    if (cd.isConnectingToInternet()) {
                      JSONLoader jsload = new JSONLoader(MainActivity.this, lyt_form, lyt_progress, tv_message, dialog);
                        jsload.execute(address.getText().toString());
                    } else {
                        tv_message.setText("Internet is offline");
                    }
                } else {
                    tv_message.setText("Please fill location");
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


    private void buttonAction() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!isOnexecute) {
                    new LoadJson().execute("");
                } else {
                    Toast.makeText(getApplicationContext(), "Current task still running", Toast.LENGTH_SHORT).show();
                }
            }
        });

        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                toggle();
            }
        });
        bt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(buttonClick);
                Intent i = new Intent(getApplicationContext(), SettingActivity.class);
                startActivity(i);
                finish();
            }
        });

        bt_addlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAddLocation();
            }
        });
    }

    //define all ui component
    private void initComponent() {
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        scroll_view = (ScrollView) findViewById(R.id.scroll_view);
        tv_addres = (TextView) findViewById(R.id.tv_addres);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_temp = (TextView) findViewById(R.id.tv_temp);
        tv_pressure = (TextView) findViewById(R.id.tv_pressure);
        tv_humidity = (TextView) findViewById(R.id.tv_humidity);
        tv_wind = (TextView) findViewById(R.id.tv_wind);
        img_weather = (ImageView) findViewById(R.id.img_weather);
        tv_sunset = (TextView) findViewById(R.id.tv_sunset);
        tv_sunrise = (TextView) findViewById(R.id.tv_sunrise);
        bt_location = (Button) findViewById(R.id.bt_location);

        tv_temp_[0] = (TextView) findViewById(R.id.tv_temp_1);
        tv_temp_[1] = (TextView) findViewById(R.id.tv_temp_2);
        tv_temp_[2] = (TextView) findViewById(R.id.tv_temp_3);
        tv_temp_[3] = (TextView) findViewById(R.id.tv_temp_4);
        tv_temp_[4] = (TextView) findViewById(R.id.tv_temp_5);

        img_small_[0] = (ImageView) findViewById(R.id.img_small_1);
        img_small_[1] = (ImageView) findViewById(R.id.img_small_2);
        img_small_[2] = (ImageView) findViewById(R.id.img_small_3);
        img_small_[3] = (ImageView) findViewById(R.id.img_small_4);
        img_small_[4] = (ImageView) findViewById(R.id.img_small_5);

        tv_day_1 = (TextView) findViewById(R.id.tv_day_1);
        tv_day_2 = (TextView) findViewById(R.id.tv_day_2);
        tv_day_3 = (TextView) findViewById(R.id.tv_day_3);
        tv_day_4 = (TextView) findViewById(R.id.tv_day_4);
        tv_day_5 = (TextView) findViewById(R.id.tv_day_5);
        lyt_main = (LinearLayout) findViewById(R.id.lyt_main);
        tv_description = (TextView) findViewById(R.id.tv_description);
        bt_about = (Button) findViewById(R.id.bt_about);
        bt_addlocation = (Button) findViewById(R.id.bt_addlocation);
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            Toast.makeText(getApplicationContext(), "Internet is offline", Toast.LENGTH_SHORT).show();
        }
        listview_location = (ListView) findViewById(R.id.listview_location);
        refreshList();
        if (!global.getStringPref(Constant.S_KEY_CURRENT_ID, "null").equals("null")) {
            Gson gson = new Gson();
            ItemLocation itemloc = global.getLocation(global.getStringPref(Constant.S_KEY_CURRENT_ID, "null"));
            WeatherResponse weather = gson.fromJson(itemloc.getJsonWeather(), WeatherResponse.class);
            ForecastResponse forecast = gson.fromJson(itemloc.getJsonForecast(), ForecastResponse.class);
            displayData(weather, forecast);
        } else {
            new LoadJson().execute("");
        }
    }

    public void refreshList() {
        ArrayList<ItemLocation> itemsloc = new ArrayList<ItemLocation>();
        for (int i = 0; i < global.getListCode().size(); i++) {
            itemsloc.add(global.getLocation(global.getListCode().get(i)));
        }
        listview_location.setAdapter(new ItemLocationAdapter(MainActivity.this, itemsloc));
    }
}