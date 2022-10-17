package com.common.common.zhangqiang.ExecutorService.test2;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ExecutorService.test2
 * @ClassName: FixedThreadPoolTest
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

public class FixedThreadPoolTest {
    public static void main(String args[]) {
        // 创建线程池对象，设置核心线程和最大线程数为5
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(5);
        fixedThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + " is running.");
                try {
                    Thread.sleep(10000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("Throw Exception.");
                }
                System.out.println(Thread.currentThread().getName() + " after sleep, is still running.");
            }
        });
        //fixedThreadPool.shutdown();
        fixedThreadPool.shutdownNow(); // 不建议这样使用，很危险，这里仅用于测试
    }
}