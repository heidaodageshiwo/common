package com.common.common.ES8;

import com.common.common.ES8.Builder.BuilderTest;
import com.common.common.ES8.Builder.BuilderTest1;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.ES8
 * @ClassName: test
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-22  14:07
 * @UpdateDate: 2022-09-22  14:07
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class test {

    //测试在单元测试里面
    public static void main(String[] args) {
        BuilderTest zq = BuilderTest.builder().name("zq").build();
        System.out.println(zq.getName()+"\n"+zq.getAddress());
        BuilderTest1 xas = BuilderTest1.builder().name("xas").address("ddf").build();
        System.out.println(xas.getName()+"\n"+xas.getAddress());
    }
}
