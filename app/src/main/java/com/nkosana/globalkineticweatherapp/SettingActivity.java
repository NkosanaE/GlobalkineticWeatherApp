package com.nkosana.globalkineticweatherapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nkosana.globalkineticweatherapp.data.Constant;
import com.nkosana.globalkineticweatherapp.data.GlobalVariable;
import com.nkosana.globalkineticweatherapp.data.Utils;

public class SettingActivity extends AppCompatActivity {

    private LinearLayout lyt_unit, lyt_about;
    private GlobalVariable global;
    private TextView tv_unit, tv_refresh;
    String[] string_unit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        global = (GlobalVariable) getApplication();

        string_unit = getResources().getStringArray(R.array.string_unit);

        lyt_unit = (LinearLayout) findViewById(R.id.lyt_unit);
        lyt_about = (LinearLayout) findViewById(R.id.lyt_about);
        tv_unit = (TextView) findViewById(R.id.tv_unit);

        tv_unit.setText(string_unit[global.getIntPref(Constant.I_KEY_UNIT, 0)]);
        actionMenu();
        initToolbar();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setTitle(R.string.action_settings);
        Utils.systemBarLollipop(this);
    }

    private void actionMenu() {
        //Toast.makeText(getApplicationContext(), "OK", Toast.LENGTH_SHORT).show();
        lyt_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUnit();
            }

        });


        lyt_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogAbout();
            }
        });

    }

    protected void dialogAbout() {
        final Dialog dialog = new Dialog(SettingActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        final Button button_ok = (Button) dialog.findViewById(R.id.button_ok);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    AlertDialog unitDialog = null;

    public void dialogUnit() {

        // Creating and Building the Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Unit Temperature");
        builder.setSingleChoiceItems(string_unit, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        global.setIntPref(Constant.I_KEY_UNIT, 0);
                        break;
                    case 1:
                        global.setIntPref(Constant.I_KEY_UNIT, 1);
                        break;
                }
                unitDialog.dismiss();
                tv_unit.setText(string_unit[global.getIntPref(Constant.I_KEY_UNIT, 0)]);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                unitDialog.dismiss();
            }
        });
        unitDialog = builder.create();
        unitDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                finish();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
        super.onBackPressed();
    }

}