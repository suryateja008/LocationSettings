package com.example.suryaduggi.locationsettings;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by suryaduggi on 1/24/15.
 */
public class WifiChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

         boolean there=false;
         int ans=0;

        final WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        String wifiname =wifiInfo.getSSID().replace('"',' ').trim();
        SharedPreferences pref = context.getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        Map<String,?> map = pref.getAll();
        for(Object val : map.values()){

            if(val.toString().split("-").length==2){
                if(val.toString().split("-")[0].equals(wifiname)){
                    there=true;
                    ans=Integer.parseInt(val.toString().split("-")[1]);
                    break;
                }
            }
        }

        if(there){
            AudioManager a = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
             a.setRingerMode(ans);
             Toast.makeText(context,"MODE IS SET",Toast.LENGTH_LONG).show();
        }
    }



}
