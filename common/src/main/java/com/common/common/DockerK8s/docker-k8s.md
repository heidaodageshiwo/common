



```powershell

博客地址：
https://blog.csdn.net/bobozai86/article/details/88875784


vi /lib/systemd/system/docker.service

ExecStart=/usr/bin/dockerd  -H tcp://0.0.0.0:2375  -H unix:///var/run/docker.sock


#重新加载配置文件
[root@izwz9eftauv7x69f5jvi96z docker]# systemctl daemon-reload    
#重启服务
[root@izwz9eftauv7x69f5jvi96z docker]# systemctl restart docker.service 
#查看端口是否开启
[root@izwz9eftauv7x69f5jvi96z docker]# netstat -nlpt
#直接curl看是否生效
[root@izwz9eftauv7x69f5jvi96z docker]# curl http://127.0.0.1:2375/info

systemctl daemon-reload    
systemctl restart docker.service 
netstat -nlpt
curl http://127.0.0.1:2375/info


# 开放2375端口
firewall-cmd --zone=public --add-port=2375/tcp --permanent
firewall-cmd --reload





```







