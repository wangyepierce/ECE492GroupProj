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
    
    def setRed(self, RedNum):
        self.red = RedNum
    
    def setBlue(self, BlueNum):
        self.blue = BlueNum
    
    def setGreen(self, GreenNum):
        self.green = GreenNum