from socket import *
from time import ctime

HOST = '172.16.22.193'
PORT = 8000
BUFSIZE = 2048
ADDR = (HOST, PORT)

tcpSerSock = socket(AF_INET, SOCK_STREAM)
tcpSerSock.setsockopt(SOL_SOCKET, SO_REUSEADDR, 1) #端口重复使用
tcpSerSock.bind(ADDR)
tcpSerSock.listen(5)

while True:
    print('waiting for connection...')
    tcpCliSock, addr = tcpSerSock.accept()
    print('...connected from:', addr)

    while True:
        data = tcpCliSock.recv(BUFSIZE)
        data = data.decode('utf-8')
        if not data:
            break
        time = ctime()
        t = ('get your data:%s\n[%s]' % (data, time))
        t = t.encode(encoding='utf-8')
        tcpCliSock.send(t)
        tcpCliSock.close()

    tcpSerSock.close()
