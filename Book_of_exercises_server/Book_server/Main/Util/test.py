from socket import *

from click._compat import raw_input

HOST = '172.16.22.193'
PORT = 8000
BUFSIZ = 1024
ADDR = (HOST, PORT)

tcpCliSock = socket(AF_INET, SOCK_STREAM)
tcpCliSock.connect(ADDR)

while True:
    data = raw_input('> ')
    data = data.encode(encoding='utf-8') # str 转 bytes
    if not data:
        break
    tcpCliSock.send(data)

    recv_data = tcpCliSock.recv(BUFSIZ)
    recv_data = recv_data.decode('utf-8')
    if not recv_data:
        break
    print(recv_data)

tcpCliSock.close()