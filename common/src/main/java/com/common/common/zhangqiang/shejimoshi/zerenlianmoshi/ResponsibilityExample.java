package com.common.common.zhangqiang.shejimoshi.zerenlianmoshi;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang
 * @ClassName: zerenlianmoshi
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-20  10:00
 * @UpdateDate: 2022-07-20  10:00
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class ResponsibilityExample {
    public static void main(String[] args) {
        //https://mp.weixin.qq.com/s/HnW2IfyvG_vN6KYS2HMwRQ
        HeadmanHander headmanHander = new HeadmanHander();
        ChiefHander chiefHander = new ChiefHander();
        GmHander gmHander =  new GmHander();

        headmanHander.setNextHandler(chiefHander);
        chiefHander.setNextHandler(gmHander);

        System.out.println("小二哥申请15天假期\n");
        if (headmanHander.handler(15)){
            System.out.println("\n您申请的假期已被批准");
        }else{
            System.out.println("\n最近项目太忙了，暂不批假");
        }
    }
}
