package com.common.common.DockerK8s;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.DockerK8s
 * @ClassName: Test1
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-29  14:33
 * @UpdateDate: 2022-07-29  14:33
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

@RestController
public class Test1 {

    @RequestMapping("/")
    public String get1() {
        return "这是根目录";
    }

    @RequestMapping("/get2")
    public HashMap get2() {
        HashMap<String, Object> objectObjectHashMap = new HashMap<>();
        objectObjectHashMap.put("1", "name");
        objectObjectHashMap.put("2", "age");
        objectObjectHashMap.put("3", "sex");
        return objectObjectHashMap;
    }

    @RequestMapping("/get3")
    public String get3() {
        return "这是根目录";
    }
}
