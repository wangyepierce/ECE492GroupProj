package com.example.smartlamp;

import android.content.Intent;

import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import java.io.UnsupportedEncodingException;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.SeekBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private static final int REQUEST_Time = 3;

    // Name of the connected device
    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;

    private static BluetoothSerialService mSerialService = null;

    private static InputMethodManager mInputManager;
    private boolean mEnablingBT;
    private boolean flagBT = false;

    //logcat tag
    public static final String LOG_TAG = "BlueTooth";
    // Message types sent from the BluetoothReadService Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;
    /**
     * Set to true to add debugging code and logging.
     */
    public static final boolean DEBUG = true;
    private MenuItem mMenuItemConnect;
    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // our main view
    private View homeView;
    //the item in homeactivity xml
    private ImageView lightOff;
    private ImageView lightOn;
    private SeekBar brightnessSeekBar;
    private SeekBar redSeekBar;
    private SeekBar greenSeekBar;
    private SeekBar blueSeekBar;
    private String[] time = {"0", "0", "0", "0"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //The input method manager  is expressed as the client-side API here
        // which exists in each application context and communicates with a global system service that manages the interaction across all processes.
        mInputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //set a bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            finishDialogNoBluetooth();
            return;
        }

        setContentView(R.layout.activity_home);
        initView();

        homeView = (View) findViewById(R.id.home);
        // initiate  views
        brightnessSeekBar =(SeekBar)findViewById(R.id.seekBar_bright);
        redSeekBar =(SeekBar)findViewById(R.id.seekBar_red);
        greenSeekBar =(SeekBar)findViewById(R.id.seekBar_green);
        blueSeekBar =(SeekBar)findViewById(R.id.seekBar_blue);

        //bluetooth serive
        mSerialService = new BluetoothSerialService(this, mHandlerBT, homeView);


        // perform seek bar change listener event used for getting the progress value
        brightnessSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                String str0 = String.valueOf(progressChangedValue);
                String str = "brightness:" + str0;
                Log.i("send light", str0);
                byte[] byteArr = new byte[0];
                try {
                    byteArr = str.getBytes("UTF-8");
                    send(byteArr);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
        });


        //actived only under connected via bluetooth
       // if(flagBT == true) {
            // perform seek bar change listener event used for getting the progress value
            redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChangedValue = 0;

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChangedValue = progress;
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    String str0 = String.valueOf(progressChangedValue);
                    String str = "red:" + str0;
                    Log.i("send red", str0);
                    byte[] byteArr = new byte[0];
                    try {
                        byteArr = str.getBytes("UTF-8");
                        send(byteArr);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });


            // perform seek bar change listener event used for getting the progress value
            greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChangedValue = 0;

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChangedValue = progress;
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    String str0 = String.valueOf(progressChangedValue);
                    String str = "green:" + str0;
                    Log.i("send green", str0);
                    byte[] byteArr = new byte[0];
                    try {
                        byteArr = str.getBytes("UTF-8");
                        send(byteArr);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });


            // perform seek bar change listener event used for getting the progress value
            blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                int progressChangedValue = 0;

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    progressChangedValue = progress;
                }

                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                public void onStopTrackingTouch(SeekBar seekBar) {
                    String str0 = String.valueOf(progressChangedValue);
                    String str = "blue:" + str0;
                    Log.i("send blue", str0);
                    byte[] byteArr = new byte[0];
                    try {
                        byteArr = str.getBytes("UTF-8");
                        send(byteArr);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            });
        //}
    }



    private void initView() {
        lightOff = findViewById(R.id.light_off);
        lightOn = findViewById(R.id.light_on);
        ImageView setTimeImg = findViewById(R.id.main_set_time);
        ImageView setBluetooth = findViewById(R.id.set_bluetooth);

        if (lightOff != null && lightOn != null && setTimeImg != null) {
            lightOff.setOnClickListener(this);
            lightOn.setOnClickListener(this);
            setTimeImg.setOnClickListener(this);
            setBluetooth.setOnClickListener(this);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //click the bluetooth image
            case R.id.set_bluetooth:{
                Log.i("bt", "btclick");

                if (flagBT == false) {
                    // Launch the DeviceListActivity to see devices and do scan
                    Intent serverIntent = new Intent(this, BT_DeviceList.class);
                    flagBT = true;
                    Log.i("flagBT", String.valueOf(flagBT));
                    startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                }
                else {
                    flagBT = false;
                    Log.i("flagBT", String.valueOf(flagBT));
                    mSerialService.stop();
                    mSerialService.start();

                }
            }
            break;
            //disable this image, and change image to light on; also send information via bluetooth
            case R.id.light_off:
                if(flagBT == true){
                    lightOff.setVisibility(View.INVISIBLE);
                    lightOn.setVisibility(View.VISIBLE);
                    String str = "off";
                    byte[] byteArr = new byte[0];
                    try {
                        byteArr = str.getBytes("UTF-8");
                        send(byteArr);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //disable this image, and change image to light off; also send information via bluetooth
            case R.id.light_on:
               if(flagBT == true){
                    lightOff.setVisibility(View.VISIBLE);
                    lightOn.setVisibility(View.INVISIBLE);
                    String str2 = "on";
                    byte[] byteArr2 = new byte[0];
                    try {
                        byteArr2 = str2.getBytes("UTF-8");
                        send(byteArr2);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                break;
            //click the time image and go to time activity
            case R.id.main_set_time: {
                Log.i("set", "settime");
                Intent intent = new Intent(HomeActivity.this, TimingActivity.class);
                startActivityForResult(intent, REQUEST_Time);
            }
            break;

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        if (DEBUG)
            Log.e(LOG_TAG, "++ ON START ++");

        mEnablingBT = false;
    }
    @Override
    public synchronized void onResume() {
        super.onResume();

        if (DEBUG) {
            Log.e(LOG_TAG, "+ ON RESUME +");
        }

        if (!mEnablingBT) { // If we are turning on the BT we cannot check if it's enable
            if ( (mBluetoothAdapter != null)  && (!mBluetoothAdapter.isEnabled()) ) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(R.string.alert_dialog_turn_on_bt)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.alert_dialog_warning_title)
                        .setCancelable( false )
                        .setPositiveButton(R.string.alert_dialog_yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mEnablingBT = true;
                                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                            }
                        })
                        .setNegativeButton(R.string.alert_dialog_no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //finishDialogNoBluetooth();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }

            if (mSerialService != null) {
                // Only if the state is STATE_NONE, do we know that we haven't started already
                if (mSerialService.getState() == BluetoothSerialService.STATE_NONE) {
                    // Start the Bluetooth chat services
                    mSerialService.start();
                }
            }

        }
    }
    @Override
    public synchronized void onPause() {
        super.onPause();
        if (DEBUG)
            Log.e(LOG_TAG, "- ON PAUSE -");

        if (homeView != null) {
            mInputManager.hideSoftInputFromWindow(homeView.getWindowToken(), 0);
            //homeView.onPause();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if(DEBUG)
            Log.e(LOG_TAG, "-- ON STOP --");
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (DEBUG)
            Log.e(LOG_TAG, "--- ON DESTROY ---");

        if (mSerialService != null)
            mSerialService.stop();

    }

    public int getConnectionState() {
        return mSerialService.getState();
    }

    // The Handler that gets information back from the BluetoothService
    private final Handler mHandlerBT = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if(DEBUG) Log.i(LOG_TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothSerialService.STATE_CONNECTED:
                            if (mMenuItemConnect != null) {
                                mMenuItemConnect.setIcon(android.R.drawable.ic_menu_close_clear_cancel);
                                mMenuItemConnect.setTitle(R.string.disconnect);
                            }

                            mInputManager.showSoftInput(homeView, InputMethodManager.SHOW_IMPLICIT);

                            break;

                        case BluetoothSerialService.STATE_CONNECTING:
                            break;

                        case BluetoothSerialService.STATE_LISTEN:
                        case BluetoothSerialService.STATE_NONE:
                            if (mMenuItemConnect != null) {
                                mMenuItemConnect.setIcon(android.R.drawable.ic_menu_search);
                                mMenuItemConnect.setTitle(R.string.connect);
                            }

                            mInputManager.hideSoftInputFromWindow(homeView.getWindowToken(), 0);


                            break;
                    }
                    break;
                case MESSAGE_WRITE:
//                    if (mLocalEcho) {
//                        byte[] writeBuf = (byte[]) msg.obj;
//                        homeView.write(writeBuf, msg.arg1);
//                    }

                    break;
/*
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                mEmulatorView.write(readBuf, msg.arg1);

                break;
*/
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), getString(R.string.toast_connected_to) + " "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //send via bluetooth using bluetooth serial service
    public void send(byte[] out) {

        if ( out.length > 0 ) {
            mSerialService.write( out );
        }
    }
    //after returning from another activity
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(DEBUG) Log.d(LOG_TAG, "onActivityResult " + resultCode);
        switch (requestCode) {

            case REQUEST_CONNECT_DEVICE:

                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(BT_DeviceList.EXTRA_DEVICE_ADDRESS);
                    Log.i("BT address", address);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mSerialService.connect(device);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    Log.d(LOG_TAG, "BT not enabled");
                   // finishDialogNoBluetooth();
                }
                break;
                
            case REQUEST_Time:
                // When the request to enable Bluetooth returns
                if (resultCode != Activity.RESULT_OK) {
                    Log.i("time", "time back");
                    if (data.hasExtra("time")){
                        time = data.getExtras().getStringArray("time");
                        Log.i("time", String.valueOf(time));
                    }

                }
                break;
        }
    }

    public void finishDialogNoBluetooth() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_no_bt)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.app_name)
                .setCancelable( false )
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
