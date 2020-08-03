#!/usr/bin/env python
import signal
import flicklib
import TestLED
import time
import curses
from curses import wrapper

some_value = 5000


def set_LED(gesture, value):
    global brightness_level
    global initColor
    global initLED
    global on
    
    
    if gesture == 'flick':
        if on == 1:
            if value == 'west - east':
                
                initColor.setColor('White')
                initLED.displayLight(initColor, brightness_level)
                
            elif value == 'east - west':
                
                initColor.setColor('Yellow')
                initLED.displayLight(initColor, brightness_level)
                
            elif value == 'south - north':
                initColor.setColor('Green')
                initLED.displayLight(initColor, brightness_level)
            elif value == 'north - south':
                
                initColor.setColor('Pink')
                initLED.displayLight(initColor, brightness_level)
    
    elif gesture == 'airwheel':
        if on == 1:
            delay = 0.3
            if value == 'clock':
                brightness_level = brightness_level + 0.06
                if brightness_level >= 1:
                    brightness_level = 1
                initLED.displayLight(initColor, brightness_level)
                time.sleep(delay)
            else:
                brightness_level = brightness_level - 0.06
                if brightness_level <= 0.02:
                    brightness_level = 0.02
                initLED.displayLight(initColor, brightness_level)
                time.sleep(delay)
            
    elif gesture == 'doubletap':
        if on == 1:
            on = 0
            initLED.turnoff()
        else:
            on = 1
            initColor.setColor('White')
            brightness_level = 0.5
            initLED.displayLight(initColor, brightness_level)
        time.sleep(0.3)
        
    
    
@flicklib.move()
def move(x, y, z):
    global xyztxt
    global xyz
    xyztxt = '{:5.3f} {:5.3f} {:5.3f}'.format(x,y,z)
    xyz = (x, y, z)

@flicklib.flick()
def flick(start,finish):
    global flicktxt
    flicktxt = start + ' - ' + finish

@flicklib.airwheel()
def spinny(delta):
    global some_value
    global airwheeltxt
    global airwheel
    some_value += delta
    if some_value < 0:
        some_value = 0
    if some_value > 10000:
        some_value = 10000
    airwheeltxt = str(some_value/100)
    airwheel = delta

@flicklib.double_tap()
def doubletap(position):
    global doubletaptxt
    doubletaptxt = position

@flicklib.tap()
def tap(position):
    global taptxt
    taptxt = position

@flicklib.touch()
def touch(position):
    global touchtxt
    touchtxt = position

#
# Main display using curses
#

def main():
    global xyztxt
    global flicktxt
    global airwheeltxt
    global touchtxt
    global taptxt
    global doubletaptxt
    global xyz
    global airwheel

    xyztxt = ''
    flicktxt = ''
    flickcount = 0
    airwheeltxt = ''
    airwheelcount = 0
    touchtxt = ''
    touchcount = 0
    taptxt = ''
    tapcount = 0
    doubletaptxt = ''
    doubletapcount = 0
    xyz = (0,0,0)
    airwheel = 0


    global brightness_level
    brightness_level = 0.5
    global initColor 
    initColor = TestLED.Color()
    global initLED 
    initLED = TestLED.LED(brightness_level)
    initLED.turnoff()
    global on
    on = 0
    
    

    # Update data window continuously until Control-C
    while True:


        if len(flicktxt) > 0 and flickcount < 5:
            flickcount += 1
            print("flicktxt: " + flicktxt)
            set_LED('flick', flicktxt)
            flicktxt = ''


        if len(airwheeltxt) > 0 and airwheelcount < 5:
            airwheelcount += 1
            print("airwheeltxt: " + airwheeltxt)
            if airwheel >= 0:
                set_LED('airwheel', 'clock')
            else:
                set_LED('airwheel', 'cclock')
            airwheeltxt = ''
            
            
        if len(doubletaptxt) > 0 and len(touchtxt) > 0:
            doubletapcount += 1
            print("doubletap: " + doubletaptxt)
            set_LED('doubletap', doubletaptxt)
            doubletaptxt = ''
        

        

        time.sleep(0.1)

if __name__ == '__main__':
    main()

