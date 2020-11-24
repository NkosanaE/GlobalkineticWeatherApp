package com.nkosana.globalkineticweatherapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nkosana.globalkineticweatherapp.MainActivity;
import com.nkosana.globalkineticweatherapp.R;
import com.nkosana.globalkineticweatherapp.data.Constant;
import com.nkosana.globalkineticweatherapp.data.DatabaseManager;
import com.nkosana.globalkineticweatherapp.data.GlobalVariable;
import com.nkosana.globalkineticweatherapp.model.ForecastResponse;
import com.nkosana.globalkineticweatherapp.model.ItemLocation;
import com.nkosana.globalkineticweatherapp.model.WeatherResponse;

import java.util.ArrayList;

public class ItemLocationAdapter extends BaseAdapter {


    private static ArrayList<ItemLocation> itemDetailsrrayList;
    Context contex;
    MainActivity act;
    private LayoutInflater l_Inflater;
    GlobalVariable global;
    DatabaseManager db;

    public ItemLocationAdapter(MainActivity act, ArrayList<ItemLocation> results) {
        this.contex = act.getApplicationContext();
        this.act = act;
        itemDetailsrrayList = results;
        l_Inflater = LayoutInflater.from(act);
        global = (GlobalVariable) act.getApplication();
        db = new DatabaseManager(act);
    }

    public int getCount() {
        return itemDetailsrrayList.size();
    }

    public ItemLocation getItem(int position) {
        return itemDetailsrrayList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = l_Inflater.inflate(R.layout.item_list_location, null);
            holder = new ViewHolder();
            holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tv_condition = (TextView) convertView.findViewById(R.id.tv_condition);
            holder.tv_temp = (TextView) convertView.findViewById(R.id.tv_temp);
            holder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);
            holder.bg_item = (LinearLayout) convertView.findViewById(R.id.bg_item);
            holder.img_icon = (ImageView) convertView.findViewById(R.id.img_icon);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Gson gson = new Gson();

        try {
            final WeatherResponse weather = gson.fromJson(itemDetailsrrayList.get(position).getJsonWeather(), WeatherResponse.class);
            final ForecastResponse forecast = gson.fromJson(itemDetailsrrayList.get(position).getJsonForecast(), ForecastResponse.class);
            holder.tv_address.setText(itemDetailsrrayList.get(position).getName() + ", " + itemDetailsrrayList.get(position).getCode());
            holder.tv_condition.setText(weather.weather.get(0).main);
            global.setDrawableSmallIcon(weather.weather.get(0).icon, holder.img_icon);
            holder.tv_temp.setText(global.getTemp(weather.main.temp));
            GradientDrawable gd = ((GradientDrawable) holder.bg_item.getBackground());
            global.setLytColorDrw(weather.weather.get(0).icon, gd);
            holder.bg_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    global.setStringPref(Constant.S_KEY_CURRENT_ID, itemDetailsrrayList.get(position).getId());
                    act.toggle();
                    act.displayData(weather, forecast);
                }
            });

        } catch (Exception e) {

        }

        holder.bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDeleteConfirmationFlat(itemDetailsrrayList.get(position).getId());
            }
        });


        return convertView;
    }


    static class ViewHolder {
        TextView tv_address;
        TextView tv_condition;
        TextView tv_temp;
        Button bt_delete;
        LinearLayout lyt;
        LinearLayout bg_item;
        ImageView img_icon;
    }


    protected void dialogDeleteConfirmationFlat(final String id) {
        final Dialog dialog = new Dialog(act);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_delete_confirm);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final Button button_no = (Button) dialog.findViewById(R.id.button_no);
        final Button button_yes = (Button) dialog.findViewById(R.id.button_yes);
        final TextView tv_message = (TextView) dialog.findViewById(R.id.tv_message);
        ItemLocation itemloc = global.getLocation(id);
        tv_message.setText("Are you sure delete location " + itemloc.getName() + ", " + itemloc.getCode() + "?");
        button_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        button_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getCount() == 1) {
                    tv_message.setText("Location cannot be empty");
                } else {
                    global.deleteLocation(id);
                    act.refreshList();
                    dialog.dismiss();
                    Toast.makeText(contex, "Delete success", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }
}
