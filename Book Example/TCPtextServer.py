from socket import *

serverPort = 12000
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', serverPort))
serverSocket.listen(1)
connectionSocket, addr = serverSocket.accept()
print('the server is ready to receive')
run = True
while run:
        sentence = connectionSocket.recv(1024).decode()
        print(sentence)

        if(sentence == "bye"):
            print("Connection terminated")
            serverSocket.close()
            run = False
        else:
            newMessage = input()
            connectionSocket.send(newMessage.encode())
            if(newMessage == "bye"):
                print("Connection terminated")
                serverSocket.close()
            
        
    