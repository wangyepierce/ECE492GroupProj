# creation date: Feb 14 2020
# author: group 5
# contents of the file: test code for controlling the LED on Raspberry pi

import board
import neopixel
import time
pixels = neopixel.NeoPixel(board.D18, 12,auto_write = False)

class LED:
    def __init__(self, brightness_level):
        
        self.brighteness_level = brightness_level
        
    def displayLight(self,color, brightness_level):
    
        pixels.fill((int(brightness_level*color.red),int(brightness_level*color.green),int(brightness_level*color.blue)))
        pixels.show()
        
    def turnoff(self):
        pixels.fill((0,0,0))
        pixels.show()
        
class Color:
    def __init__(self):
        self.red = 255
        self.green = 255
        self.blue = 255
        
    def setColor(self,color_num):
        if color_num == "White":
            self.red = 255
            self.green = 255
            self.blue = 255
        elif color_num == "Yellow":
            self.red = 255
            self.green = 255
            self.blue = 0
        elif color_num == "Pink":
            self.red = 255
            self.green = 105
            self.blue = 108
            
        elif color_num == "Green":
            self.red = 0
            self.green = 255
            self.blue = 127
    
    
    
def main():
    
    brightness_level = 0.6

    
    initColor = Color()
    User1 = LED(brightness_level)
    
    while True:
            print("Enter test number(1-9 or exit): ")
            x = input()
            
            if x == '1':
                User1.displayLight(initColor, brightness_level)
            elif x == '2':
                initColor.setColor("Yellow")
                User1.displayLight(initColor, brightness_level)
            elif x == '3':
                initColor.setColor("Green")
                User1.displayLight(initColor, brightness_level)
            elif x == '4':
                initColor.setColor("Pink")
                User1.displayLight(initColor, brightness_level)
            elif x == '5':
                initColor.setColor("White")
                User1.displayLight(initColor, brightness_level)
            elif x == '6':
                brightness_level = 0.1
                User1.displayLight(initColor, brightness_level)
            elif x == '7':
                brightness_level = 0.1
                User1.displayLight(initColor, brightness_level)
            elif x == '8':
                brightness_level = 0.8
                User1.displayLight(initColor, brightness_level)
            elif x == '9':
                brightness_level = 1.0
                User1.displayLight(initColor, brightness_level)
            elif x == 'exit':
                User1.turnoff()
                
                
if __name__ == '__main__':
    main()
