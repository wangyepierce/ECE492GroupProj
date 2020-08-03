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
    public void setLampCheck(){
        
        solo.clickOnView(solo.getView(R.id.lighton));
        solo.waitForActivity(HomeActivity.class);

		solo.waitForText("off",1,2000);

        solo.clickOnView(solo.getView(solo.getView(R.id.seekbar), 30));
        
        
        solo.clickOnView(solo.getView(solo.getView(R.id.seekBar_bright), 30));
        solo.clickOnView(solo.getView(solo.getView(R.id.seekBar_red), 40));
        solo.clickOnView(solo.getView(solo.getView(R.id.seekBar_green), 60));
        solo.clickOnView(solo.getView(solo.getView(R.id.seekBar_blue), 80));
        
    }
    
    @Test
    public void connectCheck(){
        
        solo.clickOnView(solo.getView(R.id.set_bluetooth));
        

		solo.waitForText("SmartLamp",1,2000);
		
        solo.clickInList(0);


        solo.clickOnView(solo.getView(Spinner.class, 1));
        solo.scrollToTop();
        solo.clickOnText("raspberrypi");

        solo.waitForText("connected");

    }





}
