package com.common.common.zhangqiang.redistest;

import redis.clients.jedis.Jedis;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.redistest
 * @ClassName: ad
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-08-19  16:57
 * @UpdateDate: 2022-08-19  16:57
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class ad {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.56.211", 30000);
        jedis.auth("hvS4Y1AXgE");
//        jedis.select(0);
        String biName = jedis.get("name");
        System.out.println(biName);
    }
}
