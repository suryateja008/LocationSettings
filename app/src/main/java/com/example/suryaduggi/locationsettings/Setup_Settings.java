package com.example.suryaduggi.locationsettings;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;


public class Setup_Settings extends Activity {

    String wifiname;
    EditText et;
    TextView tv;
    SeekBar seek;
    TextView seektv;
    RadioGroup radioGroup;
    RadioButton normalRB,silentRB,vibrationRB;
    Button done;
    String Days,time,repeat;
    int hour,min,ringMode;
    Boolean Checkboolean,Timepickerboolean=false,ringRadioboolean=false,activateCheckboolean,wifispec=true;
    CheckBox repeatCheckBox,activateCheckBox;
    CheckBox[] BoxList;
    TimePicker TP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup__settings);

        

        // Get view for all elements
        CheckBox[] temp={(CheckBox)findViewById(R.id.SunCheckBox),(CheckBox)findViewById(R.id.MonCheckBox),(CheckBox)findViewById(R.id.TueCheckBox),(CheckBox)findViewById(R.id.WedCheckBox),
                (CheckBox)findViewById(R.id.ThuCheckBox),(CheckBox)findViewById(R.id.FriCheckBox),(CheckBox)findViewById(R.id.SatCheckBox)};
        BoxList=temp;
        temp=null;
        et= (EditText) findViewById(R.id.titleEditText);
        TP= (TimePicker) findViewById(R.id.timePicker);
        repeatCheckBox = (CheckBox) findViewById(R.id.RepeatcheckBox);
        activateCheckBox= (CheckBox) findViewById(R.id.ActiveSMcheckBox);
        tv = (TextView) findViewById(R.id.wifiNametextView);
        seek = (SeekBar) findViewById(R.id.seekBar);
        seektv = (TextView) findViewById(R.id.SeektextView);
        normalRB = (RadioButton) findViewById(R.id.radioButtonNormal);
        silentRB = (RadioButton) findViewById(R.id.radioButtonSilent);
        vibrationRB = (RadioButton) findViewById(R.id.radioButtonVibrate);
        radioGroup = (RadioGroup) findViewById(R.id.RadioGroup);
        wifiname = getIntent().getStringExtra("wifiName");
        done = (Button) findViewById(R.id.doneButton);



        //Check if it already contains
        if(getPrefName(wifiname)){
            SharedPreferences pref = getApplicationContext().getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
           // Toast.makeText(Setup_Settings.this,"Edit Settings",Toast.LENGTH_LONG).show();
            String[] Checkdata = pref.getString(wifiname,"NONE").split("-");
          if(Checkdata.length==2) {
              int value = Integer.parseInt(Checkdata[1]);
              tv.setText(Checkdata[0]);
              et.setText(wifiname);
              wifiname=Checkdata[0];
              et.setKeyListener(null);

              if (value == 0) {
                  silentRB.setChecked(true);
              } else if (value == 1) {
                  vibrationRB.setChecked(true);
              } else if (value == 2) {
                  normalRB.setChecked(true);
              }
          }else if(Checkdata.length>7){
              //String wiName,int day,int h,int m,String AP,int mode,boolean repeat,int uniqueId
              int value = Integer.parseInt(Checkdata[5]);
              tv.setText(Checkdata[0]);
              et.setText(wifiname);
              wifiname=Checkdata[0];
              et.setKeyListener(null);


              if (value == 0) {
                  silentRB.setChecked(true);
              } else if (value == 1) {
                  vibrationRB.setChecked(true);
              } else if (value == 2) {
                  normalRB.setChecked(true);
              }

              activateCheckBox.setChecked(true);


              if(Checkdata[4].trim().equals("PM")){
              TP.setCurrentHour(Integer.parseInt(Checkdata[2])+12);
              }else{
                  if(Integer.parseInt(Checkdata[2])==12){
                      TP.setCurrentHour(Integer.parseInt(Checkdata[2])-12);
                  }else{
                      TP.setCurrentHour(Integer.parseInt(Checkdata[2]));
                  }

              }
              TP.setCurrentMinute(Integer.parseInt(Checkdata[3]));

             repeatCheckBox.setChecked(Boolean.parseBoolean(Checkdata[6]));

              for(String day: Checkdata[1].split(""))
                  if(!day.equals("")){
                      BoxList[Integer.parseInt(day)-1].setChecked(true);
                  }


          }
        }

        // Set the textview to current wifi settings
        tv.setText(wifiname);


        // Set textview when scroll bar is moved or changed
        seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                SettingBrightness(((float)progress)/(float)100.00);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //Set the values when time picker values are changed



        //DOne button clicked event listener
        //Get values and check it they are set

          done.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                 // doneCheck();
                 StoreValues();

              }
          });

        TP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {

//                if(AP.trim().equals("AM")){
//                    timeOff.set(Calendar.AM_PM, Calendar.AM);
//                }else{timeOff.set(Calendar.AM_PM, Calendar.PM);}



                if (hourOfDay > 12) {
                    hourOfDay = hourOfDay - 12;
                    time = "PM";
                } else if (hourOfDay < 12 && hourOfDay != 0) {
                    time = "AM";
                } else if (hourOfDay == 12) {
                    time = "PM";
                } else if (hourOfDay == 0) {
                    hourOfDay = 12;
                    time = "AM";
                }
                hour = hourOfDay;min=minute;
                Timepickerboolean = true;

//                Log.d("ALARMD",hour+":"+minute+time+"----- Time change listener");
            }
        });

        // Check for wifiSPecific

        if(tv.getText().toString().trim().equals("Scheduler")){
            wifispec=false;
            activateCheckBox.setChecked(true);
            activateCheckBox.setVisibility(View.INVISIBLE);
        }else{
            wifispec=true;

        }

    }



    public void CheckBox(){
        Days="";
        Checkboolean=false;

       // TP.
        for(int i=0;i<7;i++){
           if(BoxList[i].isChecked()){
               Checkboolean=true;
               Days+=i+1;
           }
        }
        Days+="-"+hour+"-"+min+"-"+time;

        //Check active check box

          if(activateCheckBox.isChecked()){
              activateCheckboolean=true;
          }else{
              activateCheckboolean=false;
          }

        if(repeatCheckBox.isChecked()){
             repeat="true";
        }else{
            repeat="false";
        }


    }

    public void RingCheck(){

        //Check which radio button is selected
        if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonNormal){
            ringMode=2;
            ringRadioboolean=true;
        }else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonSilent){
            ringRadioboolean=true;
            ringMode=0;
        }else if(radioGroup.getCheckedRadioButtonId() == R.id.radioButtonVibrate){
            ringRadioboolean=true;
            ringMode=1;
        }
    }

    public void StoreValues(){

         if(getIntent().getBooleanExtra("edit",false)){

             SharedPreferences pref = getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
             pref.edit().remove(et.getText().toString()).commit();
         }

        RingCheck();
        CheckBox();
        if(et.getText().toString().equals("")){
            Toast.makeText(Setup_Settings.this,"Please Enter title",Toast.LENGTH_SHORT).show();
        }else if(!ringRadioboolean){
            Toast.makeText(Setup_Settings.this,"Please Choose Ring Mode",Toast.LENGTH_SHORT).show();
        }else if(activateCheckboolean && (!Checkboolean || !Timepickerboolean)){
             if(!Checkboolean)
                 Toast.makeText(Setup_Settings.this,"Please Check Days of Week",Toast.LENGTH_SHORT).show();
            if(!Timepickerboolean)
                Toast.makeText(Setup_Settings.this,"Please Select Time",Toast.LENGTH_SHORT).show();
        }else{
            //store stores data in string so that it is simple to extract.
            String store="";

             if(!activateCheckboolean && ringRadioboolean){
                 store=wifiname+"-"+ringMode;
                 storePref(et.getText().toString(),store);
                 finish();
             }else if(activateCheckboolean && ringRadioboolean){

                 boolean wifis;
                 if(wifispec){
                       wifis = false;
                 }else{
                     wifis = true;
                 }



                  store = wifiname+"-"+Days+"-"+ringMode+"-"+repeat+"-"+getUniqueId()+"-"+wifis;
                  String[] data= store.split("-");
                 Log.d("DTEST",data[4]+" off store");
                    for(String day: data[1].split(""))
                        if(!day.equals("")) {
                            Log.d("DTIME",Boolean.parseBoolean(data[8])+"");
                            setAlarm(data[0], Integer.parseInt(day), Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4], Integer.parseInt(data[5]), Boolean.parseBoolean(data[6]), Integer.parseInt(data[7]));
                        }
//                       storePref(et.getText().toString(),store);
//                       finish();

             }


        }

    }

    public void SettingBrightness(float number){
        seektv.setText((int)(number*255)+"");
        android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE, android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        android.provider.Settings.System.putInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS, ((int)(number*255)));
    }

    public void storePref(String wifiName,String Data){
        SharedPreferences pref = getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(wifiName, Data);
        editor.commit();

    }

    public int getUniqueId(){

        SharedPreferences pref = getSharedPreferences("WifiNamesUniqueId", Context.MODE_PRIVATE);
        int Uid = pref.getInt("UniqueID",-1);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt("UniqueID", (Uid + 1));
        editor.commit();
        return Uid;
    }

    public boolean getPrefName(String wifiName){

        SharedPreferences pref = getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        if(pref.contains(wifiName))
            return true;
        return false;

    }


    public void setAlarm(String wiName,int day,int h,int m,String AP,int mode,boolean repeat,int uniqueId){

        boolean isNextWeek=false;
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
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

        Log.d("ALARMD",h+":"+m+AP+"----- store");



        // If current day is already done then set it for next coming day
        if (timeOff.before(Calendar.getInstance())) {

            timeOff.add(Calendar.WEEK_OF_YEAR, 1);
            isNextWeek=true;
            Log.d("ALARMD","next coming week");
        }

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("wifiName",wiName);
        intent.putExtra("day",day);
        intent.putExtra("hour",h);
        intent.putExtra("min",m);
        intent.putExtra("AP",AP);
        intent.putExtra("mode",mode);
        intent.putExtra("repeat",repeat);
        intent.putExtra("uid",uniqueId);

        if(wifispec){
            intent.putExtra("wifiSpecify",false);
        }else{
            intent.putExtra("wifiSpecify",true);
        }

        intent.putExtra("title",et.getText().toString());
        PendingIntent pi = PendingIntent.getBroadcast(this,((7*uniqueId)+day), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if(isNextWeek){
           alarm.set(AlarmManager.RTC_WAKEUP, (timeOff.getTimeInMillis()), pi);
         // Log.d("ALARMD",((timeOff.getTimeInMillis()-System.currentTimeMillis())/(1000*60))+"-min-"+((7*uniqueId)+day));
        }else{
            alarm.set(AlarmManager.RTC_WAKEUP, (timeOff.getTimeInMillis()), pi);
            //Log.d("ALARMD",((timeOff.getTimeInMillis()-System.currentTimeMillis())/(1000*60))+"-min-"+((7*uniqueId)+day));
        }





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_setup__settings, menu);
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
