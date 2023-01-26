from socket import *

serverPort = 80
serverSocket = socket(AF_INET, SOCK_STREAM)
serverSocket.bind(('', serverPort))
serverSocket.listen(1)

print('the server is ready to receive')
msg = "Connection Secured"
while True:
    connectionSocket, addr = serverSocket.accept()
    #sentence = connectionSocket.recv(1024).decode()
    #capitalizedSentence = sentence.upper()
    #connectionSocket.send(capitalizedSentence.encode())

    print("Connected to", addr)

    #connectionSocket.send(msg.encode())
    connectionSocket.close