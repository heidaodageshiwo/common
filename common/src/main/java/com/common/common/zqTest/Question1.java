package com.common.common.zqTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ProjectName: common1
 * @PackageName: com.common.common.zqTest
 * @ClassName: Question1
 * @Description: java类作用描述
 * @Author: zhangqiang
 * @CreateTime: 2022-07-28  09:50
 * @UpdateDate: 2022-07-28  09:50
 * @UpdateUser: zhangqiang
 * @UpdateRemark: The modified content
 * @Version: 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question1 {
    private String id;            //题目ID
    private String companyId;   //所属企业
    private String catalogId;   //题目所属目录ID
}