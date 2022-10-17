package com.common.common.zhangqiang.ExecutorService.test2;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ExecutorService.test2
 * @ClassName: SingleThreadExecutorTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-17  09:11
 * @UpdateDate: 2022-10-17  09:11
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingleThreadExecutorTest {
    public static void main(String args[]) {
        // 创建线程池对象，设置核心线程和最大线程数为1
        ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        singleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " is running.");
            }
        });
        singleThreadPool.shutdown();
    }
}