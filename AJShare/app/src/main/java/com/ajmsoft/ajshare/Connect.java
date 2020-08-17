package com.ajmsoft.ajshare;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.skyfishjy.library.RippleBackground;

import java.io.File;

import static com.ajmsoft.ajshare.MainActivity.context;


public class Connect extends Fragment {

    /*private Uri fileUri;
    private String filePath;
    TextView textView; */
    static TextView displayfirstletter;
    static TextView diplayFullName;
    static TextView displayDeviceName;
    Button send,receive;
    public static View fragmentView;

    public static final int STORAGE_PERMISSION_CODE = 555;
    public static final int LOCATION_PERMISSION_CODE = 675;
    boolean storagePermission=false,locationPermission=false;

    Thread checkWifiLocationService=null;

    RippleBackground rippleBackground=null;

    boolean ripplerunning=false;

    public Connect(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(fragmentView!=null){
            return  fragmentView;
        }
        else {
            //return inflater.inflate(R.layout.connect_fragment, container, false);
            fragmentView = inflater.inflate(R.layout.connect_fragment, container, false);


        /*Button bt = fragmentView.findViewById(R.id.button);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                chooseFile.addCategory(Intent.CATEGORY_OPENABLE);*/
            // chooseFile.setType("*/*");
             /*   chooseFile = Intent.createChooser(chooseFile, "Choose a file");
                startActivityForResult(chooseFile, 1);
            }
        });
*/

            setDisplayameCharacters(fragmentView);
            setButtonClicks(fragmentView);

            return fragmentView;

        }
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (data != null) {

                    // Checking for selection multiple files or single.
                    if (data.getClipData() != null){

                        // Getting the length of data and logging up the logs using index
                        for (int index = 0; index < data.getClipData().getItemCount(); index++) {

                            // Getting the URIs of the selected files and logging them into logcat at debug level
                            Uri uri = data.getClipData().getItemAt(index).getUri();
                            Log.d("filesUri [" + uri + "] : ", String.valueOf(uri) );

                        }
                    }else{

                        // Getting the URI of the selected file and logging into logcat at debug level
                        Uri uri = data.getData();
                        Log.d("fileUri: ", String.valueOf(uri));
                    }
                }

                break;
        }
    }*/

    public  static void setDisplayameCharacters(View view){
        displayfirstletter = view.findViewById(R.id.first_letter_of_username);
        String[] name= MainActivity.fetchUserNameFromMain().split("\\s");
        if(name.length>1){
            char[] firstname= name[0].toCharArray();
            char[] lastname = name[1].toCharArray();
            String printval= Character.toString(firstname[0])+Character.toString(lastname[0]);
            displayfirstletter.setText(printval);
        }
        else{
            char[] usrname= MainActivity.fetchUserNameFromMain().toCharArray();
            String printval=Character.toString(usrname[0]);
            displayfirstletter.setText(printval);

        }
        diplayFullName = view.findViewById(R.id.UserFullname);
        displayDeviceName = view.findViewById(R.id.deviceName);
        diplayFullName.setText(MainActivity.fetchUserNameFromMain());

        BluetoothAdapter myDevice = BluetoothAdapter.getDefaultAdapter();
        displayDeviceName.setText(myDevice.getName());

    }

    public void setButtonClicks(View view){
        send = (Button)view.findViewById(R.id.sendbutton);
        rippleBackground=(RippleBackground)view.findViewById(R.id.content);
        send.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View view) {

                checkPermission( Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE);
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION,LOCATION_PERMISSION_CODE);

                if(storagePermission && locationPermission) {
                    createaNewDirectory();
                    if (!MainActivity.fetchWifimanager().isWifiEnabled()) {
                        Snackbar wifiSnackbar = Snackbar.make(view, "Turn on Wifi Before Sharing", Snackbar.LENGTH_INDEFINITE).setAction("Turn On", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            }
                        });
                        wifiSnackbar.setActionTextColor(Color.rgb(7, 134, 224));  // change the color



                    }

                    if (!MainActivity.fetchLocationmanager().isLocationEnabled()) {
                        Snackbar wifiSnackbar = Snackbar.make(view, "Turn on Location for finding nearby ", Snackbar.LENGTH_INDEFINITE).setAction("Turn On", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                            }
                        });
                        wifiSnackbar.setActionTextColor(Color.rgb(7, 134, 224));  // change the color

                        wifiSnackbar.show();

                    }

                    if(MainActivity.fetchWifimanager().isWifiEnabled() && MainActivity.fetchLocationmanager().isLocationEnabled()){

                        Intent intent = new Intent(Connect.this.getActivity(), searchingforpeer.class);
                        startActivity(intent);


                    }

                    //checkWifiLocationServices();




                }


            }
        });

    }

    public static void setNameinFirstLaunch(){

        setDisplayameCharacters(fragmentView);


    }
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.fetchActivity(), permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.fetchActivity(),
                    new String[] { permission },
                    requestCode);
        }
        else {
            if(requestCode==STORAGE_PERMISSION_CODE) {
                createaNewDirectory();
                storagePermission = true;
            }
            else if(requestCode==LOCATION_PERMISSION_CODE)
                locationPermission=true;

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createaNewDirectory();
                Toast.makeText(MainActivity.fetchContext(),
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
                storagePermission=true;

            }
            else {
                Toast.makeText(MainActivity.fetchContext(),
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
                storagePermission=false;
            }
        }

        if (requestCode == LOCATION_PERMISSION_CODE) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(MainActivity.fetchContext(),
                    "Location Permission Granted",
                    Toast.LENGTH_SHORT)
                    .show();
            locationPermission=true;
        }
        else {
            Toast.makeText(MainActivity.fetchContext(),
                    " Location Permission Denied",
                    Toast.LENGTH_SHORT)
                    .show();
            locationPermission=false;
        }
    }
    }

    public void createaNewDirectory(){

        File dir=  new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/AJShare");
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    public void checkWifiLocationServices(){

        checkWifiLocationService = new Thread(){
          @Override
          public void run(){


              while(true) {
                  if (MainActivity.fetchWifimanager().isWifiEnabled() && isLocationEnabled() && !ripplerunning){

                        runripple();

                  }
              }
          }
        };

        checkWifiLocationService.start();

    }

    public void runripple(){

        ripplerunning= true;
        rippleBackground.startRippleAnimation();

    }

    public boolean isLocationEnabled(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {

            LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            return lm.isLocationEnabled();
        } else {

            int mode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE,
                    Settings.Secure.LOCATION_MODE_OFF);
            return  (mode != Settings.Secure.LOCATION_MODE_OFF);

        }

    }







}
/*
 rippleBackground.stopRippleAnimation();
 app:rb_color [color def:@android:color/holo_blue_dark] --> Color of the ripple
app:rb_radius [dimension def:64dp ] --> Radius of the ripple
app:rb_duration [integer def:3000 ] --> Duration of one ripple animation (millisecond)
app:rb_rippleAmount [integer def:6] --> Max amount of ripples at one screen
app:rb_scale [interger def:6] --> Scale of ripple at the end of one animation cycle
app:rb_type [enum (fillRipple, strokeRipple) def:fillRipple] --> Filled circle or ring
app:rb_strokeWidth [dimension def:2dp] --> Stroke width of the ripple, ONLY work when rb_type="strokeRipple"*/
