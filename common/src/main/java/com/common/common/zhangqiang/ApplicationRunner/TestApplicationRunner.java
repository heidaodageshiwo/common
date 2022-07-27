package com.common.common.zhangqiang.ApplicationRunner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ApplicationRunner
 * @ClassName: TestApplicationRunner
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-27  11:27
 * @UpdateDate: 2022-07-27  11:27
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Component
@Order(1)
public class TestApplicationRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        System.out.println("order1:TestApplicationRunner");
    }
}