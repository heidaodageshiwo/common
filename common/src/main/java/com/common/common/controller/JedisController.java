package com.common.common.controller;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.controller
 * @ClassName: JedisController
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-08-19  16:38
 * @UpdateDate: 2022-08-19  16:38
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class JedisController {

    @Resource
    RedisTemplate<String,Object> redisTemplate;

    @GetMapping("/get")
    public Object setValue(){
        Object niubi = redisTemplate.opsForValue().get("name");
        return niubi;
    }

}

