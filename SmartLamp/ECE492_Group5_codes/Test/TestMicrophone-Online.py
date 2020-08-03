import speech_recognition as sr
import TestLED

def init():
    mic_name = "Bus 001 Device 009: ID 8086:0808 Intel Corp."
    sample_rate = 48000
    chunk_size = 2048
    r = sr.Recognizer()
    r.energy_threshold = 3000
    while True: 
        with sr.Microphone(sample_rate = sample_rate, chunk_size = chunk_size) as source:
            r.adjust_for_ambient_noise(source)
        
            print("Please say something...")
        
            audio = r.listen(source)
        
            spokenText = "" + r.recognize_google(audio)
            print(spokenText)
            
            if len(spokenText) > 0:
                print ("Trigger word: " + spokenText)
            
                triggerWordIndex = spokenText.lower().find("hello")
                
                if triggerWordIndex > -1:
                    
                    print("Hello Master")
                    brightness_level = 0.5
                    global initColor 
                    initColor = TestLED.Color()
                    global initLED 
                    initLED = TestLED.LED(brightness_level)
                    initColor.setColor('White')
                    
                    initLED.displayLight(initColor, brightness_level)
                    
                    while True:
                        r.adjust_for_ambient_noise(source)
        
                        print("Please say something...")
                        audio = r.listen(source)
                        
                        commandStr =  r.recognize_google(audio)
                        print(commandStr)
                            
                        if commandStr.lower().find("turn") > -1 and commandStr.lower().find("on") > -1:
                            print("On")
                        elif commandStr.lower().find("turn") > -1 and commandStr.lower().find("off") > -1:
                            print("off")
                        elif commandStr.lower().find("4") > -1:
                            print("green")
                        elif commandStr.lower().find("yellow") > -1:
                            print("yellow")
                        elif commandStr.lower().find("pink") > -1:
                            print("pink")
                        elif commandStr.lower().find("white") > -1:
                            print("white")
                        
                    
        
        
        
if __name__ == "__main__":
    init()
