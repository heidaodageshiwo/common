package com.common.common.zhangqiang.redistest;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.redistest
 * @ClassName: test
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-29  10:04
 * @UpdateDate: 2022-07-29  10:04
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class test {
    public static void main(String[] args) {
        HashSet<HostAndPort> objects = new HashSet<>();
    objects.add(new HostAndPort("192.168.56.211",7001));
    objects.add(new HostAndPort("192.168.56.211",7002));
    objects.add(new HostAndPort("192.168.56.211",7003));
    objects.add(new HostAndPort("192.168.56.211",7004));
    objects.add(new HostAndPort("192.168.56.211",7005));
    objects.add(new HostAndPort("192.168.56.211",7006));
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        JedisCluster jedisCluster = new JedisCluster(objects,jedisPoolConfig);
        System.out.println(jedisCluster.get("name"));
    }
}
