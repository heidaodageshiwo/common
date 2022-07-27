package com.common.common.zhangqiang.ApplicationRunner;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ApplicationRunner
 * @ClassName: TestPostConstruct
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-27  11:27
 * @UpdateDate: 2022-07-27  11:27
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Component
public class TestPostConstruct {

    static {
        System.out.println("static");
    }
    public TestPostConstruct() {
        System.out.println("constructer");
    }

    @PostConstruct
    public void init() {
        System.out.println("PostConstruct");
    }
}
