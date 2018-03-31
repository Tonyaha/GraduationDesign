import json
import random
import requests
import time
import hashlib

from Book_server.models import Tasks


class ObtainToken():
    def __init__(self, userId, nickName, icon):
        self.userId = userId
        self.nickName = nickName
        self.iconUri = icon
        self.url = 'http://api.cn.ronghub.com/user/getToken.json'
        self.resultDict = {}

    def getRongToken(self):
        randomNum = str(random.randint(0,888888)*1000)
        timeStamp = str(int(time.time()))
        string = '%s%s%s' % ('8snmcdALQl',randomNum,timeStamp)
        signature = hashlib.sha1(string.encode('utf-8')).hexdigest()
        header = {
            'Host': 'api.cn.ronghub.com',
            'App-Key': 'cpj2xarlc73qn',
            'Nonce': randomNum,
            'Timestamp': timeStamp,
            'Signature': str(signature),
            'Content-Type': 'application/x-www-form-urlencoded'
        }
        postData = {
            'userId': self.userId,
            'name': self.nickName,
            'portraitUri': self.iconUri
        }
        try:
            response = requests.post(self.url,headers=header,data=postData)
            data = json.loads(response.text)
            if response.status_code == 401:
                self.resultDict['code'] = str(response.status_code)
                self.resultDict['msg'] = data['errorMessage']
                print(response.text)
            elif response.status_code == 200:
                print(response.text)
                self.resultDict['code'] =str(response.status_code)
                self.resultDict['token'] = data['token']
        except requests.RequestException as e:
            self.resultDict['msg'] = e
            print('>>>ObtainToken<<<',e)
        return  self.resultDict

def friendShip():
    url = 'http://webim.demo.rong.io.request_friend'
    randomNum = str(random.randint(0, 888888) * 1000)
    timeStamp = str(int(time.time()))
    string = '%s%s%s' % ('8snmcdALQl', randomNum, timeStamp)
    signature = hashlib.sha1(string.encode('utf-8')).hexdigest()
    header = {
        'Host': 'api.cn.ronghub.com',
        'App-Key': 'cpj2xarlc73qn',
        'Nonce': randomNum,
        'Timestamp': timeStamp,
        'Signature': str(signature),
        'Content-Type': 'application/x-www-form-urlencoded'
    }
    try:
        rep = requests.post(url,headers=header,data={"Interger":"tm",'String':"sgdsgs"}).text
        print(rep)
    except requests.RequestException as e:
        print(">>>>>>>FriendShip<<<<<<<",e)

def test():
    x = '问他突然听到让我感到个人和人家好地方实施的各色让他为人液体角色瓦夫为大师傅似的挥金如土鬼地方鬼地方按时发送的房子是电子分色规定阿三的知识点v真实的'
    task = Tasks(taskId = 4,context = x.encode('utf-8'))
    task.save()
if __name__ == '__main__':
    # token = ObtainToken('abcjhg','ABC','http://wmimg.sc115.com/tx/new/pic/0414/16044cga2r4dh2v.jpg')
    # token.getRongToken()
    #friendShip()
    test()