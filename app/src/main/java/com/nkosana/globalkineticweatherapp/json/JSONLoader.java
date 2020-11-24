package com.nkosana.globalkineticweatherapp.json;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nkosana.globalkineticweatherapp.MainActivity;
import com.nkosana.globalkineticweatherapp.data.Constant;
import com.nkosana.globalkineticweatherapp.data.DatabaseManager;
import com.nkosana.globalkineticweatherapp.data.GlobalVariable;
import com.nkosana.globalkineticweatherapp.model.City;
import com.nkosana.globalkineticweatherapp.model.ItemLocation;


public class JSONLoader extends AsyncTask<String, String, ItemLocation>{
	private com.simple.weather.json.JSONParser jsonParser = new com.simple.weather.json.JSONParser();
	private String jsonWeather = null, 
			jsonForecast= null, 
			status="null";
	
	private Context ctx;
	private LinearLayout lyt_form;
	private LinearLayout lyt_progress;
	private TextView tv_message; 
	private Dialog dialog;
	private DatabaseManager db;
	private GlobalVariable global;
	private MainActivity act;
	
	public JSONLoader(MainActivity act, LinearLayout lyt_form, LinearLayout lyt_progress, TextView tv_message, Dialog dialog) {
		this.act=act;
		this.ctx=act.getApplicationContext();
		this.lyt_form=lyt_form;
		this.lyt_progress=lyt_progress;
		this.tv_message=tv_message;
		this.dialog=dialog;
		global 	= (GlobalVariable) act.getApplication();
		db = new DatabaseManager(act);
	}


	@Override
	protected void onPreExecute() {
		lyt_form.setVisibility(View.GONE);
		lyt_progress.setVisibility(View.VISIBLE);
		super.onPreExecute();
	}
	
	@Override
	protected ItemLocation doInBackground(String... params) {
		ItemLocation itemLocation 	= new ItemLocation();

		try {
			Thread.sleep(50);
			List<NameValuePair> param = new ArrayList<NameValuePair>();
			City city = db.getWordsFormAutocomplate(params[0]);
			if(city!=null){
				itemLocation.setId(city.getId());
				itemLocation.setName(city.getName());
				itemLocation.setCode(city.getCode());
				
				String url_weather 		= Constant.getURLweather(city.getId());
				String url_forecast 	= Constant.getURLforecast(city.getId());
				
				JSONObject json_weather 	= jsonParser.makeHttpRequest(url_weather,"POST", param);
				JSONObject json_forecast 	= jsonParser.makeHttpRequest(url_forecast,"POST", param);
				
				jsonWeather 	= json_weather.toString();
				jsonForecast	= json_forecast.toString();
			
				itemLocation.setJsonWeather(jsonWeather);
				itemLocation.setJsonForecast(jsonForecast);
				
				status="success";
			}else{
				status="Invalid city name";
			}
			
		} catch (Exception e) {
			status = e.getMessage();
			e.printStackTrace();
		}
		
		return itemLocation;
	}
	
	protected void onPostExecute(ItemLocation result) {
		lyt_form.setVisibility(View.VISIBLE);
		lyt_progress.setVisibility(View.GONE);
		if(status.equals("success")){
			global.saveLocation(result);
			act.refreshList();
			dialog.dismiss();
		}
		tv_message.setText(status);
		//Toast.makeText(ctx, status, Toast.LENGTH_LONG).show();
	};

}
