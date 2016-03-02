package com.example.suryaduggi.locationsettings;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
     Button addSettingButton;
    Button editSettingButton;
    Button ScheduleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSettingButton = (Button) findViewById(R.id.addSettingButton);
        editSettingButton = (Button) findViewById(R.id.editSettingButton);
        ScheduleButton = (Button) findViewById(R.id.scheduleButton);


        addSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Add_Settings.class);
                startActivity(i);
            }
        });

        editSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Edit_Settings.class);
                startActivity(i);
            }
        });



        ScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,Setup_Settings.class);
                i.putExtra("wifiName","Scheduler");
                startActivity(i);
            }
        });



        //create first time unique id count for shared preference
        SharedPreferences pref = getSharedPreferences("WifiNamesUniqueId", Context.MODE_PRIVATE);
         if(!pref.contains("UniqueID")){
             SharedPreferences.Editor editor = pref.edit();
             editor.putInt("UniqueID",1);
             editor.commit();
         }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
