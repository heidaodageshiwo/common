package com.common.common;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common
 * @ClassName: FunctionTest
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-09-30  16:23
 * @UpdateDate: 2022-09-30  16:23
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */



import org.junit.jupiter.api.Test;

import java.util.function.Function;

/**
 * function功能测试
 */
public class FunctionTest {
    @Test
    public void test1() {
        Function<Numbers, Integer> test1 = i -> i.getN1() - i.getN2();
        Function<Numbers, Integer> test2 = i -> i.getN1() * i.getN2();
        System.out.println(calculate(test1, 2, 2));
        System.out.println(calculate(test2, 2, 2));
    }

    public Integer calculate(Function<Numbers, Integer> test, Integer number1, Integer number2) {
        Numbers n = new Numbers();
        n.setN1(number1);
        n.setN2(number2);
        return test.apply(n);
    }
}

class Numbers {
    private Integer n1;
    private Integer n2;

    public Integer getN1() {
        return n1;
    }

    public void setN1(Integer n1) {
        this.n1 = n1;
    }

    public Integer getN2() {
        return n2;
    }

    public void setN2(Integer n2) {
        this.n2 = n2;
    }}
