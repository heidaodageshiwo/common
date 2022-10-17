package com.common.common.zhangqiang.ExecutorService.test2;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ExecutorService.test2
 * @ClassName: ScheduledThreadPoolTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-17  09:11
 * @UpdateDate: 2022-10-17  09:11
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import java.util.concurrent.*;

public class ScheduledThreadPoolTest {
    public static void main(String args[]) {
        // 创建定时线程池
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);
        // 向线程池提交任务
        scheduledThreadPool.schedule(new Runnable(){
            public void run() {
                System.out.println(Thread.currentThread().getName() + "--->运行");
            }
        }, 5, TimeUnit.SECONDS); // 延迟5s后执行任务
        scheduledThreadPool.shutdown();
    }
}