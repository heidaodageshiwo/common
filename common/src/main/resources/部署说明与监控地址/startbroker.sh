


for i in 192.168.56.211 ; do echo ">>> $i";ssh root@$i " export  JAVA_HOME=/opt/jdk1.8.0_202 &&  nohup sh /home/rocketmq/rocketmq/bin/mqbroker -c /home/rocketmq/rocketmq/conf/2m-2s-sync/broker-a.properties > /dev/null 2>&1   ";done
for i in 192.168.56.212; do echo ">>> $i";ssh root@$i "export  JAVA_HOME=/opt/jdk1.8.0_202 &&  nohup sh /home/rocketmq/rocketmq/bin/mqbroker -c /home/rocketmq/rocketmq/conf/2m-2s-sync/broker-a-s.properties > /dev/null 2>&1   ";done
for i in 192.168.56.213; do echo ">>> $i";ssh root@$i "export  JAVA_HOME=/opt/jdk1.8.0_202 &&  nohup sh /home/rocketmq/rocketmq/bin/mqbroker -c /home/rocketmq/rocketmq/conf/2m-2s-sync/broker-b.properties > /dev/null 2>&1  ";done
for i in 192.168.56.214; do echo ">>> $i";ssh root@$i "export  JAVA_HOME=/opt/jdk1.8.0_202 &&  nohup sh /home/rocketmq/rocketmq/bin/mqbroker -c /home/rocketmq/rocketmq/conf/2m-2s-sync/broker-b-s.properties > /dev/null 2>&1   ";done
