package com.ece492T5.smartlamp;

import android.graphics.Color;

import com.ece492T5.smartGesture;.models.Mood;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit Test for Lamp Class
 */
public class LampTest {

    private Lamp Lamp;

    @Before
    public void setUp(){
        lamp = new Lamp("user1");
    }

    @Test
    public void testMood(){
        Lamp testLamp = new Lamp("test1");
        assertSame(testMood.getUser(),"test1");
    }

    @Test
    public void testGetMood(){
        assertSame("test1", mood.getUser());
    }

    @Test
    public void testON(){
        lamp.turnON();
        assertSame("ON", mood.getStatus());
    }
    
    @Test
    public void testOFF(){
        lamp.turnOff();
        assertSame("OFF", mood.getStatus());
    }
    @Test
    public void testSetColor(){
        lamp.setColor(Color.GREEN);
    }

    @Test
    public void testGetColor(){
    	lamp.setColor(Color.GREEN);
        assertEquals(lamp.getColor(), Color.GREEN);
    }

}