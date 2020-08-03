package com.ece492T5.smartlamp;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.ece492T5.smartlamp.activities.HomeActivity;
import com.ece492T5.smartlamp.activities.MainActivity;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;



public class IntentTestSetLamp {
    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true,true);


    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());

        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);

        solo.clickOnButton("Join now");

        solo.waitForText("SmartLamp",1,2000);
        solo.assertCurrentActivity("Wrong Activity", SignInActivity.class);


        solo.waitForActivity(HomeActivity.class);

        solo.assertCurrentActivity("Wrong Activity", HomeActivity.class);

    }

    @Test
    public void start() throws Exception{
        Activity activity= rule.getActivity();

    }


    @Test
    public void setTimmer{
        
        solo.clickOnView(solo.getView(R.id.main_set_time));
        solo.waitForActivity(TimingActivity.class);
        solo.clickOnButton(R.id.switch_timing);

        solo.clickOnView(solo.getView(solo.getView(R.id.start_time_tv));
        solo.clickOnText("OK");
        solo.sleep(100);
        solo.clickOnView(solo.getView(solo.getView(R.id.end_time_tv));
        solo.clickOnText("OK");
        solo.sleep(100);
        solo.clickOnText("Save");
        solo.waitForActivity(HomeActivity.class);
        
    }
    
 




}
