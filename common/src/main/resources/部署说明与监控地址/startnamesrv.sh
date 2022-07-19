
for i in 192.168.56.211 192.168.56.212 192.168.56.213 192.168.56.214; do echo ">>> $i";ssh root@$i "export  JAVA_HOME=/opt/jdk1.8.0_202 &&  nohup sh /home/rocketmq/rocketmq/bin/mqnamesrv  > /dev/null    ";done

