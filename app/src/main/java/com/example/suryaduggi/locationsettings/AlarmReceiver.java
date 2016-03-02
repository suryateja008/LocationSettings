package com.example.suryaduggi.locationsettings;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by suryaduggi on 2/6/15.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent i) {

        SharedPreferences pref = context.getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        final WifiManager wifiManager = (WifiManager)context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo=wifiManager.getConnectionInfo();
        String wifiname =wifiInfo.getSSID().replace('"',' ').trim();
        boolean set=getPrefName(i.getStringExtra("title"),context);

        // check if uid is similar to the uid of the intent and title exists in the
        if((wifiname.equals(i.getStringExtra("wifiName"))|| i.getBooleanExtra("wifiSpecify",true)) && set && getUniqueId(i.getStringExtra("title"),context)==i.getIntExtra("uid",0)) {


            AudioManager a = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            a.setRingerMode(i.getIntExtra("mode",0));

        }


        if(i.getBooleanExtra("repeat",false)){

            //set alarm again  if repeat is true for the particular PendingIntent
            setAlarm(i.getStringExtra("wifiName"),i.getIntExtra("day",0),i.getIntExtra("hour",0),i.getIntExtra("min",0),i.getStringExtra("AP"),i.getIntExtra("mode",0),i.getBooleanExtra("repeat",false),context,i.getIntExtra("uid",-1),i.getStringExtra("title"),i.getBooleanExtra("wifiSpecify",false));

        }else if(i.getIntExtra("uid",-1) == getUniqueId(i.getStringExtra("title"),context)){

          // Toast.makeText(context,i.getIntExtra("uid",-1)+"----"+getUniqueId(i.getStringExtra("title"),context),Toast.LENGTH_LONG).show();
            pref.edit().remove(i.getStringExtra("title")).commit();

        }
   }

    public void setAlarm(String wiName,int day,int h,int m,String AP,int mode,boolean repeat,Context context,int uId,String title,boolean wifiSpec){
          boolean isNextWeek=false;
        AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
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

       // Log.d("ALARMD",h+":"+m+AP+"----- store");



        // If current day is already done then set it for next coming day
        if (timeOff.before(Calendar.getInstance())) {

            timeOff.add(Calendar.WEEK_OF_YEAR, 1);
            isNextWeek=true;
            Log.d("ALARMD","next coming week");
        }

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("wifiName",wiName);
        intent.putExtra("day",day);
        intent.putExtra("hour",h);
        intent.putExtra("min",m);
        intent.putExtra("AP",AP);
        intent.putExtra("mode",mode);
        intent.putExtra("repeat",repeat);
        intent.putExtra("uid",uId);
        intent.putExtra("title",title);
        intent.putExtra("wifiSpecify",wifiSpec);

        PendingIntent pi = PendingIntent.getBroadcast(context,uId, intent,PendingIntent.FLAG_CANCEL_CURRENT) ;

        if(getPrefName(title,context) && getUniqueId(intent.getStringExtra("title"),context)==intent.getIntExtra("uid",0)){
            if(isNextWeek){
                alarm.set(AlarmManager.RTC_WAKEUP, (timeOff.getTimeInMillis()+(60*60*1000)), pi);
                //Log.d("ALARMD",((timeOff.getTimeInMillis()-System.currentTimeMillis()+(60*60*1000))/(1000*60))+"-min-"+((7*uniqueId)+day));
            }else{
                alarm.set(AlarmManager.RTC_WAKEUP, (timeOff.getTimeInMillis()), pi);
                //Log.d("ALARMD",((timeOff.getTimeInMillis()-System.currentTimeMillis())/(1000*60))+"-min-"+((7*uniqueId)+day));
            }
             }else
        {alarm.cancel(pi);}




    }

    public boolean getPrefName(String titleName,Context c){
        SharedPreferences pref = c.getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        if(pref.contains(titleName))
            return true;
        return false;
    }

    public int getUniqueId(String titleName,Context c){
        SharedPreferences pref = c.getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        Log.d("ALARMD",titleName +"--GET UID TITLE");
        if(pref.contains(titleName)){
            Log.d("ALARMD",(Integer.parseInt(pref.getString(titleName,"NONE").split("-")[7])*7+5)+"-- UID IN STORE");
            return Integer.parseInt(pref.getString(titleName,"NONE").split("-")[7]);
        }

        return -1;
    }

}
