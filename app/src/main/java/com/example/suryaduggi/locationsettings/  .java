package com.example.suryaduggi.locationsettings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;


public class Add_Settings extends Activity {
     String[] storage;
     ListView l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__settings);


        l= (ListView) findViewById(R.id.listView1);
         getWifiNetworksList();

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),storage[position],Toast.LENGTH_LONG).show();
            }
        });

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Stores wifi name in sharedPreference
                Intent i = new Intent(Add_Settings.this,Setup_Settings.class);
                i.putExtra("wifiName",storage[position]);
                startActivity(i);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add__settings, menu);
        return true;
    }




    private void getWifiNetworksList(){
        // Get list of wifiName in the network and put them into array
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        final WifiManager wifiManager =
                (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(new BroadcastReceiver(){

            @SuppressLint("UseValueOf") @Override
            public void onReceive(Context context, Intent intent) {
                WifiInfo wifiInfo=wifiManager.getConnectionInfo();
                List<ScanResult> scanList = wifiManager.getScanResults();
                storage = new String[scanList.size()];

                for(int i = 0; i < scanList.size(); i++){
                    if((scanList.get(i).SSID).trim().equals("")){
                        storage[i]="-------------------";
                    }else{
                        storage[i]=(scanList.get(i)).SSID;

                  }

                }
                l.setAdapter(new ArrayAdapter<String>(Add_Settings.this,R.layout.list_view,storage));
            }

        },filter);
        wifiManager.startScan();

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
