package com.example.suryaduggi.locationsettings;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.util.Log;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by suryaduggi on 2/22/15.
 */

public class onBoot extends BroadcastReceiver {
     Context con;
     SharedPreferences pref;
     String title="";

    @Override
    public void onReceive(Context context, Intent intent) {
        con = context;

        pref = context.getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        Map<String,?> map = pref.getAll();

        for(String ss : map.keySet()){
            title = ss;
            String[] data= map.get(ss).toString().split("-");


              if(data.length>5) {
                  for (String day : data[1].split(""))
                      if (!day.equals("")){
                          Log.d("DTIME",Boolean.parseBoolean(data[8])+"--- onBoot");
                          setAlarm(data[0], Integer.parseInt(day), Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4], Integer.parseInt(data[5]), Boolean.parseBoolean(data[6]), Integer.parseInt(data[7]),Boolean.parseBoolean(data[8]));
                     }
              }


        }


    }


    public void setAlarm(String wiName,int day,int h,int m,String AP,int mode,boolean repeat,int uniqueId,boolean wifiS){
        boolean isNextWeek=false;
        AlarmManager alarm = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);
        Calendar timeOff = Calendar.getInstance();
        timeOff.setTimeInMillis(System.currentTimeMillis());
        timeOff.set(Calendar.DAY_OF_WEEK,day);

        if(AP.trim().equals("AM")){
            //timeOff.set(Calendar.AM_PM,Calendar.AM);
            if(h==12){
                timeOff.set(Calendar.HOUR_OF_DAY, h-12);
                Log.d("ALARMD",h-12+":"+m+AP+"----- store");
            }else{
                timeOff.set(Calendar.HOUR_OF_DAY, h);
                Log.d("ALARMD",h+":"+m+AP+"----- store");
            }
        }
        else
        {
            //timeOff.set(Calendar.AM_PM,Calendar.PM);
            if(h==12){
                timeOff.set(Calendar.HOUR_OF_DAY, h);
                Log.d("ALARMD",h+":"+m+AP+"----- store");
            }else{
                timeOff.set(Calendar.HOUR_OF_DAY, h+12);
                Log.d("ALARMD",h+12+":"+m+AP+"----- store");
            }
        }

        timeOff.set(Calendar.MINUTE, m);
        timeOff.set(Calendar.SECOND,00);



        if (timeOff.before(Calendar.getInstance())) {

            timeOff.add(Calendar.WEEK_OF_YEAR, 1);
            isNextWeek=true;
           // Log.d("ALARMD","next coming week");
        }

        Intent intent = new Intent(con, AlarmReceiver.class);
        intent.putExtra("wifiName",wiName);
        intent.putExtra("day",day);
        intent.putExtra("hour",h);
        intent.putExtra("min",m);
        intent.putExtra("AP",AP);
        intent.putExtra("mode",mode);
        intent.putExtra("repeat",repeat);
        intent.putExtra("uid",uniqueId);
        intent.putExtra("title",title);
        intent.putExtra("wifiSpecify",wifiS);

         //Log.d("SHOWD",(timeOff.getTimeInMillis()-System.currentTimeMillis())/(1000*60)+"-min-"+((7*uniqueId)+day));

       PendingIntent pi = PendingIntent.getBroadcast(con,((7*uniqueId)+day), intent, PendingIntent.FLAG_UPDATE_CURRENT );


        if(isNextWeek){
            alarm.set(AlarmManager.RTC_WAKEUP, (timeOff.getTimeInMillis()), pi);
            //Log.d("ALARMD",((timeOff.getTimeInMillis()-System.currentTimeMillis()+(60*60*1000))/(1000*60))+"-min-"+((7*uniqueId)+day));
        }else{
            alarm.set(AlarmManager.RTC_WAKEUP, (timeOff.getTimeInMillis()), pi);
            //Log.d("ALARMD",((timeOff.getTimeInMillis()-System.currentTimeMillis())/(1000*60))+"-min-"+((7*uniqueId)+day));
        }





    }
}
