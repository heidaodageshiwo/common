package com.common.common.zhangqiang.ApplicationRunner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ApplicationRunner
 * @ClassName: TestCommandLineRunner
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-27  11:28
 * @UpdateDate: 2022-07-27  11:28
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Component
@Order(2)
public class TestCommandLineRunner implements CommandLineRunner {
    @Override
    public void run(String... strings) throws Exception {
        System.out.println("order2:TestCommandLineRunner");
    }
}
