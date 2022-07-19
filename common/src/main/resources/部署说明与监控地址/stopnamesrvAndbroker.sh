
# sh /home/rocketmq/rocketmq/bin/mqshutdown namesrv & sh /home/rocketmq/rocketmq/bin/mqshutdown broker
for i in 192.168.56.211 192.168.56.212 192.168.56.213 192.168.56.214; do echo ">>> $i";ssh root@$i "  sh /home/rocketmq/rocketmq/bin/mqshutdown namesrv & sh /home/rocketmq/rocketmq/bin/mqshutdown broker ";done

