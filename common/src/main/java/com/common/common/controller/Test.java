package com.common.common.controller;

import org.apache.catalina.core.ApplicationContext;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.controller
 * @ClassName: Test
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-08-20  09:19
 * @UpdateDate: 2022-08-20  09:19
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

public class Test {
    public static void main(String[] args) throws Exception {
        testRedisCluster ( null ) ;
    }

    //连接 redisCluster（集群模式）
    static JedisCluster cluster ;
    public static void testRedisCluster( ApplicationContext app  ) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大连接数
        poolConfig.setMaxTotal(10000);
        // 最大空闲数
        poolConfig.setMaxIdle(10000);
        // 最大允许等待时间，如果超过这个时间还未获取到连接，则会报JedisException异常：
        // Could not get a resource from the pool
        poolConfig.setMaxWaitMillis(3000000);
        poolConfig.setTestOnBorrow(true);
        Set<HostAndPort> nodes = new LinkedHashSet<HostAndPort>();
       nodes.add(new HostAndPort("192.168.56.211" ,31000));//换自己的内网IP
        nodes.add(new HostAndPort("192.168.56.211" ,31001));
        nodes.add(new HostAndPort("192.168.56.211" ,31002));
        nodes.add(new HostAndPort("192.168.56.211" ,31100));
        nodes.add(new HostAndPort("192.168.56.211" ,31101));
        nodes.add(new HostAndPort("192.168.56.211" ,31102));
//		cluster = new JedisCluster(nodes, poolConfig  );//		cluster = new JedisCluster(nodes, 5000 , 1000);  //		cluster = new JedisCluster( nodes, 2000, 5, 8, "redis@123", new GenericObjectPoolConfig() );//		cluster.auth("redis@123") ;
        cluster = new JedisCluster(nodes, 50000 , 10000);

//		cluster = app.getBean(JedisCluster.class) ;
//        cluster = new JedisCluster( nodes, 3000, 3000, 8, "", poolConfig );
        String name = cluster.get("name");
        System.out.println(name);
      /*  cluster.set("age", "18");
        System.out.println(cluster.get("age"));*/
        try {
            cluster.close();
        } catch ( Exception e) {
            e.printStackTrace();
        }
    }
}


