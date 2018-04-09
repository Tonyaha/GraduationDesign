# -*- coding: utf-8 -*-
# Create your models here. 一个model对应数据库一张数据表
from django.db import models
from mongoengine import *
from datetime import datetime

# 连接数据库
connect('TM')  # 连接本地blog数据库


# connect('Data',host='127.0.0.1',port='27017')

# 如需验证和指定主机名
# connect('blog', host='192.168.3.1', username='root', password='1234')

class User(Document):
    user_icon = StringField(default=None)
    nickname = StringField(default=None)
    username = StringField(max_length=1000, required=True)
    password = StringField(max_length=1000, required=True)
    occupation = StringField(required=True)
    user_grade = StringField(default=None)
    user_class = StringField(default=None)
    phone = StringField(default=None)
    school = StringField(required=True)
    tasks_collect = ListField(DictField(required=None),default=None)
    list_of_friends = ListField(DictField(required=None),default=None)
    token_RongYun = StringField(default=None)

class Tasks(Document):
    taskId = IntField(required=True)
    byId = StringField(required=None)
    context = StringField(default=None)
    contextImg = StringField(default=None)


