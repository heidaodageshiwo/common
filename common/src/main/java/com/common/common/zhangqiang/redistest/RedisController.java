package com.common.common.zhangqiang.redistest;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.redistest
 * @ClassName: RedisController
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-08-12  14:35
 * @UpdateDate: 2022-08-12  14:35
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     * http://localhost:8080/redis/operateRedis?cmd=set&args=k11 v11
     * @param cmd
     * @param args
     * @return
     */
    @GetMapping("/redis/operateRedis")
    public String operateRedis(String cmd, String args){
        String result;
        if ("set".equalsIgnoreCase(cmd)){
            String[] s = args.split(" ");
            redisTemplate.opsForValue().set(s[0], s[1]);
            result = "exec success";
        } else if ("get".equalsIgnoreCase(cmd)){
            result = redisTemplate.opsForValue().get(args);
        }else {
            result =  "Unkown command " + cmd;
        }
        return result;
    }
}