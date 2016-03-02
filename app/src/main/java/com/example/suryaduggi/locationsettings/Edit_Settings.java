package com.example.suryaduggi.locationsettings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class Edit_Settings extends Activity {
    String[] storage;
    ListView l;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__settings);
        l= (ListView) findViewById(R.id.listView);
        pref = getSharedPreferences("WifiNames", Context.MODE_PRIVATE);
        Map<String,?> map = pref.getAll();
        storage = new String[map.size()];
        int i=0;

        for(String ss : map.keySet()){
            storage[i++]=ss;
        }
        l.setAdapter(new ArrayAdapter<String>(Edit_Settings.this,R.layout.list_view,storage));

        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Stores wifi name in sharedPreference
                Intent i = new Intent(Edit_Settings.this,Setup_Settings.class);
                i.putExtra("wifiName",storage[position]);
                i.putExtra("edit",true);
                startActivity(i);
            }
        });
        l.setLongClickable(true);
        registerForContextMenu(l);

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
       // remove(storage[item.getItemId()]).commit()
        if(item.getItemId()!=-1){
          pref.edit().remove(storage[item.getItemId()]).commit();
        Toast.makeText(Edit_Settings.this,storage[item.getItemId()]+" Deleted",Toast.LENGTH_SHORT).show();
            finish();
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo aInfo = (AdapterView.AdapterContextMenuInfo) menuInfo;

//        Toast.makeText(Edit_Settings.this,"cool man"+aInfo.position,Toast.LENGTH_SHORT).show();
        menu.setHeaderTitle("Delete" );
        menu.add(1, aInfo.position, 2, "Yes");
        menu.add(1, -1, 2, "No");


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit__settings, menu);
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
