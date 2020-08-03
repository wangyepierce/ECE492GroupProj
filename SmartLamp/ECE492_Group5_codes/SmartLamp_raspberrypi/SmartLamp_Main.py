import RPi.GPIO as GPIO

import LED
import math

import signal
import flicklib
import LED
import time
import curses
from curses import wrapper

import bluetooth
import re

import snowboydecoder
import sys


# voice control
interrupted = False

def signal_handler(signal, frame):
    global interrupted
    interrupted = True


def interrupt_callback():
    global interrupted
    return interrupted
    
    


some_value = 5000

#this part is for Light sensor
GPIO.setwarnings(False)
GPIO.setmode(GPIO.BCM)
pin_to_circuit = 16
def rc_time(pin_to_circuit):
    count = 0
    GPIO.setup(pin_to_circuit, GPIO.OUT)
    GPIO.output(pin_to_circuit, GPIO.LOW)
    time.sleep(1)
    
    GPIO.setup(pin_to_circuit, GPIO.IN)
    
    while(GPIO.input(pin_to_circuit) == GPIO.LOW):
        count += 1
    return count


#for gesture control
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
        
    elif gesture == 'light':
        if on == 1:
            delay = 0.3
			initLED.displayLight(initColor, brightness_level)
			time.sleep(delay)
        
    
    
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


def main():

	global mode
	mode = 0
    global brightness_level
    brightness_level = 0.5
    global initColor 
    initColor = LED.Color()
    global initLED 
    initLED = LED.LED(brightness_level)
    initLED.turnoff()
    global on
    on = 0
    
    
    #gesture control
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
    
    
    #Bluetooth
	server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
	port = 1
	server_sock.bind(("",port))
	server_sock.listen(1)
	client_sock,address = server_sock.accept()
	print ("Accepted connection from" , address)
	redNum = 0
	greenNum = 0
	blueNum = 0
	flagOn = 0
	
	#voice control
	model = ["turn_on.pmdl","turn_off.pmdl","green.pmdl","yellow.pmdl","white.pmdl","pink.pmdl"]
	signal.signal(signal.SIGINT, signal_handler)
	callbacks = [lambda: snowboydecoder.turn_on(),
				 lambda: snowboydecoder.turn_off(),
				 lambda: snowboydecoder.change_color("Green"),
				 lambda: snowboydecoder.change_color("Yellow"),
				 lambda: snowboydecoder.change_color("White"),
				 lambda: snowboydecoder.change_color("Pink")]
				 
	

    
    
    while True:
    
    	# main loop for voice control
		detector.start(detected_callback=callbacks,
					   interrupt_check=interrupt_callback,
					   sleep_time=0.03)
		detector.terminate()
		
		if len(touchtxt) > 0:
				if mode == 1:
					mode = 0
				elif mode == 0:
					mode = 1
		
    	if mode == 1: 	
			#light sensor
			light_num = rc_time(pin_to_circuit)
			if(light_num > 10000):
				light_num = 10000
			elif(light_num < 100):
				light_num = 100
			delta = abs((light_num/10000) - brightness_level)
			if(delta > 0.03):
				brightness_level = light_num/10000 
			set_LED('light', brightness_level)
        
        else:
			#gesture control
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
			
		
			#bluetooth
			recvdata = client_sock.recv(1024)
			recvStr = str(recvdata, 'utf-8')
			
			print ("Received \"%s\" through Bluetooth" % recvdata)
	
			if recvStr == "on" and flagOn == 0:
				initLED.displayLight(initColor, brightness_level)
				flagOn = 1
				print("on")
				
			if recvStr == "off" and flagOn == 1:
				print("off")
				initLED.turnoff()
				flagOn = 0
				
			if(flagOn == 1):
			 
				if 'brightness' in recvStr:
					print(recvStr)
					brightness_level = int(re.search(r'\d+', recvStr).group())
					print(brightness_level)
					if brightness_level == 0:
						brightness_level = 0.5
						   
				if 'red' in recvStr:
					print(recvStr)
					redNum = int(re.search(r'\d+', recvStr).group())
					  
				if 'blue' in recvStr:
					print(recvStr)
					blueNum = int(re.search(r'\d+', recvStr).group())
					 
				if 'green' in recvStr: 
					print(recvStr)
					greenNum = int(re.search(r'\d+', recvStr).group())
					
				print(redNum)
				print(blueNum)
				print(greenNum)
				
				if (redNum + blueNum + greenNum == 0):
				
					initColor.setSelfColor(20, 20, 20)
					initLED.displayLight(initColor, brightness_level/10)
				else:
				
					initColor.setSelfColor(redNum, greenNum, blueNum)
					initLED.displayLight(initColor, brightness_level/10)
		

    
if __name__ == '__main__':
    main()