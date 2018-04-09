import json
import random

from django.http import HttpResponse
import os
import base64
# Create your views here.
import re
import time
from Book_server.Main.Util.ObtainToken import ObtainToken
from Book_server.models import *


def register(request):
    msg = ""
    code = ""
    if request.method == "POST":
        username = request.POST.get('username')
        password = request.POST.get('password')
        school = request.POST.get("school")
        phone = request.POST.get('phone')
        occupation = request.POST.get('occupation')
        user_class = request.POST.get('class_')
        if re.findall(r'^[A-Za-z]+[_A-Za-z0-9]*|^[1-9][0-9]{10,10}$', str(username)):
            if User.objects.filter(username=username):
                code = '100'
                msg = '账号已经被使用了'
        else:
            code = '101'
            msg = '账号必须是电话号码、或者字母开头的可包含数字和下划线的字符串'
        if re.findall(r'^[_.#*@%&A-Za-z0-9]{6,20}$', str(password)):
            pass
        else:
            code = '102'
            msg = '密码包含特殊符号、或长度小于6'

        if msg == "":
            user = User(username=username, password=password, school=school, phone=phone, occupation=occupation,
                        user_class=user_class)
            user.save()
            code = '200'
        return HttpResponse(json.dumps({'code': code, 'msg': msg}), content_type="application/json")


def login(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        password = request.POST.get('password')
        if User.objects.filter(username=username, password=password):
            user = User.objects.filter(username=username).first()
            if not user.token_RongYun:
                code = "null"
                token = "null"
                msg = "null"  # 不能写成：code ,token,msg = 'null'
                from Book_server.Main.Util.ObtainToken import ObtainToken
                get_token = ObtainToken(username, user.nickname, user.user_icon)
                data = get_token.getRongToken()
                if data['code'] == '200':
                    User.objects(username=username).update(token_RongYun=data['token'])
                    code = "200"
                    token = data['token']
                elif data['code'] == '401':
                    code = '401'
                    msg = data['msg']
                else:
                    msg = data['msg']
                return HttpResponse(json.dumps({'code': code, 'user_icon ': user.user_icon, 'nickname': user.nickname,
                                                'token': token, 'msg': msg}), content_type="application/json")
            else:
                return HttpResponse(json.dumps({'code': '200', 'user_icon ': user.user_icon, 'nickname': user.nickname,
                                                'token': user.token_RongYun}), content_type="application/json")
        else:
            return HttpResponse(json.dumps({'code': "103", "msg": "账号或密码错误"}), content_type="application/json")


def queryInfo(request):
    if request.method == 'POST':
        action = request.POST.get('action')
        user_ = request.POST.get('username')
        if action == 'modify':
            userInfo = User.objects.filter(username = user_).first()
            token = ''
            nickname = request.POST.get('nickname')
            phone = request.POST.get('phone')
            school = request.POST.get('school')
            occupation = request.POST.get('occupation')
            class_ = request.POST.get('class_')
            grade = request.POST.get('grade')
            icon = request.POST.get('icon')
            if icon == 'null' and userInfo.nickname == nickname:
                try:
                    if occupation == '学生':
                        User.objects(username=user_).update( occupation=occupation,user_grade=grade, user_class=class_, phone=phone,school=school)
                    else:
                        User.objects(username=user_).update(occupation=occupation,phone=phone, school=school, user_class="", user_grade='',)
                    code = "200"
                except:
                    code = '失败'
            elif icon == 'null' and userInfo.nickname != nickname:
                tokenClass = ObtainToken(user_, nickname, userInfo.user_icon)
                tokenDict = tokenClass.getRongToken()
                if tokenDict['code'] == '200':
                    token = tokenDict['token']
                try:
                    if occupation == '学生':
                        User.objects(username=user_).update( nickname=nickname, occupation=occupation,user_grade=grade, user_class=class_, phone=phone,school=school,token_RongYun=token)
                    else:
                        User.objects(username=user_).update(nickname=nickname, occupation=occupation,phone=phone, school=school, user_class="", user_grade='',token_RongYun=token)
                    code = "200"
                except:
                    code = '失败'
            elif icon != 'null' and userInfo.nickname == nickname:
                url = uploadImg('heads', icon, user_ + '_head_Portrait_by_tm' + '.jpeg')  # str(int(time.time()))
                tokenClass = ObtainToken(user_, nickname, url)
                tokenDict = tokenClass.getRongToken()
                if tokenDict['code'] == '200':
                    token = tokenDict['token']
                try:
                    if occupation == '学生':
                        User.objects(username=user_).update(user_icon = url ,occupation=occupation,user_grade=grade, user_class=class_, phone=phone,school=school,token_RongYun=token)
                    else:
                        User.objects(username=user_).update(user_icon = url , occupation=occupation,phone=phone, school=school, user_class="", user_grade='',token_RongYun=token)
                    code = "200"
                except:
                    code = '失败'
            elif icon != 'null' and userInfo.nickname != nickname:
                url = uploadImg('heads', icon, user_ + '_head_Portrait_by_tm' + '.jpeg')  # str(int(time.time()))
                tokenClass = ObtainToken(user_, nickname, url)
                tokenDict = tokenClass.getRongToken()
                if tokenDict['code'] == '200':
                    token = tokenDict['token']
                try:
                    if occupation == '学生':
                        User.objects(username=user_).update(user_icon=url,nickname = nickname, occupation=occupation,user_grade=grade, user_class=class_, phone=phone,school=school,token_RongYun=token)
                    else:
                        User.objects(username=user_).update(user_icon=url,nickname = nickname, occupation=occupation,phone=phone, school=school, user_class="", user_grade='',token_RongYun=token)
                    code = "200"
                except:
                    code = '失败'
            return HttpResponse(json.dumps({"code": code, 'icon': icon}), content_type="application/json")
    elif request.method == 'GET':
        user_ = request.GET.get('username')
        action = request.GET.get('action')
        user_info = User.objects(username=user_).first()
        if action == 'userInfo':
            userInfo_ = {
                'nickname': user_info.nickname,
                'icon': user_info.user_icon,
                'phone': user_info.phone,
                'school': user_info.school,
                'occupation': user_info.occupation,
                'class_': user_info.user_class,
                'grade': user_info.user_grade
            }
            return HttpResponse(json.dumps(userInfo_), content_type="application/json")
        elif action == 'listOfFriend':
            friendList = []
            if (not user_info.list_of_friends) or (
                len(user_info.list_of_friends) < 1):  # 如果数据库没有list_of_friends，则不能用len()
                return HttpResponse(json.dumps({'code': '404'}), content_type="application/json")
            else:
                for itemDict in user_info.list_of_friends:
                    data = {}
                    friend = User.objects.filter(username=itemDict['username']).first()
                    data = {
                        'username': itemDict['username'],
                        'remark': itemDict['remark'],
                        'icon': friend.user_icon,
                        'occupation': friend.occupation,
                        'class_': friend.user_class,
                        'phone': friend.phone,
                        'school': friend.school,
                        'nickname': friend.nickname,
                        'grade': friend.user_grade
                    }
                    friendList.append(data)
                return HttpResponse(json.dumps({'list': friendList, 'code': '200'}), content_type="application/json")
        elif action == 'friendship':
            list = []
            if User.objects:
                for user in User.objects:
                    list.append({'username': user.username, 'nickname': user.nickname, 'icon': user.user_icon})
                return HttpResponse(json.dumps({'code': '200', 'list': list}), content_type="application/json")
            else:
                return HttpResponse(json.dumps({'code': '404', 'msg': '数据'}), content_type="application/json")


def search(request):
    if request.method == 'GET':
        isFriend = "False"
        username = request.GET.get('username')
        searchName = request.GET.get('searchName')
        user_name = User.objects.filter(username=searchName).first()
        remark = ''
        if user_name:
            if searchName == username:
                isFriend = "True"
            else:
                friendList = User.objects.filter(username=username).first().list_of_friends
                if not friendList or len(friendList) < 1:
                    isFriend = 'False'
                else:
                    for friend in friendList:
                        if friend['username'] == searchName:
                            isFriend = "True"
                            remark = friend['remark']
                            break
                        else:
                            isFriend = 'False'
            data = {
                'code': "200",
                'isFriend': isFriend,
                'remark': remark,
                'username': user_name.username,
                'nickname': user_name.nickname,
                'class_': user_name.user_class,
                'grade': user_name.user_grade,
                'school': user_name.school,
                'occupation': user_name.occupation,
                'phone': user_name.phone,
                'icon':user_name.user_icon
            }
            return HttpResponse(json.dumps(data), content_type="application/json")
        else:
            return HttpResponse(json.dumps({"code": '404', 'msg': '数据库里没有相关信息'}), content_type="application/json")
    elif request.method == 'POST':
        user = request.POST.get('username')
        searchName = request.POST.get('searchName')
        remark = request.POST.get('remark')
        friendList = User.objects.filter(username=user).first().list_of_friends
        list_f = []
        for itemDict in friendList:
            if itemDict['username'] == searchName:
                data = {
                    'username': searchName,
                    'remark': remark,
                    ' QR': ''
                }  # 不能再itemDict 上直接修改dict中的值
                list_f.append(data)
            else:
                list_f.append(itemDict)
        if user:
            try:
                User.objects(username=user).update(list_of_friends=list_f)
                code = "200"
            except:
                code = "404"
        return HttpResponse(json.dumps({"code": code}), content_type="application/json")


def userTasks(request):
    if request.method == 'GET':
        username = request.GET.get("username")
        action = request.GET.get('action')
        user = User.objects.filter(username=username).first()
        if action == 'collectTasks':
            if user.tasks_collect:  # or (len(user.tasks_collect) > 0) 没有len属性
                infoList = []
                for itemDict in user.tasks_collect:
                    info = {}
                    task = Tasks.objects.filter(taskId=itemDict['taskId']).first()
                    info['taskId'] = itemDict['taskId']
                    info['context'] = task.context
                    info['byId'] = task.byId
                    if task.contextImg or (task.contextImg != ""):
                        info['contextImg'] = task.contextImg
                    else:
                        info['contextImg'] = "null"
                    info['msg'] = itemDict['msg']  # 不能用if itemDict['msg']:
                    info['collectFlag'] = 'true'
                    infoList.append(info)
                return HttpResponse(json.dumps({'code': '200', 'list': infoList}), content_type="application/json")
            else:
                return HttpResponse(json.dumps({'code': '404', 'msg': "没有收藏信息"}), content_type="application/json")
        elif action == 'allTasks':
            if Tasks.objects:
                listId = []
                if user.tasks_collect:
                    for userTasks in user.tasks_collect:
                        listId.append(userTasks['taskId'])
                list = []
                for item in Tasks.objects:
                    taskDict = {}
                    if item.taskId in listId:
                        if item.contextImg or (item.contextImg != ""):
                            taskDict = {"taskId": item.taskId, 'context': item.context, "contextImg": item.contextImg,
                                        'collectFlag': 'true','byId' : item.byId}
                        else:
                            taskDict = {"taskId": item.taskId, 'context': item.context, "contextImg": "null",
                                        'collectFlag': 'true','byId' : item.byId}
                    else:
                        if item.contextImg or (item.contextImg != ""):
                            taskDict = {"taskId": item.taskId, 'context': item.context, "contextImg": item.contextImg,
                                        'collectFlag': 'false','byId' : item.byId}
                        else:
                            taskDict = {"taskId": item.taskId, 'context': item.context, "contextImg": "null",
                                        'collectFlag': 'false','byId' : item.byId}
                    list.append(taskDict)
                return HttpResponse(json.dumps({'code': '200', 'list': list}), content_type="application/json")
            else:
                return HttpResponse(json.dumps({'code': '404', 'msg': "暂无习题"}), content_type="application/json")
    if request.method == 'POST':
        user_name = request.POST.get("username")
        action = request.POST.get('action')
        user = User.objects(username=user_name).first()
        if action == 'modifyCollect':
            isFollow = request.POST.get('isFollow')
            id = int(request.POST.get('taskId'))
            msg = request.POST.get('msg')
            list_collect = []
            existList = []
            if not user.tasks_collect or (len(user.tasks_collect) < 1):
                list_collect.append({"taskId": id, "msg": msg})
            else:
                for item in user.tasks_collect:
                    existList.append(item['taskId'])
                if id not in existList:
                    for item in user.tasks_collect:
                        list_collect.append(item)
                    list_collect.append({"taskId": id, "msg": msg})
                else:
                    for collectDict in user.tasks_collect:
                        dict_c = {}
                        if collectDict['taskId'] == id:
                            if isFollow == '已关注':
                                dict_c['taskId'] = id
                                dict_c['msg'] = msg
                                list_collect.append(dict_c)
                            else:
                                continue
                        else:
                            list_collect.append(collectDict)
            if user:
                try:
                    User.objects(username=user_name).update(tasks_collect=list_collect)
                    code = '200'
                except:
                    code = '404'
            return HttpResponse(json.dumps(
                {'code': code, 'msg': "修改成功", 'id': id, 'msg1': msg, 'follow': isFollow, 'name': user_name,
                 'isHave': existList}), content_type="application/json")


# 保存图片
def uploadImg(mark, strImg, fileName):
    path = os.getcwd()[:-24] + r'Images/' + mark + '/' + fileName
    # print(path)
    imageData = base64.b64decode(strImg.replace("\n", ""))
    try:
        with open(path.replace("\\", "/"), 'wb') as f:  # 打开方式：https://www.cnblogs.com/wzjbg/p/6383518.html
            f.write(imageData)
    except:
        return None
    return 'http://140.143.95.232:8000/images/?mark=' + mark + '&image=' + fileName


# 访问图片
def imageServer(request):
    imageName = request.GET.get('image')
    mark = request.GET.get('mark')
    imagePath = os.getcwd()[:-24] + r'Images/' + str(mark) + '/' + str(imageName)
    try:
        image_data = open(imagePath.replace("\\", "/"), "rb").read()
    except:
        image_data = open(os.getcwd()[:-24] + r'Images/icon.png','rb').read()
    return HttpResponse(image_data, content_type="image/png")  # 注意旧版的资料使用mimetype,现在已经改为content_type


# 习题id
def randomId():
    id = 1
    isExist = True
    idList = []
    for item in Tasks.objects:
        idList.append(item.taskId)
    while isExist:
        id = random.randint(1, 100000)
        if id in idList:
            continue
        else:
            isExist = False
            break
    return id


def saveTasks(request):
    if request.method == 'POST':
        username = request.POST.get('username')
        action = request.POST.get('action')
        if action == 'save':
            content = request.POST.get('content')
            image = request.POST.get('image')
            tag = request.POST.get('tag')
            taskId = randomId()
            if image == "null":
                if tag == "true":
                    tasks = Tasks(taskId=taskId, byId=username, context=content)
                    tasks.save()
                else:
                    tasks = Tasks(taskId=taskId, byId=username + '_false', context=content)
                    tasks.save()
            else:
                url = uploadImg("tasks", image, 'tasktImage_by_' + username + "_" + str(int(time.time())) + '.jpeg')
                if tag == "true":
                    tasks = Tasks(taskId=taskId, byId=username, context=content, contextImg=url)
                    tasks.save()
                else:
                    tasks = Tasks(taskId=taskId, byId=username + '_false', context=content, contextImg=url)
                    tasks.save()
            return HttpResponse(json.dumps({'msg': '保存成功'}))
