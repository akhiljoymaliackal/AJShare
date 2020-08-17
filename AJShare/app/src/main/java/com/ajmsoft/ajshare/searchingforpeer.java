package com.ajmsoft.ajshare;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.skyfishjy.library.RippleBackground;

import java.util.ArrayList;
import java.util.List;

public class searchingforpeer extends AppCompatActivity {

    RippleBackground rippleBackground;
    WifiManager wifiManager;
    LocationManager locationManager;

    WifiP2pManager wifiP2pManager;
    WifiP2pManager.Channel channel;
    BroadcastReceiver receiver;
    IntentFilter intentFilter;
    private List<WifiP2pDevice> peers = new ArrayList<>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceDetailsArray;

    boolean ripplestatus = false;
    boolean islinearlayoutintop = false;
    LinearLayout ripplelayout;

    public Thread wifistatusthread = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();
        setContentView(R.layout.activity_searchingforpeer);
        //start ripple animation
        initilization();
        //rippleBackground.startRippleAnimation();
        checkWifiStatus();


    }

    public void initilization() {
        rippleBackground = (RippleBackground) findViewById(R.id.content_searchingforpeer);
        wifiManager = MainActivity.fetchWifimanager();
        wifiP2pManager = MainActivity.fetchWifip2pManager();
        channel = MainActivity.fetchChannel();
        receiver = MainActivity.fetchReceiver();
        intentFilter=MainActivity.fetchIntentFilter();
        ripplelayout = findViewById(R.id.riplle_layout);

    }


    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList wifiP2pDeviceList) {
            if(!wifiP2pDeviceList.getDeviceList().equals(peers)){

                peers.clear();
                peers.addAll(wifiP2pDeviceList.getDeviceList());
                deviceNameArray = new String[wifiP2pDeviceList.getDeviceList().size()];
                deviceDetailsArray = new WifiP2pDevice[wifiP2pDeviceList.getDeviceList().size()];
                int index=0;
                for(WifiP2pDevice device: wifiP2pDeviceList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName;
                    deviceDetailsArray[index] = device;
                    index++;
                }
                displayavailableconnections(deviceNameArray);


            }
        }
    };

    public void displayavailableconnections(String[] deviceNameArray){
        if(!islinearlayoutintop){
            islinearlayoutintop=true;
            final float scale = getResources().getDisplayMetrics().density;

            LinearLayout.LayoutParams setpara = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,(int)scale*150);
            ripplelayout.setLayoutParams(setpara);
        }



    }

    public void checkWifiStatus() {
        wifistatusthread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    if (wifiManager.isWifiEnabled()) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                wifiP2pManager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
                                    @Override
                                    public void onSuccess() {

                                            rippleBackground.startRippleAnimation();

                                    }

                                    @Override
                                    public void onFailure(int i) {

                                        //if (ripplestatus)
                                            //rippleBackground.stopRippleAnimation();

                                    }
                                });

                            }
                        });
                        break;
                    }
                }
                stopthread(this);
          }
        };
        wifistatusthread.start();
    }


    private  synchronized void stopthread(Thread thread){
        if(thread!=null){
            thread.interrupt();
            thread=null;}
    }
}