package com.common.common.zhangqiang.utisl;

import java.text.NumberFormat;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zhangqiang.utisl
 * @ClassName: twonumber
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-10-17  09:19
 * @UpdateDate: 2022-10-17  09:19
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
public class twonumber {
    public static void main(String[] args) {
        NumberFormat percentInstance = NumberFormat.getPercentInstance();
        percentInstance.setMinimumFractionDigits(2);
        String format = percentInstance.format((double) (2) / (double) (5));
        System.out.println(format);//40.00%

    }
}
