package com.common.common.zhangqiang.ExecutorService.test2;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.ExecutorService.test2
 * @ClassName: CachedThreadPoolTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-17  09:10
 * @UpdateDate: 2022-10-17  09:10
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedThreadPoolTest {
    public static void main(String args[]) {
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        cachedThreadPool.execute(new Runnable(){
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + "--->运行");
            }
        });
        cachedThreadPool.shutdown();
    }
}