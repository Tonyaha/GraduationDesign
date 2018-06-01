# GraduationDesign

......持续完善中......

RESTful API参考：<br>
·Django REST FrameWork中文文档：http://www.chenxm.cc/post/299.html?zhihu <br>
·Django REST FrameWork英文文档：http://www.django-rest-framework.org <br>
·架构理解：http://www.ruanyifeng.com/blog/2011/09/restful.html <br>
·设计指南：http://www.ruanyifeng.com/blog/2014/05/restful_api.html <br>

配置说明：<br>
一、客服端：<br>
    1、项目名：Book_of_exercises<br>
    2、gradle配置：sdk版本为27，若使用其他的下面有些地方也需要更改.....<br>
    3、工具：Android Studio<br>
    4、其他更改：需要在Constant.java文件中更改BaseUrl，改成云服务器的ip或是项目所在电脑的IPv4 地址，局域网内访问app服务器需要连接电脑WiFi<br>
二、服务端：<br>
    1、项目名：Book_of_exercises_server<br>
    2、python版本：3.0或以上，本项目是3.6<br>
    3、运行服务器：python manage.py runserver 0.0.0.0:8000<br>
    4、其他更改：在settings.py文件下的ALLOWED_HOSTS中添加客服端中更改的ip<br>
三、mongodb数据库：<br>
    1、下载安装对应的mongodb并启动数据库服务<br>

