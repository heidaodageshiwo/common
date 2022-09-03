package com.common.common.zhangqiang.redistest;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.redistest
 * @ClassName: RedisControllers
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-08-27  11:04
 * @UpdateDate: 2022-08-27  11:04
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@RestController
public class RedisControllers {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 添加数据到redis
    @PostMapping("/redis/addstring")
    public String addToRedis(String name, String value) {

        // 操作Redis中的string类型的数据,先获取ValueOperation
        ValueOperations valueOperations = redisTemplate.opsForValue();

        // 添加数据到redis
        valueOperations.set(name, value);
        return "向redis添加string类型的数据";
    }

    // 从redis获取数据
    @GetMapping("/redis/getk")
    public String getData(String key) {

        ValueOperations valueOperations = redisTemplate.opsForValue();
        Object v = valueOperations.get(key);
        return "key是" + key + ",它的值是:" + v;
    }

}
