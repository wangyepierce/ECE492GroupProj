import RPi.GPIO as GPIO
import time

GPIO.setwarnings(False)
GPIO.setmode(GPIO.BOARD)
pin_to_circuit = 36
GPIO.setup(pin_to_circuit, GPIO.IN)

while True:
        print(GPIO.input(pin_to_circuit))