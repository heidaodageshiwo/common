package com.common.common.controller;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.controller
 * @ClassName: TestRedisCluster
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-08-20  10:28
 * @UpdateDate: 2022-08-20  10:28
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class TestRedisCluster {
    private static JedisCluster jedis;
    static {
        // 添加集群的服务节点Set集合

    }

    public static void main(String[] args) {
        Set<HostAndPort> hostAndPortsSet = new HashSet<HostAndPort>();
        // 添加节点
        hostAndPortsSet.add(new HostAndPort("192.168.56.211", 31000));
        hostAndPortsSet.add(new HostAndPort("192.168.56.211", 31001));
        hostAndPortsSet.add(new HostAndPort("192.168.56.211", 31002));
        hostAndPortsSet.add(new HostAndPort("192.168.56.211", 31100));
        hostAndPortsSet.add(new HostAndPort("192.168.56.211", 31101));
        hostAndPortsSet.add(new HostAndPort("192.168.56.211", 31102));

        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(100);
        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(500);
        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(0);
        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(200000); // 设置2秒
        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setMinEvictableIdleTimeMillis(10000);
        JedisCluster jedis = new JedisCluster(hostAndPortsSet,  jedisPoolConfig);
        System.out.println("判断某个键是否存在："+jedis.exists("name"));
        System.out.println(jedis.getClusterNodes());
        System.out.println("name："+jedis.get("name"));
        System.out.println("bs："+jedis.get("bs"));
        System.out.println("zz："+jedis.get("zz"));
    }

}
