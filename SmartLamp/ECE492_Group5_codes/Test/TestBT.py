# creation date: Feb 20 2020
# author: group 5
# contents of the file: test code for BlueTooth connection

import bluetooth
import TestLED

server_sock = bluetooth.BluetoothSocket(bluetooth.RFCOMM)
port = 1
server_sock.bind(("",port))
server_sock.listen(1)
client_sock,address = server_sock.accept()
print ("Accepted connection from" , address)
while True:
    recvdata = client_sock.recv(1024)
    recvStr = str(recvdata, 'utf-8')
    brightness_level = 0.6
    initColor = TestLED.Color()
    User1 = TestLED.LED(brightness_level)
    print ("Received \"%s\" through Bluetooth" % recvdata)
    
    if recvStr == "on":
        print("on")
    if recvStr == "off":
        print("off")
    if 'brightness' in recvStr:
        print(recvStr)
    if 'red' in recvStr:
        print(recvStr)
    if 'blue' in recvStr:
        print(recvStr)
    if 'green' in recvStr:
        print(recvStr)
    if (recvdata == "Q"):
        print("exiting")
        break
    
client_sock.close()
server_sock.close()
