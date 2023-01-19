from socket import *
serverName = '192.168.1.34'
serverPort = 12000
clientSocket = socket(AF_INET, SOCK_STREAM)
clientSocket.connect((serverName, serverPort))

print("Ready to send messages...\n")
run = True
while run:
    message = input()
    clientSocket.send(message.encode())
    if(message == "bye"):
        print("Connection Terminated")
        clientSocket.close()
        run = False
        
    sentence = clientSocket.recv(1024).decode()
    print(sentence)
    
    if(sentence == "bye"):
        print("Connection Terminated")
        clientSocket.close()
        run = False
   
      