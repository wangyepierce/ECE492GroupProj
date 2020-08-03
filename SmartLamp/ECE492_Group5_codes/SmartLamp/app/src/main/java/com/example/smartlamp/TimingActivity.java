/**
 * Authors: Shixiong Gao, Xiaoyu Liu, Ye Wang, Xianda Xu
 * Date: Jan. 30, 2020
 */

/**
 * This activity is used to let users to set time. Users can
 * set a time to control the lamp on/off
 */

package com.example.smartlamp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import androidx.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Date;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;


public class TimingActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView startTimeTv;
    private TextView endTimeTv;
    private Boolean switchOpen = false;
    private String[] time = {"0", "0", "0", "0"};
    private static BluetoothSerialService mSerialService = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
        initView();
    }

    //draw the time activity view
    private void initView() {
        SharedPreferences sharedPreferences = getSharedPreferences("timing", MODE_PRIVATE);
        //get all item from activity
        ImageView back = findViewById(R.id.setting_back);
        TextView save = findViewById(R.id.save);
        Switch aSwitch = findViewById(R.id.switch_timing);
        startTimeTv = findViewById(R.id.start_time_tv);
        endTimeTv = findViewById(R.id.end_time_tv);
        LinearLayout startTime = findViewById(R.id.start_time);
        LinearLayout endTime = findViewById(R.id.end_time);


        if (back == null || save == null || startTime == null || endTime == null || aSwitch == null) {
            return;
        }

        //set listener
        back.setOnClickListener(this);
        save.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);
        //check for state
        if (sharedPreferences.getBoolean("checkState", false)) {
            aSwitch.setChecked(true);
            switchOpen = true;
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchOpen = isChecked;
            }
        });


        setStartTimeTv(Integer.parseInt(sharedPreferences.getString("startH", "00")), Integer.parseInt(sharedPreferences.getString("startM", "00")));
        setEndTimeTv(Integer.parseInt(sharedPreferences.getString("endH", "00")), Integer.parseInt(sharedPreferences.getString("endM", "00")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //return to home activity
            case R.id.setting_back: {

                Intent intent = new Intent();
                intent.putExtra("time", time);
                // Activity finished return ok, return the data
                setResult(RESULT_OK, intent);
                finish();

            }
            break;
            //save the change
            case R.id.save: {
                alarmOn();
                alarmOff();
            }
            break;
            //set start time
            case R.id.start_time: {
                showStartTimePickerDialog();
            }
            break;
            //set end time
            case R.id.end_time: {
                showEndTimePickerDialog();
            }
            break;
        }

    }
    //draw clock dialog
    private void showStartTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setStartTimeTv(hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }
    //draw clock dialog
    private void showEndTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                setEndTimeTv(hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }
    //set time
    private void setStartTimeTv(int hourOfDay, int minute) {

        //hours and minutes
        String newH = null;
        String newM = null;
        if (hourOfDay < 10) {
            newH = "0" + hourOfDay;
        } else {
            newH = hourOfDay + "";
        }
        if (minute < 10) {
            newM = "0" + minute;
        } else {
            newM = minute + "";
        }
        startTimeTv.setText(newH + ":" + newM);
//        time[0] = newH;
//        time[1] = newM;
    }

    //similar as setStartTimeTv
    private void setEndTimeTv(int hourOfDay, int minute) {
        String newH = null;
        String newM = null;
        if (hourOfDay < 10) {
            newH = "0" + hourOfDay;
        } else {
            newH = hourOfDay + "";
        }
        if (minute < 10) {
            newM = "0" + minute;
        } else {
            newM = minute + "";
        }


        endTimeTv.setText(newH + ":" + newM);
//        time[2] = newH;
//        time[3] = newM;
    }

    private void alarmOn(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm");
        String currentTimeString = currentTimeFormat.format(currentTime);

        String wakeUpTimeString = startTimeTv.getText().toString();

        if(currentTimeString.equals(wakeUpTimeString)){
            String str = "on";
            byte[] byteArr = new byte[0];
            try {
                byteArr = str.getBytes("UTF-8");
                send(byteArr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }


    }

    private void alarmOff(){
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat currentTimeFormat = new SimpleDateFormat("HH:mm");
        String currentTimeString = currentTimeFormat.format(currentTime);

        String BedTimeString = endTimeTv.getText().toString();

        if(currentTimeString.equals(BedTimeString)){
            String str = "off";
            byte[] byteArr = new byte[0];
            try {
                byteArr = str.getBytes("UTF-8");
                send(byteArr);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
    }

    public void send(byte[] out) {
        if ( out.length > 0 ) {
            mSerialService.write(out);
        }
    }


}