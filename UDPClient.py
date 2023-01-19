from socket import *
serverName = '192.168.1.34'
serverPort = 1200
clientSocket = socket(AF_INET, SOCK_DGRAM)

def modifyMessage(message):
	if(message == "bye"):
		print("connection terminated")
		clientSocket.close()
	else:
		clientSocket.sendto(message.encode(), (serverName, serverPort))
		modifiedMessage, serverAddress = clientSocket.recvfrom(2048)
		print(modifiedMessage.decode())

print("Input lowercase sentence: ")

while True:
   modifyMessage(input())


  


