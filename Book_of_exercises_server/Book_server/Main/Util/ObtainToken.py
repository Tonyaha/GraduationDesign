import json
import random
import requests
import time
import hashlib

from Book_server.models import Tasks, User


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


def test():
    user = User.objects(username = "tm").first()
    print(type(list(user.list_of_friends)))


if __name__ == '__main__':
    # token = ObtainToken('aaa','ABC','http://wmimg.sc115.com/tx/new/pic/0414/16044cga2r4dh2v.jpg')
    # token.getRongToken()

    test()
