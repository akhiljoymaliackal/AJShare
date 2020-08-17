package com.ajmsoft.ajshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;
    static WifiManager wifiManager;
    static LocationManager locationmanager;

    static String userName;
    public static Context context;
    public static  ContentResolver contentResolver;
    SharedPreferences.Editor editor=null;
    public static  Activity shareActivity;

    public static WifiP2pManager wifiP2pManager;
    public static WifiP2pManager.Channel channel;
    public static BroadcastReceiver receiver;
    public static IntentFilter intentFilter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initilization();
        tablayoutSetting();

        onFirstLaunch();
        userName = fetchuserName();


        

    }

    public  void initilization(){
        tabLayout=findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        sharedPreferences = getSharedPreferences("AJPrefs", MODE_PRIVATE);
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        locationmanager = (LocationManager)getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        context=this.getApplicationContext();
        contentResolver = getContentResolver();
        shareActivity = MainActivity.this;


        wifiP2pManager = (WifiP2pManager) getApplicationContext().getSystemService(WIFI_P2P_SERVICE);
        channel = wifiP2pManager.initialize(this, getMainLooper(), null);
        receiver = new WifiDirectBroadcastReceiver(wifiP2pManager, channel, this);
        intentFilter = new IntentFilter();
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    public void tablayoutSetting(){
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        MyAdapter adapter= new  MyAdapter(this,getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }

    public void onFirstLaunch(){
         editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("isFirstRun", true)) {
            editor.putBoolean("isFirstRun", false);
            editor.commit();

            //createaNewDirectory();
            alertaskusername();



        }
    }

    public void alertaskusername(){
        LayoutInflater li = LayoutInflater.from(this);
        final View fetchUserName = li.inflate(R.layout.ask_for_user_name, null);
        AlertDialog.Builder alertUsername = new AlertDialog.Builder(this);
        Button alert = (Button) fetchUserName.findViewById(R.id.buttonenteryourname);


        alertUsername.setView(fetchUserName);
        final AlertDialog alertDialog = alertUsername.create();
        alert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText getUserName = (EditText) fetchUserName.findViewById(R.id.resultenteryourname);
                String entereduserName = getUserName.getText().toString();
                userName = entereduserName;
                editor.putString("userName", entereduserName);
                editor.commit();
                alertDialog.cancel();
                Connect.setNameinFirstLaunch();
            }
        });
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }





    public String fetchuserName(){
        if (sharedPreferences.getString("userName", null) == null || sharedPreferences.getString("userName", null).equalsIgnoreCase("")) {
            return "Unknown User";
        }
        else return  sharedPreferences.getString("userName", null);
    }

    public static  String fetchUserNameFromMain(){

            return userName;

    }
    public static Context fetchContext(){
        return context;
    }

    public static ContentResolver fetchcontentreceiver(){
        return contentResolver;
    }


    public static WifiManager fetchWifimanager(){
        return wifiManager;
    }
    public static LocationManager fetchLocationmanager(){
        return locationmanager;
    }
    public  static Activity fetchActivity(){ return shareActivity; }
    public static BroadcastReceiver fetchReceiver(){return receiver;};
    public  static WifiP2pManager.Channel fetchChannel(){return channel;}
    public static  WifiP2pManager fetchWifip2pManager(){return  wifiP2pManager;}
    public static IntentFilter fetchIntentFilter(){return intentFilter;}



}



